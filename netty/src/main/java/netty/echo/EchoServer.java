package netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

	public void bind(int port) throws Exception{
		EventLoopGroup boos = new NioEventLoopGroup() ;
		EventLoopGroup child = new NioEventLoopGroup() ;
		ServerBootstrap server = new ServerBootstrap() ;
		server.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG,1024)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ByteBuf delimitor = Unpooled.copiedBuffer("_$".getBytes());
				ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimitor)) ;
				ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));  
				ch.pipeline().addLast(new EchoServerHandler());
			}
		})
		.group(boos, child)
		;
		ChannelFuture f  = server.bind(port).sync();
		System.out.println("服务器启动成功...");
		f.channel().closeFuture().sync();
		System.out.println("服务器关闭成功"); 
		boos.shutdownGracefully() ;
		child.shutdownGracefully() ;
	}

	private class EchoServerHandler extends ChannelHandlerAdapter{
		
		private int counter = 0 ;
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			ByteBuf reqBuf = (ByteBuf)msg ;
			byte[] reqData = new byte[reqBuf.readableBytes()];
			reqBuf.readBytes(reqData); 
//			System.out.println("Receive client : [" + new String(reqData)  +"] counter:" + ++counter);  
			ChatPacket reqPkg = ChatPacket.wrapInstance(reqData);  
			System.out.println(reqPkg);   
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

	}
  
	public static void main(String[] args) throws Exception {

		new EchoServer().bind(8888); 

	}

}
