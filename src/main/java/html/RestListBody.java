package html;

import org.apache.commons.net.ftp.FTPFile;

import plateform.config.AppConfig;
import res.DirResource;
import res.FileResource;
import uk.co.wireweb.web.html.html5.tag.A;
import uk.co.wireweb.web.html.html5.tag.Body;
import uk.co.wireweb.web.html.html5.tag.H1;
import uk.co.wireweb.web.html.html5.tag.Hr;
import uk.co.wireweb.web.html.html5.tag.Li;
import uk.co.wireweb.web.html.html5.tag.Strong;
import uk.co.wireweb.web.html.html5.tag.Ul;

public class RestListBody extends Body {

	private FTPFile[] files;
	private String username;

	private String UPDIR_MSG = "Dossier parent &#8682;";

	public RestListBody(final String username, final FTPFile[] files) {
		this.username = username;
		this.files = files;
		createBody();
	}

	private void createBody() {

		final H1 title = new H1();
		final Ul list = new Ul();
		final Li parentItem = new Li();
		final A parentAnchor = new A();
		final Strong parentContent = new Strong();

		title.body("Files list");

		parentAnchor.href(AppConfig.RES_ABS_PATH + username + "/"
				+ DirResource.RES_ROOT + "/cdup");
		parentContent.body(UPDIR_MSG);
		parentAnchor.body(parentContent.toString());
		parentItem.body(parentAnchor.toString());

		list.child(parentItem);

		for (final FTPFile file : files) {
			final Li item = new Li();
			final A anchor = new A();

			if (file.isDirectory()) {

				anchor.href(AppConfig.RES_ABS_PATH + username + "/"
						+ DirResource.RES_ROOT + "/" + file.getName());
				anchor.body(file.getName());
				item.body(anchor.toString());

			} else if (file.isFile()) {

				anchor.href(AppConfig.RES_ABS_PATH + username + "/"
						+ FileResource.RES_ROOT + "/" + file.getName());
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
