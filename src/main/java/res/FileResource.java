package res;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import plateform.exceptions.BadAuthorizationHeaderException;
import plateform.exceptions.RestServerErrorException;
import plateform.exceptions.UnauthorizedException;
import user.HTTPAuthenticator;
import user.PathManager;
import utils.FtpUtils;

/**
 * Représente une ressource REST de type fichier. Précisément, 
 * une instance de cette classe représente un fichier sur le 
 * serveur FTP de l'application. Les méthodes de cette classe 
 * se réfèrent à la classe PathManager pour obtenir les chemins
 * de répertoire courant de l'utilisateur. Les noms de fichier
 * passés en argument sont donc relatifs au répertoire courant 
 * de l'utilisateur.
 * 
 * @author Samuel Grandsir
 *
 */
@Path("/{username}/file/{filename}")
public class FileResource {

	/**
	 * Nom de la ressource, correspond au nom présent dans l'URL.
	 */
	public final static String RES_NAME = "file";

	/**
	 * Permet au client de télécharger un fichier. La méthode communique avec le
	 * serveur FTP pour récupérer le fichier, puis renvoie une instance
	 * d'InputStream pour construire la réponse HTTP.
	 * 
	 * @param uriInfo
	 * @param filename
	 *            le nom du fichier à récupérer sur le serveur FTP.
	 * @param username
	 * @param authorization l'en-tête d'authentification HTTP de la requête
	 * @return une instance d'InputStream permettant de lire le fichier.
	 * @throws IOException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public InputStream getFile(@PathParam("filename") String filename,
			@PathParam("username") String username,
			@HeaderParam("authorization") String authorization) throws IOException {
		FTPClient client = new FTPClient();
		String path;

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
		
		client.setFileType(FTP.BINARY_FILE_TYPE);
		InputStream fileInput = client.retrieveFileStream(filename);

		// QUIT
		client.logout();
		client.disconnect();

		// ERROR CASE
		if (fileInput == null) {
			throw new RestServerErrorException(
					"&eacute;chec obtenrion de fichier.");
		}

		return fileInput;
	}

	/**
	 * Permet au client de supprimer un fichier sur le serveur FTP.
	 * 
	 * @param filename
	 *            le nom du fichier à supprimer sur le serveur FTP.
	 * @param username
	 * @param authorization l'en-tête d'authentification HTTP de la requête
	 * @return une réponse HTTP positive en cas de succès, une réponse négative
	 *         sinon.
	 * @throws IOException
	 */
	@DELETE
	public Response deleteFile(@PathParam("filename") String filename,
			@PathParam("username") String username,
			@HeaderParam("authorization") String authorization) throws IOException {

		FTPClient client = new FTPClient();
		String path;
		boolean deletionSuccessful;

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
		
		deletionSuccessful = client.deleteFile(filename);

		// QUIT
		client.logout();
		client.disconnect();

		// ERROR CASE
		if (!deletionSuccessful) {
			throw new RestServerErrorException(
					"&eacute;chec suppression de fichier.");
		}

		return Response.ok().build();
	}

	/**
	 * Permet au client de transférer un fichier sur le serveur FTP avec la
	 * méthode PUT. Le nom du fichier côté serveur est celui donné dans l'URI.
	 * Si un fichier porte déjà ce nom côté serveur, il sera écrasé.
	 * 
	 * @param filename
	 *            le nom du fichier sur le serveur
	 * @param username
	 * @param inputStream
	 *            l'instance d'InputStream permettant de lire le contenu du
	 *            fichier à transférer.
	 * @param authorization l'en-tête d'authentification HTTP de la requête
	 * @return une réponse HTTP positive en cas de succès, une réponse négative
	 *         sinon.
	 * @throws IOException
	 */
	@PUT
	@Consumes({ MediaType.APPLICATION_OCTET_STREAM })
	public Response storeFile(@PathParam("filename") String filename,
			@PathParam("username") String username, InputStream inputStream,
			@HeaderParam("authorization") String authorization)
			throws IOException {

		FTPClient client = new FTPClient();
		String path;
		boolean storeSuccessful = false;

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
		
		client.setFileType(FTP.BINARY_FILE_TYPE);
		storeSuccessful = client.storeFile(filename, inputStream);

		// QUIT
		client.logout();
		client.disconnect();

		// ERROR CASE
		if (!storeSuccessful) {
			throw new RestServerErrorException(
					"&eacute;chec de l'upload de fichier.");
		}

		return Response.ok().build();
	}
}
