package netty.chat.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.echo.ChatPacket;

public class ChatMessageToPacketHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf reqBuf = (ByteBuf)msg ;
		byte[] reqData = new byte[reqBuf.readableBytes()];
		reqBuf.readBytes(reqData); 
		ChatPacket reqPkg = ChatPacket.wrapInstance(reqData);  
		ctx.fireChannelRead(reqPkg);
	}

}
