package html;

import org.apache.commons.net.ftp.FTPFile;

public class HtmlRestListDocument extends HtmlDocument {
	
	public HtmlRestListDocument(final FTPFile[] files) {
		super(new RestUTF8Head("List files"), new RestListBody(files));
	}

	public static void main(String[] args) {
		System.out.println(new HtmlRestListDocument(null));
	}
	
}
