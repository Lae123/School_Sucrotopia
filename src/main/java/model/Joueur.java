package main.java.model;

import java.util.Scanner;

import main.java.GUI.TerrainGui;
import main.java.geometry.RealCoordinates;

public class Joueur {
    private Scanner scanReponse;
    private int argent;
    private int vies;
    private int score;
    private final int initialHealth;

    // Constructeur initialisant les attributs : un scanner permettant à l'utilisateur de saisir des réponses 
    // depuis la console, et les points de vie en fonction de la difficulté spécifiée
    public Joueur(String niveaudeDifficulte) {
        this.scanReponse = new Scanner(System.in);
        this.argent = 200;
        this.score = 0;

        switch (niveaudeDifficulte.toLowerCase()) {
            case "facile":
                this.initialHealth = this.vies = 150;
                break;
            case "moyen":
                this.initialHealth = this.vies = 100;
                break;
            case "difficile":
                this.initialHealth = this.vies = 100;
                break;
            case "marathon":
                this.initialHealth = this.vies = 200;
                break;
            default:
                this.initialHealth = this.vies = 50;
        }
    }

    public int getArgent() {
        return argent;
    }

    public int getInitialHealth() {
        return initialHealth;
    }

    public void setArgent(int argent) {
        this.argent = argent;
    }

    public int getVies() {
        return vies;
    }

    public void setVies(int vies) {
        this.vies = vies;
    }

    public int getScore() {
        return score;
    }

    public void gagnerArgent(int montant) {
        this.argent += montant;
    }

    public void augmenterScore(int points) {
        this.score += points;
    }

    public void perdreVie(int pv) {
        this.vies -= pv;
    }


    // ---------------------------------------------------- //
    ////////         Terminal et Graphique            ////////
    // ---------------------------------------------------- //


    // Crée une tour spécifique aux coordonnées passées en argument 
    public Tours creerTour(Terrain terrain, String typeTourString, int[] coordonnees) {
        RealCoordinates coordinates = new RealCoordinates(coordonnees[0], coordonnees[1]);
        Tours tour = null;

        switch (typeTourString) {
            case "NOUNOURS_GUMMY":
                tour = new NounoursGummy(this, terrain, coordinates, Menu.getDifficultyLevel());
                break;
            case "FILAMENT_BARBAPAPA":
                tour = new FilamentBarbapapa(this, terrain, coordinates, Menu.getDifficultyLevel());
                break;
            case "LANCE_TOPPING":
                tour = new LanceTopping(this, terrain, coordinates, Menu.getDifficultyLevel());
                break;
            case "MARSHMALLOW_ENFLAMME":
                tour = new MarshmallowEnflamme(this, terrain,coordinates, Menu.getDifficultyLevel());
                break;
            default:
                System.out.println("Type de tour non valide !");
        }

        return tour;
    }

    // Place la tour passée en argument sur le terrain en mettant à jour le type de contenu de la cellule
    public void placerTour(int x, int y, Tours tour, Terrain terrain) {
        if ((x >= 0 && x < terrain.getLargeur() && y >= 0 && y < terrain.getHauteur()) && terrain.getCellule(x, y).getTypeContenu() == typeContenu.VIDE) {
            terrain.getCellule(x, y).setTypeContenu(typeContenu.EMPLACEMENT_TOUR);
            terrain.getCellule(x, y).setTour(tour);
            
            System.out.println(terrain.getCellule(x, y).getTour().toString());

        }
    }

    // Récupère le type de tour en fonction du nombre passé en argument
    public String getTypeTour(int number) {
        String typeTour;
        switch (number) {
            case 1:
                typeTour = "NOUNOURS_GUMMY";
                break;
            case 2:
                typeTour = "FILAMENT_BARBAPAPA";
                break;
            case 3:
                typeTour = "LANCE_TOPPING";
                break;
            case 4:
                typeTour = "MARSHMALLOW_ENFLAMME";
                break;
            default:
                typeTour = "";
                break;
        }
        return typeTour;
    }


    // ----------------------------------- //
    ////////       Terminal          ////////
    // ----------------------------------- //

    // Demande à l'utilisateur une action en entrant un chiffre entre 1 et 3
    public void demanderAction(Terrain terrain, TerrainGui terrainGui) {
        int reponse = 0;
        boolean saisieValide = false;

        System.out.println("=== Que voulez-vous faire ? ===");
        System.out.println("1. Placer une tour");
        System.out.println("2. Améliorer une tour");
        System.out.println("3. Afficher la map" + "\n");
    
        while (!saisieValide) {    
            if (scanReponse.hasNextInt()) {
                reponse = scanReponse.nextInt();
                if (reponse >= 1 && reponse <= 3) {
                    saisieValide = true;
                } else {
                    System.out.println("Veuillez choisir un numéro entre 1 et 3.");
                }
            } else {
                System.out.println("Veuillez saisir un nombre valide.");
                scanReponse.nextLine();
            }
        }

        switch (reponse) {
            case 1:
                placerTourSurMap(terrain);
                break;
            case 2:
                ameliorerTourSurMap(terrain);
                break;
            case 3:
                if (terrainGui != null) {
                    terrainGui.mettreAJourAffichage();
                } else {
                    terrain.afficherTerrain();
                }
                break;
            default:
                System.out.println("Choix non valide !");
                break;
        }
    }

    // Place une tour sur le terrain en demandant à l'utilisateur les coordonnées de l'emplacement s'il a suffisamment d'argent.
    // Si oui, déduit le prix de la tour à l'argent du joueur
    public void placerTourSurMap(Terrain terrain) {

        int typeTour = demanderPlacerTour();
        String typeTourString = getTypeTour(typeTour);
        int[] coordonnees = demanderCoordonneesTour(terrain);

        int cout = Tours.getPrixTour(typeTourString);
        if (argent >= cout) {
            Tours tour = creerTour(terrain, typeTourString, coordonnees);
            placerTour(coordonnees[0], coordonnees[1], tour, terrain);
            argent -= cout;
        } else {
            System.out.println("Pas assez d'argent pour placer la tour !");
        }

    }


    // Demande le type de tour à placer en entrant un chiffre entre 1 et 3
    public int demanderPlacerTour() {
        int choix = 0;
        boolean saisieValide = false;

        System.out.println("=== Placer une tour ===");
        System.out.println("1. NOUNOURS_GUMMY");
        System.out.println("2. FILAMENT_BARBAPAPA");
        System.out.println("3. LANCE_TOPPING");
        System.out.println("4. MARSHMALLOW_ENFLAMME");
    
        while (!saisieValide) {
            if (scanReponse.hasNextInt()) {
                choix = scanReponse.nextInt();
                if (choix >= 1 && choix <= 4) {
                    saisieValide = true;
                } else {
                    System.out.println("Veuillez choisir un numéro entre 1 et 4.");
                }
            } else {
                System.out.println("Veuillez saisir un numéro valide.");
                scanReponse.nextLine();
            }
        }
    
        return choix;
    }
    
         
    // Demande à l'utilisateur d'entrer les coordonnées de la tour à placer : 
    // convertit la première lettre en indice de colonne et décrémente la ligne pour obtenir les coordonnées du terrain
    public int[] demanderCoordonneesTour(Terrain terrain) {
        int[] coordonneesTableau = new int[2];
        boolean saisieValide = false;

        System.out.println("Entrez les coordonnées pour placer la tour (ex: B6) : ");

        while (!saisieValide) {
            String coordonnees = scanReponse.next().toUpperCase();

            if (coordonnees.length() == 2) {
                char colonneChar = coordonnees.charAt(0);
                int ligne = Character.getNumericValue(coordonnees.charAt(1));

                if (colonneChar >= ('A' + terrain.getLargeur() - 2) && colonneChar < ('A' + terrain.getLargeur()) && ligne >= 1 && ligne <= terrain.getHauteur()) {
                    int colonne = colonneChar - 'A';
                    int ligneIndex = ligne - 1;

                    // Récupérer la cellule aux coordonnées spécifiées
                    Cellule cellule = terrain.getCellule(ligneIndex, colonne);
                    
                    if (cellule.getTypeContenu() == typeContenu.VIDE) {
                        coordonneesTableau[0] = ligneIndex;
                        coordonneesTableau[1] = colonne;
                        saisieValide = true;
                        
                    } else {
                        System.out.println("Il y a déjà une tour à cet endroit.");
                    }
                } else {
                    System.out.println("Coordonnées incorrectes. Veuillez entrer des coordonnées valides.");
                }
            } else {
                System.out.println("Coordonnées incorrectes. Veuillez entrer des coordonnées valides.");
            }
        }

        return coordonneesTableau;
    }
    

    // Améliore la tour située aux coordonnées entrées par l'utilisateur si le joueur a suffisamment d'argent et
    // que la limite d'amélioration n'est pas atteinte
    public void ameliorerTourSurMap(Terrain terrain) {
        int[] coordonnees = demanderAmeliorerTour(terrain);
        
        int x = coordonnees[0];
        int y = coordonnees[1];

        Cellule cellule = terrain.getCellule(x, y);
 
        if (cellule.getTour() != null) {
            if (cellule.getTour().getNiveau() < 3) {
                Tours tour = cellule.getTour();
        
                int coutAmelioration = tour.getPrix();
                if (argent >= coutAmelioration) {
                    argent -= coutAmelioration;
                    tour.ameliorerTour();
                    System.out.println("Tour améliorée avec succès !");
                    
                } else {
                    System.out.println("Pas assez d'argent pour améliorer la tour !");
                }
            } else {
                System.out.println("Niveau maximum d'amélioration atteint pour cette tour !");
            }
        } else {
            System.out.println("La cellule sélectionnée ne contient pas de tour à améliorer.");
        }
    }

    // Récupére les coordonnées d'une tour à améliorer si elle existe
    public int[] demanderAmeliorerTour(Terrain terrain) {
        int[] coordonneesTableau = new int[2];
        boolean saisieValide = false;

        System.out.println("Entrez les coordonnées de la tour à améliorer (ex: B6) : ");
    
        while (!saisieValide) {
            String coordonnees = scanReponse.next().toUpperCase();
    
            if (coordonnees.length() == 2) {
                char colonneChar = coordonnees.charAt(0);
                int ligne = Character.getNumericValue(coordonnees.charAt(1));
    
                if (colonneChar >= ('A' + terrain.getLargeur() - 2) && colonneChar < ('A' + terrain.getLargeur()) && ligne >= 1 && ligne <= terrain.getHauteur()) {
                    int colonne = colonneChar - 'A';
                    int ligneIndex = ligne - 1;
    
                    Cellule cellule = terrain.getCellule(ligneIndex, colonne);
    
                    if (cellule.getTypeContenu() == typeContenu.EMPLACEMENT_TOUR && cellule.getTour() != null) {
                        coordonneesTableau[0] = ligneIndex;
                        coordonneesTableau[1] = colonne;
                        saisieValide = true;

                    } else {
                        System.out.println("Il n'y a pas de tour à améliorer à cet emplacement.");
                    }
                } else {
                    System.out.println("Coordonnées incorrectes. Veuillez entrer des coordonnées valides sur les deux dernières colonnes du terrain.");
                }
            } else {
                System.out.println("Coordonnées incorrectes. Veuillez entrer des coordonnées valides.");
            }
        }
    
        return coordonneesTableau;
    }


    public void closeScanner() {
        scanReponse.close();
    }


    // ----------------------------------- //
    ////////          Gui            ////////
    // ----------------------------------- //

    public void placerTourSurMapGui(Terrain terrain, String typeTour, int[] coordonnees) {
        int x = coordonnees[0];
        int y = coordonnees[1];
        Tours tour = null;

        switch (typeTour) {
            case "NOUNOURS_GUMMY":
                tour = new NounoursGummy(this, terrain, new RealCoordinates(x, y), Menu.getDifficultyLevel());
                break;
            case "FILAMENT_BARBAPAPA":
                tour = new FilamentBarbapapa(this, terrain, new RealCoordinates(x, y), Menu.getDifficultyLevel());
                break;
            case "LANCE_TOPPING":
                tour = new LanceTopping(this, terrain, new RealCoordinates(x, y), Menu.getDifficultyLevel());
                break;
            case "MARSHMALLOW_ENFLAMME":
                tour = new MarshmallowEnflamme(this, terrain, new RealCoordinates(x, y), Menu.getDifficultyLevel());
                break;
            default:
                System.out.println("Type de tour non valide !");
                return;
        }

        placerTour(x, y, tour, terrain);
    }


    public int[] demanderCoordonneesTourGui(int x, int y, Terrain terrain) {
        int[] coordonneesTableau = new int[2];
        boolean saisieValide = false;
    
        int ligneIndex = y;
        int colonne = x;
    
        if (ligneIndex >= 0 && ligneIndex < terrain.getHauteur() && colonne >= 0 && colonne < terrain.getLargeur()) {
            Cellule cellule = terrain.getCellule(ligneIndex, colonne);
    
            if (cellule.getTypeContenu() == typeContenu.VIDE) {
                coordonneesTableau[0] = ligneIndex;
                coordonneesTableau[1] = colonne;
                saisieValide = true;
            }
        }
    
        return saisieValide ? coordonneesTableau : null;
    }


    public void ameliorerTourSurMapGui(Terrain terrain, int x, int y) {
        Cellule cellule = terrain.getCellule(y, x);

        if (cellule.getTour() != null) {
            Tours tour = cellule.getTour();
            if (tour.getNiveau() < 3) {
                int coutAmelioration = tour.getPrix();
                if (argent >= coutAmelioration) {
                    argent -= coutAmelioration;
                    tour.ameliorerTour();
                    System.out.println("Tour améliorée avec succès !");
                } else {
                    System.out.println("Pas assez d'argent pour améliorer la tour !");
                }
            } else {
                System.out.println("Niveau maximum d'amélioration atteint pour cette tour !");
            }
        } else {
            System.out.println("La cellule sélectionnée ne contient pas de tour à améliorer.");
        }
    }

}