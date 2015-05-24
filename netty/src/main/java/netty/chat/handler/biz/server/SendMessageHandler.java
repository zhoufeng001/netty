package netty.chat.handler.biz.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.Iterator;

import netty.chat.dto.ReceiveMessageResponse;
import netty.chat.dto.SendMessageRequest;
import netty.chat.handler.biz.ChatMessageBizHandler;
import netty.chat.session.SessionContainer;

public class SendMessageHandler extends ChatMessageBizHandler{

	private SessionContainer sessionContainer ;

	public SendMessageHandler(SessionContainer sessionContainer) {
		this.sessionContainer = sessionContainer ;
	}
	
	@Override
	public void handle(int code, Object data, ChannelHandlerContext ctx) {
		System.out.println("SendMessageHandler...");
		SendMessageRequest request = (SendMessageRequest)data ;
		ReceiveMessageResponse response = new ReceiveMessageResponse() ;
		Object userObj = ctx.channel().attr(AttributeKey.valueOf("username")).get();
		if(userObj == null){
			response.setCode(-1); //用户未登录
			writeData(ctx, 3,response); 
			return ;
		}
		response.setSender(userObj.toString());
		response.setReciver(request.getReceiver()); 
		response.setMessage(request.getMessage()); 
		response.setPriv(request.isPriv()); 
		String reciver = request.getReceiver();

		if(reciver == null){
			response.setCode(0); 
			Iterator<ChannelHandlerContext> sessions =  sessionContainer.getAllContext().iterator();
			while(sessions.hasNext()){
				ChannelHandlerContext reciverSession = sessions.next();
				writeData(reciverSession, 4,response); 
			}
		}else{
			ChannelHandlerContext reciverSession = sessionContainer.getContextByUsername(userObj.toString());
			if(reciverSession == null){
				response.setCode(-2); //接收人为空
				writeData( ctx, 3,response);
				return ;
			}
			if(request.isPriv()){
				response.setCode(0); 
				writeData( reciverSession, 4,response);
				return; 
			}else{
				response.setCode(0); 
				Iterator<ChannelHandlerContext> sessions =  sessionContainer.getAllContext().iterator();
				while(sessions.hasNext()){
					reciverSession = sessions.next();
					writeData(reciverSession,4, response); 
				}
			}
		}
	}

	@Override
	public Class<?> getRequestDataClass() {
		return SendMessageRequest.class;
 	}



}
