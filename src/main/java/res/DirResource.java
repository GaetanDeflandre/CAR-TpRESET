package res;

import html.HtmlRestListDocument;

import java.io.IOException;
import java.net.SocketException;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import user.UserManager;
import utils.FtpUtils;

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 * http://localhost:8080/rest/api/dir
 */
@Path("/{username}/dir")
public class DirResource {

	public final static String RES_ROOT = "dir";

	@GET
	@Produces("text/html")
	public String directories(@PathParam("username") String username)
			throws IOException {

		String path;
		FTPClient client = new FTPClient();

		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);

		// LOG
		client.login(FtpUtils.LOGIN, FtpUtils.PASS);

		UserManager userManager = UserManager.getInstance();

		// CHANGE DIRECTORY
		path = userManager.getPath(username);
		if (!client.changeWorkingDirectory(path)) {
			path = client.printWorkingDirectory();
			userManager.putPath(username, path);
		}

		// LIST
		FTPFile[] files = client.listFiles();

		HtmlRestListDocument html = new HtmlRestListDocument(username, files);

		// QUIT
		client.logout();
		client.disconnect();

		return html.toString();
	}

	/**
	 * Change directory
	 * 
	 * @param uriInfo
	 * @param dirName
	 * @return
	 * @throws IOException
	 * @throws SocketException
	 */
	@GET
	@Path("/{dirname}")
	public Response changeDir(@Context UriInfo uriInfo,
			@PathParam("dirname") String dirName,
			@PathParam("username") String username) throws SocketException,
			IOException {

		Response res;
		URI uri;

		FTPClient client = new FTPClient();

		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);

		// LOG
		client.login(FtpUtils.LOGIN, FtpUtils.PASS);

		// CHANGE DIR
		UserManager userManager = UserManager.getInstance();
		client.changeWorkingDirectory(userManager.getPath(username));
		client.changeWorkingDirectory(dirName);
		userManager.putPath(username, client.printWorkingDirectory());

		// QUIT
		client.logout();
		client.disconnect();

		uri = uriInfo.getBaseUriBuilder().path(username + "/" + RES_ROOT)
				.build();
		res = Response.seeOther(uri).build();
		return res;
	}

	@GET
	@Path("/cdup")
	public Response changeToParentDir(@Context UriInfo uriInfo,
			@PathParam("username") String username) throws SocketException,
			IOException {

		return this.changeDir(uriInfo, "..", username);
	}

	@GET
	@Path("{var}/b")
	public String getStuff(@PathParam("var") String stuff) {
		return "Stuff: " + stuff;
	}

}
