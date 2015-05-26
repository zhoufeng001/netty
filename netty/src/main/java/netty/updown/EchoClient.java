package netty.updown;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoClient {


	public void connect(String host , int port) throws Exception{
		EventLoopGroup loop = new NioEventLoopGroup();
		Bootstrap client = new Bootstrap(); 
		client.channel(NioSocketChannel.class)
		.group(loop)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4,0,4));
				ch.pipeline().addLast(new MessageDecodeHandler()) ;
				ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG)) ;
				ch.pipeline().addLast(new MessageEncodeHandler()) ;		 	
				ch.pipeline().addLast(new EchoClientChannelHandler()) ;  
			}
		});
		ChannelFuture f = client.connect(host, port).sync();
		System.out.println("连接服务器成功...");
		f.channel().closeFuture().sync();
		System.out.println("客户端关闭成功...");
	}

	private class EchoClientChannelHandler extends ChannelHandlerAdapter{
		
		int counter ;
		
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			for (int i = 0; i < 100 ; i++) {
				Message msg = new Message();
				msg.setCode(i);
				msg.setMsg("hello world" + i);
				ctx.writeAndFlush(msg);
			}
			ctx.fireChannelActive() ;
		}
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			Message message = (Message)msg ;
			System.out.println("客户端收到消息：" + ++counter + " " + message); 
		}
		
		@Override
		public void write(ChannelHandlerContext ctx, Object msg,
				ChannelPromise promise) throws Exception {
			super.write(ctx, msg, promise);
		}
	}

	public static void main(String[] args) throws Exception {

		new EchoClient().connect("127.0.0.1", 9999);  

	}

}
