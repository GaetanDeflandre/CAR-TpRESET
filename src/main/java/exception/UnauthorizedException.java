package exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Exception renvoyée quand le client n'est pas authentifié 
 * d'un point de vue HTTP.
 * @author Samuel Grandsir
 *
 */
public class UnauthorizedException extends WebApplicationException {

	private static final long serialVersionUID = 4235743755689632833L;

	public UnauthorizedException(String username) {
		super(
			Response
				.status(Status.UNAUTHORIZED)
				.header("WWW-Authenticate", "Basic realm=\"Non authentifié pour " + username + "\"")
				.build());
	}

}
