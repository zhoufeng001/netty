package netty.chat.handler.biz.client;

import io.netty.channel.ChannelHandlerContext;
import netty.chat.dto.BasicResponse;
import netty.chat.handler.biz.ChatMessageBizHandler;

public class LoginResponseHandler extends ChatMessageBizHandler{

	@Override
	public void handle(int code, Object data, ChannelHandlerContext ctx) {
		System.out.println("LoginResponseHandler...");
		BasicResponse response = (BasicResponse)data ;
		if(response.getCode() == 0){
			System.out.println("登录成功");
		}else if(response.getCode() == -1){
			System.out.println("请求内容不能为空");
		}else if(response.getCode() == -2){
			System.out.println("用户名不存在");
		}else if(response.getCode() == -3){
			System.out.println("密码错误");
		}else{
			System.out.println("未知状态码[" + response.getCode() + "]"); 
		}
	}
	
	@Override
	public Class<?> getRequestDataClass() {
		return BasicResponse.class;
	}

	
}
