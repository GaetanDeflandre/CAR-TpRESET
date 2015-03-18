package plateform.exceptions;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

/**
 * Exception levée lorsqu'un problème du serveur FTP est rencontré.
 */
public class RestServerErrorException extends ServerErrorException {

	private static final long serialVersionUID = 8784202304772216873L;
	private static final String ERROR_TYPE = "Erreur 500: ";

	/**
	 * Construction de l'exception avec un affichage HTML, contenant le message
	 * <tt>message</tt>.
	 * 
	 * @param message
	 *            Message à afficher.
	 */
	public RestServerErrorException(final String message) {
		super(Response.serverError()
				.entity("<h1>" + ERROR_TYPE + message + "</h1>").build());
	}
}
