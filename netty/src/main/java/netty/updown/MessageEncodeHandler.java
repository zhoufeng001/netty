package netty.updown;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncodeHandler extends MessageToByteEncoder<Message>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out)
			throws Exception {
		
		byte[] msgByte = msg.getMsg().getBytes();
		out.writeInt(msgByte.length + 4); 
		out.writeInt(msg.getCode());
		out.writeBytes(msgByte);		
		
	}

}


