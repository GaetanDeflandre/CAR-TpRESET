package html;

import org.apache.commons.net.ftp.FTPFile;

import plateform.config.AppConfig;
import res.DirResource;
import uk.co.wireweb.web.html.html5.tag.A;
import uk.co.wireweb.web.html.html5.tag.Body;
import uk.co.wireweb.web.html.html5.tag.H1;
import uk.co.wireweb.web.html.html5.tag.Hr;
import uk.co.wireweb.web.html.html5.tag.Li;
import uk.co.wireweb.web.html.html5.tag.Ul;

public class RestListBody extends Body {

	private FTPFile[] files;

	public RestListBody(final FTPFile[] files) {
		this.files = files;
		createBody();
	}

	private void createBody() {

		final H1 title = new H1();
		final Ul list = new Ul();

		title.body("Files list");
		
		for (final FTPFile file : files) {
			final A anchor = new A();
			final Li item = new Li();

			if (file.isDirectory()) {

				anchor.href(AppConfig.RES_ABS_PATH + DirResource.RES_ROOT + "/"
						+ file.getName());
				anchor.body(file.getName());
				item.body(anchor.toString());

			} else if (file.isFile()) {
				
				anchor.href(AppConfig.RES_ABS_PATH + "file/" + file.getName());
				anchor.body(file.getName());
				item.body(anchor.toString());

			} else {
				item.body(file.getName());
			}
			
			list.child(item);
			
		}
		
		child(title);
		child(new Hr());
		child(list);
	}

}
