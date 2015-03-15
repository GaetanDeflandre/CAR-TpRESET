package html;

import org.apache.commons.net.ftp.FTPFile;

import plateform.config.AppConfig;
import res.DirResource;
import res.FileResource;
import uk.co.wireweb.web.html.html5.tag.A;
import uk.co.wireweb.web.html.html5.tag.Body;
import uk.co.wireweb.web.html.html5.tag.H1;
import uk.co.wireweb.web.html.html5.tag.Hr;
import uk.co.wireweb.web.html.html5.tag.Li;
import uk.co.wireweb.web.html.html5.tag.Strong;
import uk.co.wireweb.web.html.html5.tag.Ul;

/**
 * Surcouche de la basile <tt>Body</tt> pour contenir la liste des fichiers FTP.
 */
public class RestListBody extends Body {

	// ATTRIBUTES //

	/**
	 * Nom de l'utilisateur utiliser pour recréer les URLs.
	 */
	private String username;

	/**
	 * Chemin courant utiliser pour l'affichage.
	 */
	private String path;

	/**
	 * Les fichiers à afficher.
	 */
	private FTPFile[] files;

	/**
	 * Affichage HTML utiliser pour le dossier parent.
	 */
	private String UPDIR_MSG = "Dossier parent &#8682;";

	// CONSTRUCTOR //

	/**
	 * Construit le body HTML qui contient le chemin <tt>path</tt>, la liste des
	 * fichier <tt>files</tt> avec les liens utilisant <tt>username</tt>.
	 * 
	 * @param username
	 *            Nom de l'utilisateur utiliser pour recréer les URLs.
	 * @param path
	 *            Chemin courant utiliser pour l'affichage.
	 * @param files
	 *            Les fichiers à afficher
	 */
	public RestListBody(final String username, final String path,
			final FTPFile[] files) {
		this.username = username;
		this.path = path;
		this.files = files;
		createBody();
	}
	
	// METHOD //
	
	/**
	 * Méthode de création du contenu HTML.
	 */
	private void createBody() {

		final H1 title = new H1();
		final Ul list = new Ul();
		final Li parentItem = new Li();
		final A parentAnchor = new A();
		final Strong parentContent = new Strong();

		title.body("Fichier(s) de " + path);

		// cdup link
		parentAnchor.href(AppConfig.RES_ABS_PATH + username + "/"
				+ DirResource.RES_ROOT + "/cdup");
		parentContent.body(UPDIR_MSG);
		parentAnchor.body(parentContent.toString());
		parentItem.body(parentAnchor.toString());

		list.child(parentItem);

		// loop on the files' list
		for (final FTPFile file : files) {
			final Li item = new Li();
			final A anchor = new A();

			if (file.isDirectory()) { // Directory case

				anchor.href(AppConfig.RES_ABS_PATH + username + "/"
						+ DirResource.RES_ROOT + "/" + file.getName());
				anchor.body(file.getName());
				item.body(anchor.toString());

			} else if (file.isFile()) { // File case

				anchor.href(AppConfig.RES_ABS_PATH + username + "/"
						+ FileResource.RES_ROOT + "/" + file.getName());
				anchor.body(file.getName());
				item.body(anchor.toString());

			} else { // Other case
				item.body(file.getName());
			}

			list.child(item);

		}

		child(title);
		child(new Hr());
		child(list);
	}

}
