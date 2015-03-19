package user;

import org.apache.commons.net.util.Base64;

import plateform.exceptions.BadAuthorizationHeaderException;

/**
 * Représente les informations de connexion qu'envoie un client HTTP.
 * Basé sur la méthode d'authentification HTTP "Basic"
 * @author Samuel Grandsir
 *
 */
public class HTTPAuthenticator {
	private String username;
	private String password;
	
	/**
	 * Analyse la chaîne donnée en paramètre, la décode, et en extrait les informations
	 * correspondant respectivement au nom d'utilisateur et au mot de passe. Ce constructeur
	 * s'assure de l'intégrité de l'en-tête HTTP.
	 * 
	 * @param authorizationHeader l'en-tête HTTP tel qu'il est envoyé par le client.
	 * @throws BadAuthorizationHeaderException si l'en-tête est incorrecte selon 
	 * la méthode Basic
	 */
	public HTTPAuthenticator(String authorizationHeader) throws BadAuthorizationHeaderException {
		boolean badArgument = false;
		String authDecoded;
		String [] tokens;
		
		if (authorizationHeader == null)
			badArgument = true;
		else if (authorizationHeader.startsWith("Basic ")) {
			authorizationHeader = authorizationHeader.substring(6);
			if (Base64.isArrayByteBase64(authorizationHeader.getBytes())) {
				authDecoded = new String(Base64.decodeBase64(authorizationHeader.getBytes()));
				tokens = authDecoded.split(":", 2);
				if (tokens.length == 2) {
					username = tokens[0];
					password = tokens[1];
				}
				else
					badArgument = true;
			}
			else
				badArgument = true;
		}
		else
			badArgument = true;
		
		if (badArgument)
			throw new BadAuthorizationHeaderException(authorizationHeader);
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
