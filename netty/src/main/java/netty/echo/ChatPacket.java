package netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class ChatPacket {
	
	private int code ;
	private byte[] data ;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return String.format("code:[%d],msg:[%s]", code , new String(data)) ;
	}
	
	public static ChatPacket wrapInstance(byte[] bytes) throws Exception{
		if(bytes == null || bytes.length < 4){
			throw new IllegalArgumentException("字节数组的长度必须大于4位");
		}
		ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
		int code = byteBuf.getInt(0);
		ChatPacket reqPkg = new ChatPacket() ;
		reqPkg.setCode(code);
		if(bytes.length > 4){
			byte[] msgData = new byte[bytes.length - 4];
			byteBuf.getBytes(4, msgData); 
			reqPkg.setData(msgData);
		}
		return reqPkg ;
	}
	
}
