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

Le projet constituer de diffèrents packages:

 - res: contenant les classes des ressources REST.
 - user: contenant les classes liées aux utilisateurs connectées sur
   la passerelle REST.
 - utils: contenant des informations sur le serveur FTP.
 - plateform: contenant les classes directement liées à la passerelle
   REST
   - plateform: contenant la classe de démarrage de la
     passerelle REST.
   - plateform.config: contenant la classe de configuration de la
     passerelle REST.
   - plateform.exceptions: contenant les classes d'exceptions de la
     passerelle REST.
 - html: contenant les classes permettant de générer du contenu HTML.
 - json: contenant les classes permettant de générer du contenu JSON.


## Gestion d'erreur


## Code samples
