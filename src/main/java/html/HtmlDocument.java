package html;

import uk.co.wireweb.web.html.DocType;
import uk.co.wireweb.web.html.html5.tag.Body;
import uk.co.wireweb.web.html.html5.tag.Head;
import uk.co.wireweb.web.html.html5.tag.Html;

/**
 * Simple page HTML avec un head et un body, le tout dans une balise HTML.
 */
public class HtmlDocument extends HtmlContainer {

	// ATTRIBUTES //

	/**
	 * La balise head bas niveau.
	 */
	private Head head;

	/**
	 * La balise body bas niveau.
	 */
	private Body body;

	// CONSTRUCTOR //

	/**
	 * Construit la page HTML avec un head <tt>head</tt> et un body
	 * <tt>body</tt>.
	 * 
	 * @param head
	 *            La balise head de la page.
	 * @param body
	 *            La balise body de la page.
	 */
	public HtmlDocument(final Head head, final Body body) {
		this.head = head;
		this.body = body;
		generate();
	}

	// METHOD //

	@Override
	protected void generate() {
		final Html html = new Html();

		html.body(DocType.HTML5.getDocType());
		html.child(head);
		html.child(body);

		setHtmlContent(html.toString());

	}

}
