package netty.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import netty.chat.handler.biz.ChatMessageBizHandler;


public class BizMessageHandlerMap {

	private BizMessageHandlerMap(){}

	private static final BizMessageHandlerMap instance = new BizMessageHandlerMap();
	
	private static final Map<Integer, ChatMessageBizHandler> handlerMap = new ConcurrentHashMap<Integer, ChatMessageBizHandler>() ;
	
	public static BizMessageHandlerMap getInstance(){
		return instance ;
	}

	public void regist(Integer code , ChatMessageBizHandler handler){
		handlerMap.put(Integer.valueOf(code), handler) ; 
	}

	public ChatMessageBizHandler getHandler(Integer code){
		return handlerMap.get(code);
	}
	
}
