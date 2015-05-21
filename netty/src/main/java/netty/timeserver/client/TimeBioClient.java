package netty.timeserver.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TimeBioClient {
	
	public static void main(String[] args) throws Exception {
		
		Socket socket = new Socket("127.0.0.1" , 9999);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		os.write("QUERY TIME ORDER".getBytes());
		os.flush();
		byte[] buf = new byte[1024];
		int len = is.read(buf);
		System.out.println(new String(buf , 0 , len));
		is.close();
		os.close();
		socket.close();
	}

}
