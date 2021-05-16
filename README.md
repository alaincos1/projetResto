# Projet de simulation d'un restaurant 

## Comment utiliser le projet ?
Notre projet est un projet *Maven* entièrement interactif via la console. 

### Données initiales
Nous avons créé un jeu de données initiales afin que le projet soit utilisable directement sans avoir à créer tous les utilisateurs, les plats, les produits, etc... 
Les seules données qui ne sont pas initialisées sont les factures et les commandes.

### Fichier de configuration
Vu que notre projet est un project Maven, nous avons un fichier **pom.xml** afin d'importer toutes les dépendances/plugins necessaires.

Le fichier **lombok.config** est présent pour la configuration SonarCube, il fait en sorte que toutes les annotations qui créent des méthodes ne soient pas à tester (comme par exemple @Data).

## Choix des tests
Nous n'avons pas fait de tests : 

	- sur les View -> il ne s'agit que d'affichage
	- sur les Constantes -> il n'y a pas de méthodes dans ces classes
	- les classes finissant par collections car cela reviendrait à tester MongoDriver ce qui n'est pas pertinent

## Utilisation Sonar 
Pour ne pas prendre en compte les classes où nous avons pris la décision de ne pas tester, nous avons ajouter ces restrictions dans Coverage Exclusions : 

`**/*Collection.java`

`**/*View.java`

`**/constants/*.java`


## Difficultés rencontrées
Nous avons essayé de créer des tests sur des méthodes statiques mais nous n'y sommes pas parvenu, par exemple pour la classe InputUtil.





