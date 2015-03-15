package res;

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
import user.UserManager;
import utils.FtpUtils;
import utils.HtmlUtils;

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
	public String directories(@PathParam("username") String username) throws IOException {
		String html, path;
		FTPClient client = new FTPClient();
		
		html = "<h1>Ressources repertoires</h1>" + HtmlUtils.ENDL;

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
		try {
			FTPFile[] files = client.listFiles();

			html += "<ul>" + HtmlUtils.ENDL;
			html += "<a href=" + AppConfig.RES_ABS_PATH + username 
					+ "/" + RES_ROOT+ "/cdup>";
			html += "<h4>Dossier parent &#8682;</h4>";
			html += "</a>" + HtmlUtils.ENDL;
			for (FTPFile file : files) {
				if (file.isDirectory()) {
					html += "<li><a href=" + AppConfig.RES_ABS_PATH + username 
							+ "/" + RES_ROOT+ "/" + file.getName() + ">";
					html += file.getName();
					html += "</a></li>" + HtmlUtils.ENDL;

				} else if (file.isFile()) {
					html += "<li><a href='http://localhost:8080/rest/api/" + username + "/file/"
							+ file.getName() + "'>";
					html += file.getName();
					html += "</a></li>" + HtmlUtils.ENDL;

				} else {
					html += "<li>" + file.getName() + "</li>" + HtmlUtils.ENDL;
				}
			}
			html += "</ul>" + HtmlUtils.ENDL;

		} catch (IOException e) {
			html += e.getMessage() + HtmlUtils.ENDL;
			return html;
		}

		// QUIT
		client.logout();
		client.disconnect();

		return html;
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
	 * @throws IOException 
	 * @throws SocketException 
	 */
	@GET
	@Path("/{dirname}")
	public Response changeDir(@Context UriInfo uriInfo,
			@PathParam("dirname") String dirName,
			@PathParam("username") String username) throws SocketException, IOException {

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

		uri = uriInfo.getBaseUriBuilder().path(username + "/" + RES_ROOT).build();
		res = Response.seeOther(uri).build();
		return res;
	}
	
	@GET
	@Path("/cdup")
	public Response changeToParentDir(@Context UriInfo uriInfo,
			@PathParam("username") String username) throws SocketException, IOException {

		return this.changeDir(uriInfo, "..", username);
	}

	@GET
	@Path("{var}/b")
	public String getStuff(@PathParam("var") String stuff) {
		return "Stuff: " + stuff;
	}

}
