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

import plateform.config.AppConfig;
import utils.FtpUtils;
import utils.HtmlUtils;

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 * http://localhost:8080/rest/api/dir
 */
@Path("/dir")
public class DirResource {

	public final static String RES_ROOT = "dir";

	@GET
	@Produces("text/html")
	public String directories() throws SocketException, IOException {
		FTPClient client = new FTPClient();

		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);

		// LOG
		client.login(FtpUtils.LOGIN, FtpUtils.PASS);

		// LIST
		FTPFile[] files = client.listFiles();

		HtmlRestListDocument html = new HtmlRestListDocument(files);
		
		// QUIT
		client.quit();
		client.disconnect();

		return html.toString();
	}

	/*
	 * /!\ README La methode change dir est fonctionelle: elle fait le CWD coté
	 * serveur mais comme on quit le serveur, le chemin n'est pas retenu coté
	 * serveur
	 */

	/**
	 * Change directory
	 * 
	 * @param uriInfo
	 * @param dirName
	 * @return
	 */
	@GET
	@Path("/{dirname}")
	public Response changeDir(@Context UriInfo uriInfo,
			@PathParam("dirname") String dirName) {

		Response res;
		URI uri;

		FTPClient client = new FTPClient();

		// CONNECT
		try {
			client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);
		} catch (SocketException e) {
			res = Response.serverError().build();
			return res;
		} catch (IOException e) {
			res = Response.serverError().build();
			return res;
		}

		// LOG
		try {
			client.login(FtpUtils.LOGIN, FtpUtils.PASS);
		} catch (IOException e) {
			res = Response.serverError().build();
			return res;
		}

		// CHANGE DIR
		try {
			client.changeWorkingDirectory(dirName);
		} catch (IOException e) {
			res = Response.serverError().build();
			return res;
		}

		// QUIT
		try {
			client.quit();
			client.disconnect();
		} catch (IOException e) {
			res = Response.serverError().build();
			return res;
		}

		uri = uriInfo.getBaseUriBuilder().path(RES_ROOT).build();
		res = Response.seeOther(uri).build();
		return res;
	}

	@GET
	@Path("{var}/b")
	public String getStuff(@PathParam("var") String stuff) {
		return "Stuff: " + stuff;
	}

}
