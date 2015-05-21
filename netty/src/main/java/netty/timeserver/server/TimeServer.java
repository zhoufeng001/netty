package netty.timeserver.server;

import java.util.Date;

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

public class TimeServer {

	public void bind(int port) throws Exception {

		EventLoopGroup boosGroup = new NioEventLoopGroup() ;
		EventLoopGroup workerGroup = new NioEventLoopGroup() ;
		try{
			ServerBootstrap server = new ServerBootstrap() ;
			server.group(boosGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(new ChildChannelHandler());  

			ChannelFuture f = server.bind(port).sync();
			
			System.out.println("服务器启动成功...");   
			
			f.channel().closeFuture().sync(); 
			
			System.out.println("服务端关闭"); 
		}finally{
			boosGroup.shutdownGracefully() ;
			workerGroup.shutdownGracefully();
		}

	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new TimeServerHandler()); 
		}
		
	}
	
	private class TimeServerHandler extends ChannelHandlerAdapter{
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			System.out.println("准备读取消息");
			ByteBuf buf = (ByteBuf)msg ;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req , "UTF-8") ;
			System.out.println("The time server receive order:" + body);
			String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toLocaleString() : "BAD ORDER";
			ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
			ctx.write(resp); 
		}
		
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx)
				throws Exception {
			ctx.flush();
			System.out.println("读取消息完成，并发送消息完成");
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			System.out.println(cause.getMessage()); 
			ctx.close();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		int port = 9999 ;
		new TimeServer().bind(port);  
		
		
	}

}
