package utils;

import java.util.Map;

public class UserManager {
	private Map<String, String> paths;
	
	public String getPath(String username) {
		String path = paths.get(username);
		
		return path;
	}
	
	public void updatePath(String username, String newPath) {
		paths.put(username, newPath);
	}
}
