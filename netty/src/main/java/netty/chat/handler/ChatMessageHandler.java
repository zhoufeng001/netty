package netty.chat.handler;

import java.io.IOException;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.chat.BizMessageHandlerMap;
import netty.chat.handler.biz.ChatMessageBizHandler;
import netty.echo.ChatPacket;

import org.msgpack.MessagePack;

public class ChatMessageHandler extends ChannelHandlerAdapter{

	BizMessageHandlerMap handlerMap = BizMessageHandlerMap.getInstance();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ChatPacket pkg = (ChatPacket)(msg);
		processMessage(pkg,ctx);
		ctx.fireChannelRead(msg);
	}

	private void processMessage(ChatPacket pkg ,ChannelHandlerContext ctx){
		ChatMessageBizHandler handler = handlerMap.getHandler(pkg.getCode());
		if(handler == null){
			System.out.println("code[" + pkg.getCode() + "]没有对应的处理器");
			return ;
		}
		MessagePack decoder = new MessagePack() ;
		Object data = null;
		try {
			data = decoder.read(pkg.getData(), handler.getRequestDataClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.handle(pkg.getCode(), data, ctx);  
	}

}
