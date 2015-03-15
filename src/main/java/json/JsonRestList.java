package json;

import org.apache.commons.net.ftp.FTPFile;

import uk.co.wireweb.web.javascript.json.JsonArray;
import uk.co.wireweb.web.javascript.json.JsonKeyValuePair;
import uk.co.wireweb.web.javascript.json.JsonObject;

public class JsonRestList extends JsonContainer {

	private String username;
	private String path;
	private FTPFile[] files;

	public JsonRestList(final String username, final String path,
			final FTPFile[] files) {

		this.username = username;
		this.path = path;
		this.files = files;
		
		generate();

	}

	@Override
	protected void generate() {
		JsonObject root = new JsonObject("root");
		JsonArray jsonFiles = new JsonArray("files");
		
		for (FTPFile file : files) {	
			jsonFiles.member(new JsonKeyValuePair<String>("value",file.getRawListing()));
		}
		
		root.member( new JsonKeyValuePair<String>("path", path) );
		root.member( new JsonKeyValuePair<String>("username", username) );
		root.array(jsonFiles);
		
		setJSonContent(root.toString());
	}
}
