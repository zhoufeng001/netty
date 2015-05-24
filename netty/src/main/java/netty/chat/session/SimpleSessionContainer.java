package netty.chat.session;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleSessionContainer implements SessionContainer{
	
	private static SimpleSessionContainer instance = new SimpleSessionContainer() ;
	
	private ConcurrentHashMap<String, ChannelHandlerContext> contextMap = new ConcurrentHashMap<String, ChannelHandlerContext>();
	
	private SimpleSessionContainer() {}
	
	public static SessionContainer getInstance(){
		return instance ;
	}

	public ChannelHandlerContext getContextByUsername(String username) {
		return contextMap.get(username);
	}

	public void put(String username, ChannelHandlerContext context) {
		contextMap.putIfAbsent(username, context) ;
	}

	public Collection<ChannelHandlerContext> getAllContext() {
		return Collections.synchronizedCollection(contextMap.values()); 
	}

	public void delete(String username) {
		contextMap.remove(username);
	}

	public void delete(ChannelHandlerContext context) {
		 Iterator<String> keysIterator = contextMap.keySet().iterator();
		 while(keysIterator.hasNext()){
			String username = keysIterator.next();
			ChannelHandlerContext ctx = contextMap.get(username);
			if(ctx.equals(context)){
				contextMap.remove(username);
				break ;
			}
		 }
	}

	
}
