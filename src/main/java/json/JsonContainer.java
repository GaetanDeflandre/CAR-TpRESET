package json;

/**
 * Classe abstraite qui permet d'uniformiser les classes qui contiennent du
 * JSON.
 */
public abstract class JsonContainer {

	// ATTRIBUTE //

	/**
	 * Le contenu JSON.
	 */
	private String jsonContent;

	// METHODS //

	/**
	 * Génère le contenu JSON en fonction des caractéristiques de la classe.
	 */
	protected abstract void generate();

	/**
	 * Fixe le contenu JSON avec le nouveau contenu <tt>jsonContent</tt>.
	 * 
	 * @param htmlContent
	 *            Le nouveau contenu JSON.
	 */
	protected void setJSonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}

	/**
	 * @return Le contenu JSON.
	 */
	public String toString() {
		return jsonContent;
	}

}
