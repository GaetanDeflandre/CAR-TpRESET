package user;

import static org.junit.Assert.*;

import org.apache.commons.net.util.Base64;
import org.junit.Test;

import exception.BadAuthorizationHeaderException;

public class TestHTTPAuthenticator {

	@Test
	public void testNominal() throws BadAuthorizationHeaderException {
		String user = "testuser";
		String mdp = "testmdp";
		String authEncoded = "Basic "+Base64.encodeBase64String((user+":"+mdp).getBytes());
		HTTPAuthenticator auth = new HTTPAuthenticator(authEncoded);
		
		assertEquals(user, auth.getUsername());
		assertEquals(mdp, auth.getPassword());
	}
	
	@Test(expected = BadAuthorizationHeaderException.class)
	public void testNull() throws BadAuthorizationHeaderException {
		HTTPAuthenticator auth = new HTTPAuthenticator(null);
	}
	
	@Test(expected = BadAuthorizationHeaderException.class)
	public void testBadPrefix() throws BadAuthorizationHeaderException {
		String test = "xxxxx "+Base64.encodeBase64String("user:mdp".getBytes());
		HTTPAuthenticator auth = new HTTPAuthenticator(test);
	}
	
	@Test(expected = BadAuthorizationHeaderException.class)
	public void testNotBase64() throws BadAuthorizationHeaderException {
		HTTPAuthenticator auth = new HTTPAuthenticator("Basic $$ùmdp");
	}
	
	@Test(expected = BadAuthorizationHeaderException.class)
	public void testBadFormat() throws BadAuthorizationHeaderException {
		String test = "Basic "+Base64.encodeBase64String("usermdp".getBytes());
		HTTPAuthenticator auth = new HTTPAuthenticator(test);
	}

}
