package translate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
	public static String MD5(String message) {
		
		MessageDigest md = null;
		byte md5[] = null; 
	    String result = "";
		try {
			md = MessageDigest.getInstance("md5");
			md5 = md.digest(message.getBytes("UTF-8"));
			if (md5 == null) {
				MainWindow.setErrorFlag(2);
				return "";
			}
			StringBuffer stringBuffer = new StringBuffer();
			for (byte b : md5) {
				int bt = b&0xff; 
		        if (bt < 16){ 
		          stringBuffer.append(0); 
		        }  
		        stringBuffer.append(Integer.toHexString(bt)); 
			}
			result = stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			MainWindow.setErrorFlag(2);
			return "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			MainWindow.setErrorFlag(2);
			return "";
		}
		
		return result;
	}
}
