package netty.chat.handler.biz.server;

import netty.chat.dto.BasicResponse;
import netty.chat.handler.biz.ChatMessageBizHandler;
import netty.chat.session.SessionContainer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 退出处理
 * @author Administrator
 *
 */
public class LogoutHandler extends ChatMessageBizHandler{
	
	private SessionContainer sessionContainer ;
	
	public LogoutHandler(SessionContainer sessionContainer) {
		this.sessionContainer = sessionContainer ;
	}

	public void handle(int code, Object data, ChannelHandlerContext ctx) {
		System.out.println("LogoutHandler...");
		BasicResponse response = new BasicResponse() ;
		Object usernameObj = ctx.channel().attr(AttributeKey.valueOf("username")).get();
		if(usernameObj == null){
			response.setCode(-1); //用户未登录
			writeData(ctx, 2,response); 
			return ;
		}
		ctx.channel().attr(AttributeKey.valueOf("username")).remove();
		sessionContainer.delete(usernameObj.toString()); 
		response.setCode(0);
		writeData(ctx, 2,response); 
	}

	@Override
	public Class<?> getRequestDataClass() {
		return null;
	}

	
}
