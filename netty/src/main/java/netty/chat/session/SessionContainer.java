package netty.chat.session;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;

public interface SessionContainer {
	
	ChannelHandlerContext getContextByUsername(String username);
	
	void put(String username , ChannelHandlerContext context);
	
	void delete(String username);
	
	void delete(ChannelHandlerContext context);

	Collection<ChannelHandlerContext> getAllContext();
	
}
