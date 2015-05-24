package netty.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;

import netty.chat.BizMessageHandlerMap;
import netty.chat.dto.LoginRequest;
import netty.chat.dto.SendMessageRequest;
import netty.chat.handler.ChatMessageHandler;
import netty.chat.handler.ChatMessageToPacketHandler;
import netty.chat.handler.biz.client.LoginResponseHandler;
import netty.echo.ChatPacket;

import org.msgpack.MessagePack;

public class TestChatClient {
	
	private void doLogin(ChannelHandlerContext ctx) throws IOException{
		MessagePack msgPkg = new MessagePack() ;
		//登录
		ChatPacket loginReqPkg = new ChatPacket() ;
		loginReqPkg.setCode(1); 
		LoginRequest loginRequest = new LoginRequest() ;
		loginRequest.setUsername("user1");
		loginRequest.setPassword("pass1");  
		loginReqPkg.setData(msgPkg.write(loginRequest)); 
		
		ByteBuf sendData = Unpooled.buffer();
		sendData.writeInt(loginReqPkg.getCode());  
		sendData.writeBytes(loginReqPkg.getData());
		ctx.writeAndFlush(sendData);
	}
	
	private void sendMessage(ChannelHandlerContext ctx) throws IOException{
		MessagePack msgPkg = new MessagePack() ;
		//登录
		ChatPacket sendMsgPkg = new ChatPacket() ;
		sendMsgPkg.setCode(3); 
		SendMessageRequest sendMessageRequest = new SendMessageRequest() ;
		sendMessageRequest.setMessage("hello!");
		sendMessageRequest.setPriv(false);
		sendMsgPkg.setData(msgPkg.write(sendMessageRequest)); 
		
		ByteBuf sendData = Unpooled.buffer();
		sendData.writeInt(sendMsgPkg.getCode());
		sendData.writeBytes(sendMsgPkg.getData());
		ctx.writeAndFlush(sendData);
	}
	
	public void connect(String host , int port) throws Exception{
		EventLoopGroup loop = new NioEventLoopGroup();
		Bootstrap client = new Bootstrap(); 
		client.channel(NioSocketChannel.class)
		.group(loop)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("lengthFielddecoder" , new LengthFieldBasedFrameDecoder(10240, 0,4,0,4)); 
				ch.pipeline().addLast("logger",new LoggingHandler(LogLevel.DEBUG));  
				ch.pipeline().addLast("lengthPrepender",new LengthFieldPrepender(4)); 
				ch.pipeline().addLast("translate",new ChatMessageToPacketHandler());  
				ch.pipeline().addLast("biz",new ChatMessageHandler()) ; 
				
				//测试
				ch.pipeline().addLast(new ChannelHandlerAdapter(){
					
					@Override
					public void channelActive(ChannelHandlerContext ctx)
							throws Exception {
						
						TestChatClient.this.doLogin(ctx);
						
						TestChatClient.this.sendMessage(ctx);
					}
					
				});
				
			}
		});
		ChannelFuture f = client.connect(host, port).sync();
		System.out.println("连接服务器成功...");
		f.channel().closeFuture().sync();
		System.out.println("客户端关闭成功...");
	}
	  
	
	public static void main(String[] args) throws Exception {
		BizMessageHandlerMap handlerMap = BizMessageHandlerMap.getInstance();
		handlerMap.regist(1, new LoginResponseHandler());  
		
		new TestChatClient().connect("127.0.0.1", 8888); 
		
	}

}
