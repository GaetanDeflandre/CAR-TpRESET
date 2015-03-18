package plateform.exceptions;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Exception levée lorsqu'une ressource est introuvable.
 */
public class RestNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 8784202304772216873L;
	private static final String MESSAGE = "<h1>Erreur 404: Ressource non trouv&eacute;e</h1>";

	/**
	 * Construction de l'exception avec un message HTML.
	 */
	public RestNotFoundException() {
		super(Response.status(Status.NOT_FOUND).entity(MESSAGE).build());
	}

}
