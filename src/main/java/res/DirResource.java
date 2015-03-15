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
import javax.ws.rs.core.MediaType;
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

	/**
	 * 
	 * @param username
	 *            Nom de l'utilisateur dans l'URL.
	 * @return La liste des fichier du répertoire courant en format HTML.
	 * @throws IOException
	 */
	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String dirHtml(@PathParam("username") String username)
			throws IOException {

		String path;
		final FTPClient client = new FTPClient();

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

		HtmlRestListDocument html = new HtmlRestListDocument(username, path,
				files);

		// QUIT
		client.logout();
		client.disconnect();

		return html.toString();
	}

	/**
	 * curl -v -X GET http://localhost:8080/rest/api/toto/dir/json -H "Content-type: application/json"
	 * 
	 * @param username Nom de l'utilisateur dans l'URL.
	 * @return La liste des fichier du répertoire courant en format JSON.
	 * @throws IOException
	 */
	@GET
	@Path("/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public String dirJSon(@PathParam("username") String username)
			throws IOException {
		
		/*
		 * {
		 * 	 "files":[
		 * 		{"name":"src","type":"dir"},
		 *  	{"name":"bin","type":"dir"},
		 *  	{"name":"README.md","type":"file"}
		 *   ] 
		 * }
		 */
		
		String path;
		final FTPClient client = new FTPClient();

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
		
		
		// QUIT
		client.logout();
		client.disconnect();
		
		return "test\n";
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


}
