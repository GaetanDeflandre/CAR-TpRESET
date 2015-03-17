package html;

/**
 * Enumérateur des type d'encodage HTML.
 */
public enum CharsetEnum {

	// ENUM //

	/**
	 * Type d'encodage UTF8
	 */
	UTF8("UTF-8"),
	/**
	 * Type d'encodage Latin-1
	 */
	ISO88591("ISO-8859-1");

	// ATTRIBUTE //

	/**
	 * Contient la chaine de caractères conrespondant au type d'encodage pour
	 * HTML. Ne contient pas de syntaxe HTML.
	 */
	private String charset;

	// CONSTRUCTOR //

	/**
	 * Construit la chaine pour l'encodage.
	 * 
	 * @param charset
	 *            Le text conspondant au type d'encodage en HTML.
	 */
	private CharsetEnum(final String charset) {
		this.charset = charset;
	}

	// METHOD //

	/**
	 * @return le type d'encodage sous forme d'une chaine de caractère
	 *         correspondant à la syntaxe HTML. Ne contient aucune balise, ni
	 *         syntaxe d'attribut HTML.
	 */
	public String toString() {
		return charset;
	}

}
