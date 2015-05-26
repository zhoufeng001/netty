package netty.updown;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class MessageDecodeHandler extends MessageToMessageDecoder<ByteBuf>{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		Message message = new Message() ;
		int code = msg.readInt();
		message.setCode(code); 
		byte[] rcvBytes = new byte[msg.readableBytes()];
		msg.readBytes(rcvBytes);
		message.setMsg(new String(rcvBytes)); 
		out.add(message);
	}

	
}


