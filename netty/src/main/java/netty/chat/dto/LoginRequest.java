package netty.chat.dto;

import org.msgpack.annotation.Message;

@Message
public class LoginRequest {
	
	private String username ;
	private String password ;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
