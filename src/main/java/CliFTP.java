import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class CliFTP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FTPClient f = new FTPClient();
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		conf.setServerLanguageCode("en_EN");
		f.configure(conf);

		try {
			f.connect("127.0.0.1", 2121);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			f.login("toto", "mdp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FTPFile[] files = f.listFiles();
			System.out.println("nu: " + files.length);
			for (FTPFile file : files) {
				System.out.println(file.toFormattedString());
			}

			f.quit();
			f.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
