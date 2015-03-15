package html;

import org.apache.commons.net.ftp.FTPFile;

public class HtmlRestListDocument extends HtmlDocument {
	
	public HtmlRestListDocument(final String username, final FTPFile[] files) {
		super(new RestUTF8Head("List files"), new RestListBody(username, files));
	}
	
}
