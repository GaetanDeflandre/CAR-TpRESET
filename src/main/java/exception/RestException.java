package exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class RestException extends WebApplicationException {

	private static final long serialVersionUID = 8784202304772216873L;

	public RestException(final String message) {
		super(Response.status(Status.NOT_FOUND).entity("Not found: " + message)
				.build());
	}

}
