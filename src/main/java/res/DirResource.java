package res;

import html.HtmlRestListDocument;

import java.io.IOException;
import java.net.SocketException;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import json.JsonRestList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import plateform.exceptions.BadAuthorizationHeaderException;
import plateform.exceptions.RestNotFoundException;
import plateform.exceptions.UnauthorizedException;
import user.HTTPAuthenticator;
import user.PathManager;
import utils.FtpUtils;

/**
 * Représente une ressource REST de type répertoire. Précisément, une instance
 * de cette classe permet de récupérer le contenu d'un répertoire présent sur le
 * serveur FTP de l'application. Les méthodes de cette classe se réfèrent à la
 * classe PathManager pour obtenir les chemins de répertoire courant de
 * l'utilisateur.<br/>
 * 
 * Url: http://localhost:8080/rest/api/toto/dir
 */
@Path("/{username}/dir")
public class DirResource {

	/**
	 * Nom de la ressource, correspond au nom présent dans l'URL.
	 */
	public final static String RES_NAME = "dir";

	/**
	 * Liste le contenu du répertoire courant, provenant du serveur FTP, au
	 * format <strong>HTML</strong>. Le répertoire courant est enregistré dans
	 * la classe <tt>PathManager</tt>.<br/>
	 * Chaque élément du répertoire listé est un lien:
	 * <ul>
	 * <li>Si l'élément est un autre répertoire, alors un "cd" est effectué via
	 * une autre instance de cette classe.</li>
	 * <li>Sinon, si l'élément est un fichier, un téléchargement est possible
	 * via la classe <tt>FileResoure</tt>.</li>
	 * </ul>
	 * 
	 * @param username
	 *            Le nom de l'utilisateur courant conservé dans l'URL. Nom de
	 *            l'utilisateur dans l'URL.
	 * @param authorization l'en-tête d'authentification HTTP de la requête
	 * @return Le contenu du répertoire courant au format HTML.
	 * @throws IOException
	 */
	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String dirHtml(@PathParam("username") String username, 
						  @HeaderParam("authorization") String authorization)
			throws IOException {

		String path;
		final FTPClient client = new FTPClient();
		HTTPAuthenticator authenticator;
		
		try {
			authenticator = new HTTPAuthenticator(authorization);
		} catch (BadAuthorizationHeaderException e) {
			throw new UnauthorizedException(username);
		}
		
		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);

		// LOG
		if (!client.login(username, authenticator.getPassword()))
			throw new UnauthorizedException(username);

		PathManager pathManager = PathManager.getInstance();

		// CHANGE DIRECTORY
		path = pathManager.getPath(username);
		if (!client.changeWorkingDirectory(path)) {
			path = client.printWorkingDirectory();
			pathManager.putPath(username, path);
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
	 * Retourne le contenu du répertoire courant, provenant du serveur FTP, au
	 * format <strong>JSON</strong>. Le répertoire courant est enregistré dans
	 * la classe <tt>PathManager</tt>. Il est possible de récupérer ce contenu
	 * avec la commande <tt>curl</tt>.<br/>
	 * <strong>Exemple:</strong><br/>
	 * <code>
	 * curl -v -X GET http://localhost:8080/rest/api/toto/dir/json -H "Content-type: application/json"
	 * </code>
	 * 
	 * @param username
	 *            Le nom de l'utilisateur courant conservé dans l'URL.
	 * @param authorization l'en-tête d'authentification HTTP de la requête
	 * @return Le contenu du répertoire courant au format JSON.
	 * @throws IOException
	 */
	@GET
	@Path("/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public String dirJSon(@PathParam("username") String username, 
			  			  @HeaderParam("authorization") String authorization)
			throws IOException {

		/*
		 * { "files":[ {"name":"src","type":"dir"}, {"name":"bin","type":"dir"},
		 * {"name":"README.md","type":"file"} ] }
		 */

		String path;
		final FTPClient client = new FTPClient();

		HTTPAuthenticator authenticator;
		
		try {
			authenticator = new HTTPAuthenticator(authorization);
		} catch (BadAuthorizationHeaderException e) {
			throw new UnauthorizedException(username);
		}
		
		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);

		// LOG
		if (!client.login(username, authenticator.getPassword()))
			throw new UnauthorizedException(username);

		PathManager pathManager = PathManager.getInstance();

		// CHANGE DIRECTORY
		path = pathManager.getPath(username);
		
		if (!client.changeWorkingDirectory(path)) {
			path = client.printWorkingDirectory();
			pathManager.putPath(username, path);
		}
		
		// LIST
		final FTPFile[] files = client.listFiles();
		final JsonRestList json = new JsonRestList(username, path, files);

		// QUIT
		client.logout();
		client.disconnect();

		return json.toString() + "\n";
	}

	/**
	 * Met à jour le chemin dans la <tt>PathManager</tt> pour l'utilisateur
	 * courant. Le nom du répertoire cible <tt>dirName</tt> passés en argument
	 * est en relatif. Il correspond à un répertoire du chemin avec l'appel.
	 * 
	 * @param uriInfo
	 *            Information sur l'URI.
	 * @param dirName
	 *            Nom du répertoire cible.
	 * @param username
	 * @param authorization l'en-tête d'authentification HTTP de la requête
	 *            Le nom de l'utilisateur courant conservé dans l'URL.
	 * @return Une réponse pour l'application. Cette réponse à pour effet une
	 *         redirection vers l'affichage du contenu du nouveau chemin en
	 *         HTML.
	 * @throws IOException
	 * @throws SocketException
	 */
	@GET
	@Path("/{dirname}")
	public Response changeDir(@Context UriInfo uriInfo,
			@PathParam("dirname") String dirName,
			@PathParam("username") String username, 
			@HeaderParam("authorization") String authorization) throws SocketException,
			IOException {

		Response res;
		URI uri;
		String path;

		FTPClient client = new FTPClient();

		HTTPAuthenticator authenticator;
		
		try {
			authenticator = new HTTPAuthenticator(authorization);
		} catch (BadAuthorizationHeaderException e) {
			throw new UnauthorizedException(username);
		}
		
		// CONNECT
		client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);

		// LOG
		if (!client.login(username, authenticator.getPassword()))
			throw new UnauthorizedException(username);

		PathManager pathManager = PathManager.getInstance();

		// CHANGE DIRECTORY
		path = pathManager.getPath(username);
		
		if (!client.changeWorkingDirectory(path)) {
			path = client.printWorkingDirectory();
			pathManager.putPath(username, path);
		}
		
		if (!client.changeWorkingDirectory(dirName)) {
			client.logout();
			client.disconnect();
			throw new RestNotFoundException();
		}
		
		pathManager.putPath(username, client.printWorkingDirectory());

		// QUIT
		client.logout();
		client.disconnect();

		uri = uriInfo.getBaseUriBuilder().path(username + "/" + RES_NAME)
				.build();
		res = Response.seeOther(uri).build();
		return res;
	}

	/**
	 * Met à jour le chemin dans la <tt>PathManager</tt> pour l'utilisateur
	 * courant vers le répertoire parent, si autorisé par le serveur.
	 * 
	 * @param uriInfo
	 *            Information sur l'URI.
	 * @param username
	 *            Le nom de l'utilisateur courant conservé dans l'URL.
	 * @param authorization l'en-tête d'authentification HTTP de la requête
	 * @return Une réponse pour l'application. Cette réponse à pour effet, une
	 *         redirection vers l'affichage du contenu du nouveau chemin en
	 *         HTML.
	 * @throws SocketException
	 * @throws IOException
	 */
	@GET
	@Path("/cdup")
	public Response changeToParentDir(@Context UriInfo uriInfo,
			@PathParam("username") String username, 
			@HeaderParam("authorization") String authorization) throws SocketException,
			IOException {

		return this.changeDir(uriInfo, "..", username, authorization);
	}

}
