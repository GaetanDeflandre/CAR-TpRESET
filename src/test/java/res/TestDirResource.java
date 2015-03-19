package res;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.SocketException;

import org.apache.geronimo.mail.util.Base64;
import org.junit.Test;

import plateform.exceptions.UnauthorizedException;

public class TestDirResource {

	private String TEST_ROOT_CONTENT_HTML = "<html><!DOCTYPE HTML><head><meta charset=\"UTF-8\" /><title>Liste des fichiers</title><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\" /></head><body><h1>Fichier(s) de /</h1><hr /><ul><li><a href=\"http://localhost:8080/rest/api/test/dir/cdup\"><strong>Dossier parent &#8682;</strong></a></li><li><a href=\"http://localhost:8080/rest/api/test/dir/folder\">folder</a></li><li><a href=\"http://localhost:8080/rest/api/test/file/test.txt\">test.txt</a></li></ul></body></html>";
	private String TEST_ROOT_CONTENT_JSON = "{\"path\" : \"/\",\"username\" : \"test\",\"files\" : [\"value\" : \"drwxrwxr-x 3 gaetan gaetan 4096 Mar 18 20:47 folder\",\"value\" : \"-rw-rw-r-- 1 gaetan gaetan  731 Mar 18 20:41 test.txt\"]}\n";

	@Test
	public void testDirHtml() throws SocketException, IOException {

		DirResource resource = new DirResource();
		String userTest = "test", mdpTest = "testmdp";
		byte[] authorizationHeader;
		String htmlRoot;

		authorizationHeader = Base64.encode((userTest + ":" + mdpTest)
				.getBytes());
		htmlRoot = resource.dirHtml(userTest, "Basic "
				+ new String(authorizationHeader));

		assertEquals(TEST_ROOT_CONTENT_HTML, htmlRoot);
	}

	@Test
	public void testDirJSon() throws SocketException, IOException {

		DirResource resource = new DirResource();
		String userTest = "test", mdpTest = "testmdp";
		byte[] authorizationHeader;
		String jsonRoot;

		authorizationHeader = Base64.encode((userTest + ":" + mdpTest)
				.getBytes());
		jsonRoot = resource.dirJSon(userTest, "Basic "
				+ new String(authorizationHeader));

		assertEquals(TEST_ROOT_CONTENT_JSON, jsonRoot);
	}

	@Test(expected = UnauthorizedException.class)
	public void testDirHtmlBadAuthente() throws SocketException, IOException {

		DirResource resource = new DirResource();
		String userTest = "test", mdpTest = "CAFEBABE";
		byte[] authorizationHeader;

		authorizationHeader = Base64.encode((userTest + ":" + mdpTest)
				.getBytes());
		resource.dirHtml(userTest, "Basic " + new String(authorizationHeader));
	}

	@Test(expected = UnauthorizedException.class)
	public void testDirJSonBadAuthente() throws SocketException, IOException {

		DirResource resource = new DirResource();
		String userTest = "test", mdpTest = "DEADB33F";
		byte[] authorizationHeader;

		authorizationHeader = Base64.encode((userTest + ":" + mdpTest)
				.getBytes());
		resource.dirJSon(userTest, "Basic " + new String(authorizationHeader));
	}

}
