package json;

/**
 * Classe abstraite qui permet d'uniformiser les classes qui contienne du JSON.
 */
public abstract class JsonContainer {

	// ATTRIBUTE //

	/**
	 * Le contenu JSON.
	 */
	private String jsonContent;

	// METHODS //

	/**
	 * Génére le contenu JSON en fonction des caractéristique de la classe.
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
