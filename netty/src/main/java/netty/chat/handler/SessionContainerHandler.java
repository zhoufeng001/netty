package netty.chat.handler;

import netty.chat.session.SessionContainer;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class SessionContainerHandler extends ChannelHandlerAdapter{
	
	private SessionContainer sessionContainer ;
	
	public SessionContainerHandler(SessionContainer sessionContainer) {
		this.sessionContainer = sessionContainer;
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		clearSession(ctx); 
		ctx.fireChannelInactive(); 
	}
	
	private void clearSession(ChannelHandlerContext ctx){
		System.out.println("清除用户Session..."); 
		Object obj = ctx.channel().attr(AttributeKey.valueOf("username")).get();
		if(obj != null){
			sessionContainer.delete(obj.toString()); 
		}
	}

}
