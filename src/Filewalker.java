/* Filewalker Texas Ranger */

import java.util.*;
import java.io.File;
import java.security.MessageDigest;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Filewalker {

	public void walk ( String path, HashMap hm ){
		File root = new File(path);
		File[] list = root.listFiles();
		
		if (list==null) return;
		
		for (File f : list){
			if (f.isDirectory()){
				walk(f.getAbsolutePath(),hm);
			}else{
				MessageDigest shaDigest;
				String checksum;
				
				try{
					shaDigest = MessageDigest.getInstance("SHA-1");
					checksum = getFileChecksum(shaDigest, f);
					
					if (hm.containsKey(checksum)){
						System.out.println(f.getAbsolutePath());
					}
					else{
						hm.put(checksum, f.getAbsoluteFile());
					}
					
				}
				  catch (IOException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public static void main(String[] args){
		
		HashMap<String,File> hm = new HashMap<String,File>();
		
		System.out.println("Duplicate Files exist at:");
		
		Filewalker fw = new Filewalker();
		fw.walk("/home/oracle/Documents/Duplicates",hm);
	
	}

	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
		//Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		//Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0; 

		//Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		};

		//close the stream; We don't need it now.
		fis.close();

		//Get the hash's bytes
		byte[] bytes = digest.digest();

		//This bytes[] has bytes in decimal format;
		//Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++)
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		//return complete hash
		return sb.toString();
	}


}