package html;

import uk.co.wireweb.web.html.html5.tag.Head;
import uk.co.wireweb.web.html.html5.tag.Meta;
import uk.co.wireweb.web.html.html5.tag.Title;

public class HtmlHead extends HtmlContainer {

	private String pageTitle;
	private HtmlCharsetEnum charset;
	
	public HtmlHead(final String pageTitle, final HtmlCharsetEnum charset) {
		this.pageTitle = pageTitle;
		this.charset = charset;
	}

	@Override
	protected void generate() {
        final Head head = new Head();
        final Meta charset = new Meta();
        final Title title = new Title();
        
        title.body(pageTitle);
        
        head.child(new Title().body("Helloworld Resource"));

        setHtmlContent(head.toString());
	}	
	
}
