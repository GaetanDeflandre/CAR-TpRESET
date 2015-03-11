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

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 * http://localhost:8080/rest/api/cmd
 * 
 * @author Lionel Seinturier <Lionel.Seinturier@univ-lille1.fr>
 */
@Path("/dir")
public class DirResource {

	public final static String ADDRESS = "127.0.0.1";
	public final static int PORT = 2121;
	public final static String ENDL = "\r\n";

	public final static String LOGIN = "toto";
	public final static String PASS = "mdp";
	public final static String RES_ABS_PATH = "http://localhost:8080/rest/api/dir";
	public final static String RES_ROOT = "dir";

	@GET
	@Produces("text/html")
	public String directories() {
		String html;
		FTPClient client = new FTPClient();

		html = "<h1>Ressources repertoires</h1>" + ENDL;

		// CONNECT
		try {
			client.connect(ADDRESS, PORT);
		} catch (SocketException e) {
			html += e.getMessage() + ENDL;
			return html;
		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}

		// LOG
		try {
			client.login(LOGIN, PASS);
		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}

		// LIST
		try {
			FTPFile[] files = client.listFiles();

			html += "<ul>" + ENDL;
			for (FTPFile file : files) {
				if (file.isDirectory()) {
					html += "<li><a href=" + RES_ABS_PATH + "/"
							+ file.getName() + ">";
					html += file.getName();
					html += "</a></li>" + ENDL;

				} else if (file.isFile()) {
					html += "<li><a href=#>";
					html += file.getName();
					html += "</a></li>" + ENDL;

				} else {
					html += "<li>" + file.getName() + "</li>" + ENDL;
				}
			}
			html += "</ul>" + ENDL;

		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}

		// QUIT
		try {
			client.quit();
			client.disconnect();
		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}

		return html;
	}

	/*
	 * /!\ README 
	 * La methode change dir est fonctionelle: elle fait le CWD coté
	 * serveur mais comme on quit le serveur, le chemin n'est pas 
	 * retenu coté serveur
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
			client.connect(ADDRESS, PORT);
		} catch (SocketException e) {
			res = Response.serverError().build();
			return res;
		} catch (IOException e) {
			res = Response.serverError().build();
			return res;
		}

		// LOG
		try {
			client.login(LOGIN, PASS);
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
