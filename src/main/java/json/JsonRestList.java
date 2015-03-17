package json;

import org.apache.commons.net.ftp.FTPFile;

import uk.co.wireweb.web.javascript.json.JsonArray;
import uk.co.wireweb.web.javascript.json.JsonKeyValuePair;
import uk.co.wireweb.web.javascript.json.JsonObject;

/**
 * Document JSON qui représente le contenu d'un répertoire provenant du serveur
 * FTP.
 */
public class JsonRestList extends JsonContainer {

	// ATTRIBUTES //

	/**
	 * Le nom de l'utilisateur connecté sur l'application.
	 */
	private String username;

	/**
	 * Le chemin courant visité par l'utilisateur.
	 */
	private String path;

	/**
	 * Les fichiers du répertoire <tt>path</tt>.
	 */
	private FTPFile[] files;

	// CONSTRUCTOR //

	/**
	 * Construit et génère le contenu JSON de la classe.
	 * 
	 * @param username
	 *            Le nom de l'utilisateur connecté sur l'application.
	 * @param path
	 *            Le chemin courant visité par l'utilisateur.
	 * @param files
	 *            Les fichiers du répertoire <tt>path</tt>.
	 */
	public JsonRestList(final String username, final String path,
			final FTPFile[] files) {

		this.username = username;
		this.path = path;
		this.files = files;

		generate();

	}

	// METHOD //

	@Override
	protected void generate() {
		JsonObject root = new JsonObject("root");
		JsonArray jsonFiles = new JsonArray("files");

		for (FTPFile file : files) {
			jsonFiles.member(new JsonKeyValuePair<String>("value", file
					.getRawListing()));
		}

		root.member(new JsonKeyValuePair<String>("path", path));
		root.member(new JsonKeyValuePair<String>("username", username));
		root.array(jsonFiles);

		setJSonContent(root.toString());
	}
}
