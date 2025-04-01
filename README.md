# Sucrotopia

Bienvenue dans Sucrotopia, le paradis des sucreries !

## Installation

1. Clonez ce dépôt sur votre machine:


2.  Accédez au répertoire Sucrotopia : cd sucrotopia

## Compilation et Lancement

Sur votre terminal copier coller ces commandes pour acceder à la version graphique :

* Remarque :  Ces commandes vous permettent de lancer à la fois la version console et la version graphique du jeu, offrant ainsi une visibilité complète des deux modes de jeu. 

De plus, il est important de noter que depuis le mode console, le terrain est affiché en mode graphique, offrant ainsi une expérience visuelle enrichie.

## Sur Mac et Linux : 

### Lancer depuis le mode console : 

- javac -d bin -sourcepath src src/main/java/**/*.java
- java -cp bin main.java.model.Menu

### Lancer depuis le mode Graphique :

- javac -d bin -sourcepath src src/main/java/**/*.java
- java -cp bin main.java.GUI.MenuGui


## Sur Windows :

### Lancer depuis le mode console : 

    - javac -d bin -sourcepath src src\main\java\**\*.java
    - java -cp bin main.java.model.Menu

### Lancer depuis le mode Graphique :

    - javac -d bin -sourcepath src src\main\java\**\*.java
    - java -cp bin main.java.GUI.MenuGui


## Tuto Graphique : 

-  Une fois le jeu lancé, vous vous retrouverez sur le menu principal. En sélectionnant "Commencer la partie", vous accéderez à un écran présentant l'histoire de l'univers de Sucrotopia que vous avez la possibilité de passer en appuyant sur la touche "Skip".

-  Ensuite, vous serez dirigé vers une fenêtre où vous devrez choisir le niveau de difficulté parmi les options suivantes : Facile, Moyen, Difficile, et Marathon.

-  Une fois votre choix effectué, vous atterrirez sur l'écran du terrain de jeu. À intervalles réguliers de 10 secondes, des vagues d'ennemis seront lancées.

-  Pour placer une tour, vous disposerez d'informations détaillées, notamment leur coût initial, leurs dégâts, ainsi que la possibilité de les améliorer à trois niveaux différents. Ces informations vous permettront de choisir le type de tour et de la placer sur les cases chocolatées situées derrière le mur. Il est important de noter que seules ces cases sont autorisées ; il ne vous sera pas possible de positionner une tour sur une case murale ou sur le chemin emprunté par les ennemis.

-  Une fois la tour placée, elle sera prête à attaquer tous les monstres qui se présenteront devant elle. Afin d'améliorer votre tour, assurez-vous d'avoir suffisamment d'argent, puis cliquez simplement sur la tour pour la renforcer. Vous pourrez observer visuellement le niveau d'amélioration, renforçant ainsi son efficacité. Soyez stratégique dans le choix des emplacements pour maximiser l'efficacité de vos tours défensives.

-  Pour gagner c'est simple éliminier les ennemis pour sauver le royaume de Sucrotopia.
Sinon vous perder => Si c'est le cas vous avez la possibilté de rejoueur la partie, ainsi que la possibilté de quitter.

## Tuto Console : // NOTE : Cette version du jeu est une implémentation basique et peut être améliorée pour inclure des fonctionnalités plus avancées. // AVERTISSEMENT : Cette version du jeu inclut des effets sonores pour les monstres et les attaques

- Pour démarrer une nouvelle partie depuis le terminal, choisissez d'abord la difficulté souhaitée. Une fois sélectionnée, une mini-carte s'affiche. Vous aurez alors la possibilité de placer une tour, d'en améliorer une existante et d'afficher le terrain.

- Lors de l'amélioration d'une tour, veuillez fournir ses coordonnées, par exemple B8 (LettreChiffre). Soyez attentif, car vous devrez positionner la tour sur les points autorisés.
Sur la carte, les cellules de chemin sont représentées par la lettre C, les ennemis par X, les murs par M, les tours déjà placées par T, et les emplacements possibles pour les tours par des points.

- Lors de l'amélioration des tours, suivez la même logique que pour le placement en vous assurant que les coordonnées saisies correspondent à une tour existante.
Pour mettre à jour la carte, appuyez ensuite sur trois pour suivre la progression des ennemis. Cela rafraîchira le terminal, mais veuillez noter qu'il y a un léger délai de deux secondes.

 
Pour une meilleure vue d'ensemble, le terrain du jeu est aussi affiché sous forme graphique (ne fermer pas la fenetre sinon le jeu s'arrete).


Bon Jeu !
