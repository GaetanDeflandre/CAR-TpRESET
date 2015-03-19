package res;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.geronimo.mail.util.Base64;
import org.junit.Test;

import utils.FtpUtils;

public class TestFileResource {

	String fileContent = "Iamque non umbratis fallaciis res agebatur, "
			+ "sed qua palatium est extra muros, armatis omne circumdedit. "
			+ "ingressusque obscuro iam die, ablatis regiis indumentis "
			+ "Caesarem tunica texit et paludamento communi, eum post haec "
			+ "nihil passurum velut mandato principis iurandi crebritate confirmans "
			+ "et statim inquit exsurge et inopinum carpento privato inpositum ad "
			+ "Histriam duxit prope oppidum Polam, ubi quondam peremptum Constantini "
			+ "filium accepimus Crispum.Hinc ille commotus ut iniusta perferens et "
			+ "indigna praefecti custodiam protectoribus mandaverat fidis. quo conperto "
			+ "Montius tunc quaestor acer quidem sed ad lenitatem propensior, consulens "
			+ "in commune advocatos palatinarum primos scholarum adlocutus est mollius "
			+ "docens nec decere haec fieri nec prodesse addensque vocis obiurgatorio "
			+ "sonu quod si id placeret, post statuas Constantii deiectas super adimenda "
			+ "vita praefecto conveniet securius cogitari.";
	
	@Test
	public void testGetFile() throws SocketException, IOException {
		FileResource resource = new FileResource();
		String userTest = "test", mdpTest = "testmdp";
		String filenameTest = "JUnit_test";
		FTPClient client = new FTPClient();
		OutputStream out;
		InputStream in;
		byte[] authorizationHeader;
		byte[] readBuffer = new byte[1];
		String fileRetrieved = "";
		
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);
		client.login(userTest, mdpTest);
		
		// Stockage du fichier de test
		out = client.storeFileStream(filenameTest);
		out.write(fileContent.getBytes());
		out.close();
		
		client.logout();
		client.disconnect();
		
		authorizationHeader = Base64.encode((userTest+":"+mdpTest).getBytes());
		in = resource.getFile(filenameTest, userTest, "Basic "+new String(authorizationHeader));
		
		while (in.read(readBuffer) != -1)
			fileRetrieved += new String(readBuffer);
		
		in.close();
		
		assertEquals(fileContent, fileRetrieved);
	}
	
	@Test
	public void testPutFile() throws SocketException, IOException {
		FileResource resource = new FileResource();
		String userTest = "test", mdpTest = "testmdp";
		String filenameTest = "JUnit_test2";
		FTPClient client = new FTPClient();
		OutputStream out;
		InputStream in;
		byte[] authorizationHeader;
		byte[] readBuffer = new byte[1];
		Response res;
		String fileRetrieved="";
		
		in = new ByteArrayInputStream(fileContent.getBytes());
		authorizationHeader = Base64.encode((userTest+":"+mdpTest).getBytes());
		res = resource.storeFile(filenameTest, userTest, in, "Basic "+new String(authorizationHeader));
		assertEquals(200, res.getStatus());
		
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);
		client.login(userTest, mdpTest);
		
		// Stockage du fichier de test
		in = client.retrieveFileStream(filenameTest);
		
		while (in.read(readBuffer) != -1)
			fileRetrieved += new String(readBuffer);
		
		in.close();
		client.logout();
		client.disconnect();
		
		System.out.println(fileRetrieved);
		
		assertEquals(fileContent, fileRetrieved);
	}
	
}
