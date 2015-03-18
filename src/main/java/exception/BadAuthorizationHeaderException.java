package exception;

/**
 * Exception renvoyée quand un header d'authentification HTTP
 * a été identifié comme incorrect.
 * 
 * @author Samuel Grandsir
 *
 */
public class BadAuthorizationHeaderException extends Exception {

	private static final long serialVersionUID = -7283307874440789185L;
	
	public BadAuthorizationHeaderException(String falseHeader) {
		super(falseHeader + " is not a correct authorization header.");
	}
}
