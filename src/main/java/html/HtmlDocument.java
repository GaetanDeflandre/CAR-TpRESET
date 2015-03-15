package html;

import uk.co.wireweb.web.html.DocType;
import uk.co.wireweb.web.html.html5.tag.Body;
import uk.co.wireweb.web.html.html5.tag.Head;
import uk.co.wireweb.web.html.html5.tag.Html;

/**
 * Simple document HTML avec un head et un body, le tout dans une balise html.
 */
public class HtmlDocument extends HtmlContainer {

	private Head head;
	private Body body;

	public HtmlDocument(final Head head, final Body body) {
		this.head = head;
		this.body = body;
		generate();
	}

	@Override
	protected void generate() {
		final Html html = new Html();

		html.body(DocType.HTML5.getDocType());
		html.child(head);
		html.child(body);

		setHtmlContent(html.toString());

	}

}
