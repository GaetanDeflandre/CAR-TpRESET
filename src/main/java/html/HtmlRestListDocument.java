package html;

import org.apache.commons.net.ftp.FTPFile;

/**
 * Page HTML qui représente la liste de fichier sur le serveur FTP.
 */
public class HtmlRestListDocument extends HtmlDocument {

	/**
	 * Construit la page HTML correspondant à la list de fichiers du serveur
	 * FTP, pour ce project.
	 * 
	 * @param username
	 *            Le nom d'utilisateur courant.
	 * @param path
	 *            Le chemin du dossier couramment visité.
	 * @param files
	 *            Les fichier du dossier à afficher sur la page.
	 */
	public HtmlRestListDocument(final String username, final String path,
			final FTPFile[] files) {

		super(new RestUTF8Head("Liste des fichiers"), new RestListBody(username, path,
				files));
	}

}
