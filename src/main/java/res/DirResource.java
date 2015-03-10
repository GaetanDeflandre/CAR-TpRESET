package res;

import java.io.IOException;
import java.net.SocketException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 * http://localhost:8080/rest/api/cmd
 * 
 * @author Lionel Seinturier <Lionel.Seinturier@univ-lille1.fr>
 */
@Path("/dir")
public class DirResource {

	public final static String ADDRESS = "127.0.0.1";
	public final static int PORT = 2121;
	public final static String ENDL = "\r\n";
	
	public final static String LOGIN = "toto";
	public final static String PASS = "mdp";

	@GET
	@Produces("text/html")
	public String directories() {
		String html;
		FTPClient client = new FTPClient();

		html = "<h1>Ressources repertoires</h1>" + ENDL;

		// CONNECT
		try {
			client.connect(ADDRESS, PORT);
		} catch (SocketException e) {
			html += e.getMessage() + ENDL;
			return html;
		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}

		// LOG
		try {
			client.login(LOGIN, PASS);
		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}
		
		// LIST
		try {
			FTPFile[] files = client.listFiles();
			
			html += "<ul>" + ENDL;
			for (FTPFile file : files) {
				if(file.isDirectory()){
					html += "<li>" + file.getName() + "</li>";
				} else if(file.isFile()){
					html += "<li>" + file.getName() + "</li>";
				} else {
					html += "<li>" + file.getName() + "</li>";
				}
			}
			html += "</ul>" + ENDL;

		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}
		
		// QUIT
		try {
			client.quit();
			client.disconnect();
		} catch (IOException e) {
			html += e.getMessage() + ENDL;
			return html;
		}

		return html;
	}

	@GET
	@Path("a/{isbn}")
	@Produces("text/html")
	public String getBook(@PathParam("isbn") String isbn) {
		return "Book: " + isbn;
	}

	@GET
	@Path("{var}/b")
	public String getStuff(@PathParam("var") String stuff) {
		return "Stuff: " + stuff;
	}

}
