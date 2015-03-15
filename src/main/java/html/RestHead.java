package html;

import uk.co.wireweb.web.html.html5.tag.Head;
import uk.co.wireweb.web.html.html5.tag.Link;
import uk.co.wireweb.web.html.html5.tag.Meta;
import uk.co.wireweb.web.html.html5.tag.Title;

/**
 * Classe amélioré de la classe Head. Cette classe represente la balise head
 * d'HTML pour un document HTML du projet. Cette classe est formatté pour avoir:
 * <ul>
 * <li>un encodage (charset)</li>
 * <li>un titre</li>
 * <li>un style css bootstrap</li>
 * </ul>
 */
public class RestHead extends Head {

	// ATTRIBUTES //

	/**
	 * Titre de la page.
	 */
	private String pageTitle;

	/**
	 * Type d'encodage.
	 */
	private CharsetEnum charset;

	/**
	 * Url du css de bootstrap
	 */
	private final static String BOOTSTRAP_URL = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css";

	// CONSTRUCTOR //

	/**
	 * Construit le head du HTML pour un document HTML du projet REST.
	 * 
	 * @param pageTitle
	 *            Le titre de la page.
	 * @param charset
	 *            Le type d'encodage de la page.
	 */
	public RestHead(final String pageTitle, final CharsetEnum charset) {
		this.pageTitle = pageTitle;
		this.charset = charset;
		createHead();
	}

	// METHODS //

	/**
	 * Crée la balise head en fonction des
	 */
	private void createHead() {

		final Meta meta = new Meta();
		final Title title = new Title();
		final Link link = new Link();

		meta.charset(charset.toString());
		title.body(pageTitle);
		link.rel("stylesheet");
		link.href(BOOTSTRAP_URL);

		child(meta);
		child(title);
		child(link);
	}

}
