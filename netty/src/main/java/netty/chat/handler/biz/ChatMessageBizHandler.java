package netty.chat.handler.biz;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import org.msgpack.MessagePack;

public abstract class ChatMessageBizHandler {
	
	public abstract void handle(int code , Object data , ChannelHandlerContext ctx);
	
	public abstract Class<?> getRequestDataClass();

	protected void writeData(ChannelHandlerContext ctx,int responseCode , Object data){
		ByteBuf sendBuf = Unpooled.buffer();
		try {
			MessagePack msgPkg = new MessagePack();
			byte[] dataBytes = msgPkg.write(data);
			sendBuf.writeInt(responseCode) ;
			sendBuf.writeBytes(dataBytes) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		ctx.writeAndFlush(sendBuf);  
	}	
}
