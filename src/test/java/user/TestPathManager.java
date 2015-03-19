package user;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPathManager {

	@Test
	public void testSingleton() {
		PathManager instance = PathManager.getInstance();
		
		assertNotNull(instance);
	}
	
	@Test
	public void testGetSetPath() {
		PathManager pm = PathManager.getInstance();
		String user = "userTest";
		String path = "test/path/toto";
		
		pm.putPath(user, path);
		
		assertEquals(path, pm.getPath(user));
	}

}
