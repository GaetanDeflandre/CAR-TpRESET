package res;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import user.UserManager;
import utils.FtpUtils;

@Path("/{username}/file/{filename}")
public class FileResource {
	
	@GET
	@Produces("application/octet-stream")
	public Response getFile(@Context UriInfo uriInfo, @PathParam("filename") String filename,
							@PathParam("username") String username) throws IOException {
		FTPClient client = new FTPClient();
		String path;
		
		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);
		
		// LOG
		client.login(FtpUtils.LOGIN, FtpUtils.PASS);
		
		// CHANGE DIR
		UserManager userManager = UserManager.getInstance();
		path = userManager.getPath(username);
		client.changeWorkingDirectory(path);
		
		client.setFileType(FTP.BINARY_FILE_TYPE);
		InputStream fileInput = client.retrieveFileStream(filename);
		
		// QUIT
		client.logout();
		client.disconnect();
		
		if (fileInput == null)
			return Response.serverError().build();
		else
			return Response.ok(fileInput, MediaType.APPLICATION_OCTET_STREAM).build();
	}
	
	@DELETE
	public Response deleteFile(@PathParam("filename") String filename,
							   @PathParam("username") String username) throws SocketException, IOException {
		
		FTPClient client = new FTPClient();
		String path;
		boolean deletionSuccessful;

		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);
		
		// LOG
		client.login(username, FtpUtils.PASS);
		
		// CHANGE DIR
		UserManager userManager = UserManager.getInstance();
		path = userManager.getPath(username);
		client.changeWorkingDirectory(path);
		
		deletionSuccessful = client.deleteFile(filename);
		
		// QUIT
		client.logout();
		client.disconnect();
		
		if (deletionSuccessful)
			return Response.ok().build();
		else
			return Response.serverError().build();
	}
	
	@PUT
	@Consumes("application/octet-stream")
	public Response storeFile(@PathParam("filename") String filename,
							@PathParam("username") String username,
							InputStream inputStream
							) throws SocketException, IOException {
		
		FTPClient client = new FTPClient();
		String path;
		boolean storeSuccessful=false;

		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);
		
		// LOG
		client.login(username, FtpUtils.PASS);
		
		// CHANGE DIR
		UserManager userManager = UserManager.getInstance();
		path = userManager.getPath(username);
		client.changeWorkingDirectory(path);
		
		storeSuccessful = client.storeFile(filename, inputStream);
		
		// QUIT
		client.logout();
		client.disconnect();
		
		if (storeSuccessful)
			return Response.ok().build();
		else
			return Response.serverError().build();
	}
}
