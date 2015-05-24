package netty.msgpack;

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
import io.netty.handler.codec.LengthFieldPrepender;
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
				ch.pipeline().addLast("logger",new LoggingHandler(LogLevel.DEBUG)) ;
				ch.pipeline().addLast("lengthPrepender",new LengthFieldPrepender(4)); 
				ch.pipeline().addLast("echo",new EchoClientChannelHandler()) ;  
			}
		});
		ChannelFuture f = client.connect(host, port).sync();
		System.out.println("连接服务器成功...");
		f.channel().closeFuture().sync();
		System.out.println("客户端关闭成功...");
	}
	
	private class EchoClientChannelHandler extends ChannelHandlerAdapter{
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			for (int i = 0; i < 100 ; i++) {
				ByteBuf reqData = Unpooled.buffer();
				reqData.writeInt(i);
				reqData.writeBytes(("hello" + i).getBytes());
				ctx.write(reqData);  
			}
			ctx.flush();
			ctx.fireChannelActive() ;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		new EchoClient().connect("127.0.0.1", 8888);  
 		
	}

}
