import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


public class Test5 {
	public static void main(String[] args) {
		int a=3;
		int b=5;
		b-=2;
		
//		System.out.println(b);
		
		Test5 t = new Test5();
//		System.out.println(t.getIpLocal());
		System.out.println(t.getIp());
		t.writeFile(t.getIp());
		t.writeFtpFile();
		String tt = t.code("test");
		String ttt = t.decode(tt);
		System.out.println(tt);
		System.out.println(ttt);
	}
	
	public List<String> getIpLocal() {
		String ipLocal = "";
		String ipInternet = "";
		try(final DatagramSocket socket = new DatagramSocket()){
		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
		  ipLocal = socket.getLocalAddress().getHostAddress();
		  ipInternet = socket.getInetAddress().getHostAddress();		  
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		List<String> l = new ArrayList<>();
		l.add(ipLocal);
		l.add(ipInternet);
		return l;
	}
	
	public String getIp() {
		String ip = "";
		
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
	                whatismyip.openStream()));
			ip = in.readLine(); //you get the IP as a String

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ip;
	}
	
	public void writeFile(String content) {
//        String content = "This is the content to write into file\n";

        // If the file doesn't exists, create and write to it
		// If the file exists, truncate (remove all content) and write to it
        try (FileWriter writer = new FileWriter("ip.txt");
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(content);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
	}
	
	public String code(String originalInput) {
//		String originalInput = "test input";
		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
		return encodedString;
	}
	
	public String decode(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);
		return decodedString;
	}
	
	public void writeFtpFile() {
        String server = "8.8.8.8";
        int port = 21;
        String user = decode("111");
        String pass = decode("111");
 
        FTPClient ftpClient = new FTPClient();
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
 
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File("ip.txt");
 
            String firstRemoteFile = "httpdocs/ip.txt";
            InputStream inputStream = new FileInputStream(firstLocalFile);
 
            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
 
//            // APPROACH #2: uploads second file using an OutputStream
//            File secondLocalFile = new File("E:/Test/Report.doc");
//            String secondRemoteFile = "test/Report.doc";
//            inputStream = new FileInputStream(secondLocalFile);
// 
//            System.out.println("Start uploading second file");
//            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
//            byte[] bytesIn = new byte[4096];
//            int read = 0;
// 
//            while ((read = inputStream.read(bytesIn)) != -1) {
//                outputStream.write(bytesIn, 0, read);
//            }
//            inputStream.close();
//            outputStream.close();
// 
//            boolean completed = ftpClient.completePendingCommand();
//            if (completed) {
//                System.out.println("The second file is uploaded successfully.");
//            }
 
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}

}
