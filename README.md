TP2: Passerelle REST
====================

## Auteurs

 - Samuel GRANDSIR
 - Gaëtan DEFLANDRE
 
 
## Introduction

Passerelle REST, en java, reliant un serveur FTP avec un client
inviter de commande ou navigateur web. Cette passerelle est multi
utilisateur.


## Architecture

L'application REST est basée sur deux ressources principales, l'une
gérant les fichiers, l'autre les répertoires. Dans l'URL, se trouve
toujours le nom de l'utilisateur, suivi d'un mot clé identifiant le
type de ressource ("dir" ou "file") et terminé éventuellement par le
nom d'un fichier ou d'un répertoire. Un composant de l'application se
charge de la gestion des chemins pour chaque utilisateur et de leur
authentification HTTP via la méthode Basic.


## Design

Nos classes d'exception les classes de haut niveau:
 - NotFoundException
 - ServerErrorException


Nous utilisons le décorateur des objets Response pour construire nos
réponses:

    Response
        .status(Status.UNAUTHORIZED)
	    .header("WWW-Authenticate", "Basic realm=\"Non authentifié pour " + username + "\"")
		.build();


Des classes abstraites permettent de généraliser les classes
représentant du contenu HTML ou JSON.
 - HtmlContainer.java
 - JsonContainer.java


Une arborescence de classes pour les documents HTML existe permettant
une abstraction importante. (voir UML) 


## Gestion d'erreur

### Exception géré par le framework

Nous laissons le framework géré une partie des exceptions, par
exemple:

    public String dirHtml(@PathParam("username") String username)
			throws IOException {
		...
	}

### Exceptions créées pour le projet

Nous disposons de nos propres classes d'exceptions afin d'avoir un
retour en HTML, qui donne la cause précise de l'erreur à
l'utilisateur.

 - RestNotFoundException
 - RestServerErrorException

### Liste des `trow new ...`

Vérification après chaque cd sur le serveur FTP. Dans les ressources
DirRessource et FileRessource:

	UserManager userManager = UserManager.getInstance();
	if (!client.changeWorkingDirectory(userManager.getPath(username))) {
		client.logout();
		client.disconnect();
		throw new RestNotFoundException();
	}
	if (!client.changeWorkingDirectory(dirName)) {
		client.logout();
		client.disconnect();
		throw new RestNotFoundException();
	}


Vérification des authorizations utilisateur:

	try {
		authenticator = new HTTPAuthenticator(authorization);
	} catch (BadAuthorizationHeaderException e) {
		throw new UnauthorizedException(username);
	}
		

Vérification après le retrieve:

	// ERROR CASE
	if (fileInput == null) {
		throw new RestServerErrorException(
				"&eacute;chec obtenrion de fichier.");
	}


Vérification après le delete:

	// ERROR CASE
	if (!deletionSuccessful) {
		throw new RestServerErrorException(
				"&eacute;chec suppression de fichier.");
	}


Vérification après le store:

    // ERROR CASE
	if (!storeSuccessful) {
		throw new RestServerErrorException(
				"&eacute;chec de l'upload de fichier.");
	}


## Code samples

### Obtenir une ressource fichier (action get):

	@GET
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public InputStream getFile(@Context UriInfo uriInfo,
			@PathParam("filename") String filename,
			@PathParam("username") String username) throws IOException {
		FTPClient client = new FTPClient();
		String path;

		// CONNECT ...
		// LOG ...

		// CHANGE DIR
		UserManager userManager = UserManager.getInstance();
		path = userManager.getPath(username);
		if (!client.changeWorkingDirectory(path)) {
			client.logout();
			client.disconnect();
			throw new RestNotFoundException();
		}

		client.setFileType(FTP.BINARY_FILE_TYPE);
		InputStream fileInput = client.retrieveFileStream(filename);

		// QUIT ...

		// ERROR CASE
		if (fileInput == null) {
			throw new RestServerErrorException(
					"&eacute;chec obtenrion de fichier.");
		}

		return fileInput;
	}


### Gestion des utilisateurs connectés et de leurs chemins sur leurs
    espaces FTP.

    public class UserManager {
        private Map<String, String> paths;
	
	    private static UserManager INSTANCE = null;
	
	    private UserManager() {
    		paths = new ConcurrentHashMap<String, String>();
	    }
	
	    public static UserManager getInstance() {
		    ...
	    }
	
	    public String getPath(String username) {
		    String path = paths.get(username);
		    return path;
	    }
	
	    public void putPath(String username, String newPath) {
		    paths.put(username, newPath);
	    }	
    }


### Authentification

	HTTPAuthenticator authenticator;
		
	try {
		authenticator = new HTTPAuthenticator(authorization);
	} catch (BadAuthorizationHeaderException e) {
		throw new UnauthorizedException(username);
	}
		
	// CONNECT
	client.connect(FtpUtils.ADDRESS, FtpUtils.PORT);

	// LOG
	if (!client.login(username, authenticator.getPassword()))
		throw new UnauthorizedException(username);


### Création de la liste de répertoire un HTML

	// loop on the files' list
	for (final FTPFile file : files) {
		final Li item = new Li();
		final A anchor = new A();

		if (file.isDirectory()) { // Directory case

			anchor.href(AppConfig.RES_ABS_PATH + username + "/"
					+ DirResource.RES_NAME + "/" + file.getName());
			anchor.body(file.getName());
			item.body(anchor.toString());

		} else if (file.isFile()) { // File case

			anchor.href(AppConfig.RES_ABS_PATH + username + "/"
					+ FileResource.RES_NAME + "/" + file.getName());
			anchor.body(file.getName());
			item.body(anchor.toString());

		} else { // Other case
			item.body(file.getName());
		}

		list.child(item);
	}


### Classe abstraite permettant de générer du contenu JSON.

    public abstract class JsonContainer {

	    // ATTRIBUTE //

        private String jsonContent;

	    // METHODS //

	    protected abstract void generate();

	    protected void setJSonContent(String jsonContent) {
		    this.jsonContent = jsonContent;
	    }

	    public String toString() {
		    return jsonContent;
	    }

    }
