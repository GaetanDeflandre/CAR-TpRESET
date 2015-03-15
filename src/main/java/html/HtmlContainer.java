package html;

/**
 * Classe abstraite qui permet d'uniformiser les classes qui contienne dde
 * l'HTML.
 */
public abstract class HtmlContainer {

	// ATTRIBUTE //

	/**
	 * Le contenu HTML.
	 */
	private String htmlContent;

	// METHODS //

	/**
	 * Génére le contenu HTML en fonction des caractéristique de la classe.
	 */
	protected abstract void generate();

	/**
	 * Fixe le contenu HTML avec le nouveau contenu <tt>htmlContent</tt>.
	 * 
	 * @param htmlContent Le nouveau contenu HTML.
	 */
	protected void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	/**
	 * @return Le contenu HTML.
	 */
	public String toString() {
		return htmlContent;
	}
}
