package html;

import uk.co.wireweb.web.html.html5.tag.Body;
import uk.co.wireweb.web.html.html5.tag.Head;
import uk.co.wireweb.web.html.html5.tag.Html;

public class HtmlDocument extends HtmlContainer{
	
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
				
		html.child(head);
		html.child(body);
		
		setHtmlContent(html.toString());
		
	}
	
	public static void main(String[] args) {
		Head test = new RestHead("toto", CharsetEnum.UTF8);
		Body body = new Body();
		
		HtmlDocument doc = new HtmlDocument(test, body);
		
		System.out.println(doc.toString());
	}

}
