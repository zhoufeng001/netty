package netty.chat.handler.biz.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import netty.chat.dto.BasicResponse;
import netty.chat.dto.LoginRequest;
import netty.chat.handler.biz.ChatMessageBizHandler;
import netty.chat.session.SessionContainer;
import netty.chat.user.User;
import netty.chat.user.UserService;

/**
 * 登录处理
 */
public class LoginHandler extends ChatMessageBizHandler{
	
	private UserService userService ;
	private SessionContainer sessionContainer ;
	public LoginHandler(UserService userService,SessionContainer sessionContainer) {
		this.userService = userService ;
		this.sessionContainer = sessionContainer ;
	}

	public void handle(int code, Object data ,ChannelHandlerContext ctx) {
		System.out.println("LoginHandler...");
		LoginRequest request = (LoginRequest) data ;
		BasicResponse response = new BasicResponse() ;
		if(request == null){
			response.setCode(-1);  //请求内容不能为空
			super.writeData(ctx, 1,response); 
			return; 
		}
		User user = userService.getUser(request.getUsername()); 
		if(user == null){
			response.setCode(-2); //用户名不存在
			super.writeData(ctx,1, response); 
			return; 
		}
		if(user.getPassword().equals(request.getPassword())){
			response.setCode(0);
			ctx.channel().attr(AttributeKey.valueOf("username")).set(request.getUsername());
			sessionContainer.put(request.getUsername(), ctx);  
 		}else{
			response.setCode(-3); //密码错误
		}
		super.writeData(ctx, 1,response);
	}

	@Override
	public Class<?> getRequestDataClass() {
		return LoginRequest.class;
	}

}
