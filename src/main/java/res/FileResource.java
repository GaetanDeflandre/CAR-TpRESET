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

/**
 * Représente une ressource REST de type fichier. Précisément, 
 * une instance de cette classe représente un fichier sur le 
 * serveur FTP de l'application. Les méthodes de cette classe 
 * se réfèrent à la classe UserManager pour obtenir les chemins
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
	 * Permet au client de télécharger un fichier. La méthode
	 * communique avec le serveur FTP pour récupérer le fichier, 
	 * puis renvoie une instance d'InputStream pour construire 
	 * la réponse HTTP.
	 * 
	 * @param uriInfo
	 * @param filename le nom du fichier à récupérer sur le serveur FTP.
	 * @param username
	 * @return une instance d'InputStream permettant de lire le fichier.
	 * @throws IOException
	 */
	@GET
	@Produces("application/octet-stream")
	public InputStream getFile(@Context UriInfo uriInfo, @PathParam("filename") String filename,
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
		
		return fileInput;
	}
	
	/**
	 * Permet au client de supprimer un fichier sur le serveur FTP.
	 * 
	 * @param filename le nom du fichier à supprimer sur le serveur FTP.
	 * @param username
	 * @return une réponse HTTP positive en cas de succès, une réponse négative sinon.
	 * @throws IOException
	 */
	@DELETE
	public Response deleteFile(@PathParam("filename") String filename,
							   @PathParam("username") String username) throws IOException {
		
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
	
	/**
	 * Permet au client de transférer un fichier sur le serveur FTP
	 * avec la méthode PUT. Le nom du fichier côté serveur est celui 
	 * donné dans l'URI. Si un fichier porte déjà ce nom côté serveur, 
	 * il sera écrasé.
	 * 
	 * @param filename le nom du fichier sur le serveur
	 * @param username
	 * @param inputStream l'instance d'InputStream permettant de lire le 
	 * contenu du fichier à transférer.
	 * @return une réponse HTTP positive en cas de succès, une réponse négative sinon.
	 * @throws IOException
	 */
	@PUT
	@Consumes("application/octet-stream")
	public Response storeFile(@PathParam("filename") String filename,
							@PathParam("username") String username,
							InputStream inputStream
							) throws IOException {
		
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
		
		client.setFileType(FTP.BINARY_FILE_TYPE);
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
