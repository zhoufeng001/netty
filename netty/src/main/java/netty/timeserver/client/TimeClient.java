package netty.timeserver.client;

import io.netty.bootstrap.Bootstrap;
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
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class TimeClient {

	public void connect(String host , int port)throws Exception{
		
		EventLoopGroup group = new NioEventLoopGroup() ;
		try{
			Bootstrap client = new Bootstrap() ;
			client.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new TimeClientHandler()) ;
				}
			});
			ChannelFuture f = client.connect(new InetSocketAddress(host , port)).sync();
			System.out.println("连接服务器成功"); 
			f.channel().closeFuture().sync();
			System.out.println("客户端关闭");
		}finally{
			group.shutdownGracefully() ;
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8888 ;  
		new TimeClient().connect("127.0.0.1", port); 

	}

	private class TimeClientHandler extends ChannelHandlerAdapter{

		private final ByteBuf firstMessage ;

		public TimeClientHandler() {
			byte[] req = "QUERY TIME ORDER".getBytes();
			firstMessage = Unpooled.buffer(req.length); 
			firstMessage.writeBytes(req);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ctx.writeAndFlush(firstMessage);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			ByteBuf buf = (ByteBuf)msg ;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req);
			System.out.println("Now is : " + body); 
			Thread.sleep(1000);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			System.err.println(cause.getMessage()); 
			ctx.close();
		}
	}

}
