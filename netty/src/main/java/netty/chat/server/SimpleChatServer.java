package netty.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.chat.BizMessageHandlerMap;
import netty.chat.handler.ChatMessageHandler;
import netty.chat.handler.ChatMessageToPacketHandler;
import netty.chat.handler.SessionContainerHandler;
import netty.chat.handler.biz.server.LoginHandler;
import netty.chat.handler.biz.server.LogoutHandler;
import netty.chat.handler.biz.server.SendMessageHandler;
import netty.chat.session.SessionContainer;
import netty.chat.session.SimpleSessionContainer;
import netty.chat.user.SimpleUserService;
import netty.chat.user.UserService;

public class SimpleChatServer {

	private int port ;

	private UserService userService ;

	private SessionContainer sessionContainer ;
	
	private ServerBootstrap serverBootstrap;

	private EventLoopGroup boosGroup ;

	private EventLoopGroup childGroup ;

	public SimpleChatServer(int port , UserService userService,SessionContainer sessionContainer) {
		this.port = port ;
		this.userService = userService ;
		this.sessionContainer = sessionContainer; 
	}

	public void start() throws Exception{
		serverBootstrap = new ServerBootstrap() ;
		boosGroup = new NioEventLoopGroup() ;
		childGroup = new NioEventLoopGroup() ;
		serverBootstrap
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG,1024)
		.group(boosGroup,childGroup)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("lengthFielddecoder" , new LengthFieldBasedFrameDecoder(10240, 0,4,0,4)); 
				ch.pipeline().addLast("logger",new LoggingHandler(LogLevel.DEBUG));  
				ch.pipeline().addLast("lengthPrepender",new LengthFieldPrepender(4)); 
				ch.pipeline().addLast("translate",new ChatMessageToPacketHandler()) ;   
				ch.pipeline().addLast("biz",new ChatMessageHandler()) ;   
				ch.pipeline().addFirst("sessionContainer",new SessionContainerHandler(sessionContainer)) ;   
			} 
		});
		ChannelFuture f = serverBootstrap.bind(port).sync();
		System.out.println("服务器启动成功...");
		f.channel().closeFuture().sync();
		System.out.println("服务器关闭成功...");  
	}
	
	public static void main(String[] args) throws Exception {
		
		UserService userService = new SimpleUserService() ;
		
		SessionContainer sessionContainer = SimpleSessionContainer.getInstance();
		
		BizMessageHandlerMap bizHandler = BizMessageHandlerMap.getInstance(); 
		
		bizHandler.regist(1, new LoginHandler(userService, sessionContainer)); 
		bizHandler.regist(2, new LogoutHandler(sessionContainer)); 
		bizHandler.regist(3, new SendMessageHandler(sessionContainer)); 
		
		new SimpleChatServer(8888, userService ,sessionContainer).start() ;  
		
	}

}
