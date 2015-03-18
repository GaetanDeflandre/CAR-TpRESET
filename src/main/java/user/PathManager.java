package user;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

public class PathManager implements ContainerRequestFilter {
	private Map<String, String> paths;
	
	private static PathManager INSTANCE = null;
	
	private PathManager() {
		paths = new ConcurrentHashMap<String, String>();
	}
	
	public static PathManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PathManager();
		
		return INSTANCE;
	}
	
	public String getPath(String username) {
		String path = paths.get(username);
		
		return path;
	}
	
	public void putPath(String username, String newPath) {
		paths.put(username, newPath);
	}

	@Override
	public void filter(ContainerRequestContext arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
