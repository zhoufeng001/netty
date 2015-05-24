package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        for (int i = 0; i < 100 ; i++) {
        	ByteBuf reqData = Unpooled.buffer();
			reqData.writeInt(i);
			reqData.writeBytes(("hello" + i).getBytes());
			byte[] bytes = new byte[reqData.readableBytes()];
			reqData.readBytes(bytes);
			for (int j = 0; j < bytes.length; j++) {
				if(bytes[j] == '\001'){
					System.out.println(i + " " + j); 
					break ;
				}
			}
		}
    }
}
