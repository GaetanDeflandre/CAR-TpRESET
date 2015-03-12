package res;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTPClient;

@Path("/file/{filename}")
public class FileResource {
	public final static String ADDRESS = "127.0.0.1";
	public final static int PORT = 2121;
	public final static String ENDL = "\r\n";
	
	public final static String LOGIN = "toto";
	public final static String PASS = "mdp";
	
	@GET
	@Produces("application/octet-stream")
	public Response getFile(@PathParam("filename") String filename) throws IOException {
		FTPClient ftp = new FTPClient();

		// CONNECT
		try {
			ftp.connect(ADDRESS, PORT);
		} catch (SocketException e) {
			return Response.serverError().build();
		} catch (IOException e) {
			return Response.serverError().build();
		}
		
		// LOG
		try {
			ftp.login(LOGIN, PASS);
		} catch (IOException e) {
			return Response.serverError().build();
		}
		
		InputStream fileInput = ftp.retrieveFileStream(filename);
		
		// QUIT
		try {
			ftp.quit();
			ftp.disconnect();
		} catch (IOException e) {
			return Response.serverError().build();
		}
		
		if (fileInput == null)
			return Response.serverError().build();
		else
			return Response.ok(fileInput, MediaType.APPLICATION_OCTET_STREAM).build();
	}
	
}
