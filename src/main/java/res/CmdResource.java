package res;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 * http://localhost:8080/rest/api/cmd
 * 
 * @author Lionel Seinturier <Lionel.Seinturier@univ-lille1.fr>
 */
@Path("/cmd")
public class CmdResource {

	@GET
	@Produces("text/html")
	public String sayHello() {
		String output;
		
		output = "<h1>POST</h1>";
		
		return output;
	}
	
	

	@GET
	@Path("/param/{isbn}")
	@Produces("text/html")
	public String getBook(@PathParam("isbn") String isbn) {
		return "Book: " + isbn;
	}

	@GET
	@Path("{var: .*}/stuff")
	public String getStuff(@PathParam("var") String stuff) {
		return "Stuff: " + stuff;
	}
}
