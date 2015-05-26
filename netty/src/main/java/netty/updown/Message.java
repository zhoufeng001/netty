package netty.updown;
public class Message {
	
	private int code ;
	
	private String msg ;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Message [code=" + code + ", msg=" + msg + "]";
	}
	
}


