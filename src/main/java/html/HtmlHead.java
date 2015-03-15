package html;

import uk.co.wireweb.web.html.html5.tag.Head;

public class HtmlHead extends HtmlGeneratorContract {

	private String pageTitle;
	private HtmlCharsetEnum charset;
	private Head head;
	
	public HtmlHead(final String pageTitle, final HtmlCharsetEnum charset) {
		this.pageTitle = pageTitle;
		this.charset = charset;
	}

	@Override
	protected void generate() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
