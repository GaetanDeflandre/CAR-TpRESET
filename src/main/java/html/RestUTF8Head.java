package html;

/**
 * Surcouche de la classe <tt>RestHead</tt> qui fixe l'UTF 8.
 */
public class RestUTF8Head extends RestHead {

	/**
	 * Construit un head UTF8 pour les documents du projet.
	 * 
	 * @param pageTitle
	 *            Le titre de la page.
	 */
	public RestUTF8Head(final String pageTitle) {
		super(pageTitle, CharsetEnum.UTF8);
	}
}
