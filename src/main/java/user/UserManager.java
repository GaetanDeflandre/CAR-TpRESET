package user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
	private Map<String, String> paths;
	
	private static UserManager INSTANCE = null;
	
	private UserManager() {
		paths = new ConcurrentHashMap<String, String>();
	}
	
	public static UserManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new UserManager();
		
		return INSTANCE;
	}
	
	public String getPath(String username) {
		String path = paths.get(username);
		
		return path;
	}
	
	public void putPath(String username, String newPath) {
		paths.put(username, newPath);
	}
	
}
