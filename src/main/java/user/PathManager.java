package user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton permettant de gérer les chemins de chaque utilisateur 
 * FTP sur le serveur FTP. Il est possible d'affecter ou de récupérer 
 * une valeur d'un chemin pour un utilisateur donné. Cette classe 
 * est utilisée par les ressources.
 * 
 * @author Samuel Grandsir
 *
 */
public class PathManager {
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
	
	/**
	 * 
	 * @param username
	 * @return le chemin de l'utilisateur donné en paramètre
	 */
	public String getPath(String username) {
		String path = paths.get(username);
		
		return path;
	}
	
	/**
	 * Affecte une nouvelle valeur de chemin pour un utilisateur
	 * @param username l'utilisateur attribué au chemin
	 * @param newPath le nouveau chemin
	 */
	public void putPath(String username, String newPath) {
		paths.put(username, newPath);
	}
}
