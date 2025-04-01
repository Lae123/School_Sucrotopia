package main.java.model;

import java.util.ArrayList;
import java.util.List;

import main.java.geometry.RealCoordinates;

public class Terrain {
    private Cellule [][] terrain;
    private List<Cellule> chemin;
    private int largeur;
    private int hauteur;

    public int getLargeur(){
        return this.largeur;
    }

    public int getHauteur(){
        return this.hauteur;
    }

    public Cellule getCellule(int ligne, int colonne) {
        return terrain[ligne][colonne];
    }

    public List<Cellule> getChemin() {
        return chemin;
    }

    public Cellule[][] getTerrain() {
        return terrain;
    }

    public void setHauteur(int h){
        this.hauteur = h;
    }

    public void setLargeur(int l){
        this.largeur = l;
    }
    
    public Terrain(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.terrain = new Cellule[largeur][hauteur];
        this.chemin = new ArrayList<>();
        initialiserTerrain();
        genererChemins();
    }
    
    //  
    public RealCoordinates[] getFirstCoor() {
        List<RealCoordinates> coordinatesList = new ArrayList<>();
    
        for (int x = 0; x < this.hauteur; x++) {
            int y = 0;
    
            if (terrain[x][y].getTypeContenu() == typeContenu.CHEMIN) {
                coordinatesList.add(new RealCoordinates(x, y));
            }
        }
    
        if (!coordinatesList.isEmpty()) {
            return coordinatesList.toArray(new RealCoordinates[0]);
        } else {
            return new RealCoordinates[0];
        }
    }
    
    // Initialise le terrain en créant de nouvelles cellules associées aux coordonnées correspondantes
    private void initialiserTerrain() {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {

                char colonne = (char) ('A' + j);
                int ligne = i + 1;
                String coordonnees = "" + colonne + ligne;

                terrain[i][j] = new Cellule();
                terrain[i][j].setCoordinates(coordonnees);
            }
        }
    }
    

    public boolean estPositionValide(int x, int y) {
        return x >= 0 && x < largeur && y >= 0 && y < hauteur;
    }


    // Affiche le terrain de jeu : 
    // les repères du terrain avec les lettres associées aux colonnes et les chiffres associés aux lignes, et
    // le contenu des cellules
    public void afficherTerrain() {

        System.out.print("    ");
        for (int i = 0; i < largeur; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();

        for (int i = 1; i <= largeur*2 + 3; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i < hauteur; i++) {
            System.out.print((i + 1) + " | ");
        
            for (int j = 0; j < largeur; j++) {
                switch (terrain[i][j].getTypeContenu()) {
                    case CHEMIN:
                        System.out.print("C ");
                        break;
                    case EMPLACEMENT_TOUR:
                        System.out.print("T ");
                        break;
                    case MUR :
                        System.out.print("M ");
                        break;
                    case ENNEMI:
                        System.out.print("X ");
                        break;
                    default:
                        System.out.print(". ");
                        break;
                }
            }
            System.out.println();
        }
    }

    public void afficherDetailsJoueur(Joueur joueur) {
        System.out.println("\n=== Détails du joueur ===");
        System.out.println("Points de vie : " + joueur.getVies());
        System.out.println("Argent : " + joueur.getArgent());
        System.out.println("Score : " + joueur.getScore() + "\n");
    }

    public void genererChemins() {
    
        // Emplacements des tours (T)
        for (int row = 0; row < hauteur; row++) {
            for (int col = largeur - 2; col < largeur; col++) {
                terrain[row][col].setTypeContenu(typeContenu.VIDE);
            }
        }
    
        // Emplacements des murs (M)
        for (int row = 0; row < hauteur; row++) {
            for (int col = largeur - 3; col < largeur - 2; col++) {
                terrain[row][col].setTypeContenu(typeContenu.MUR);
            }
        }
    
        // Emplacements des chemins (C)
        for (int row = 0; row < hauteur; row++) {
            for (int col = 0; col < largeur - 3; col++) {
                terrain[row][col].setTypeContenu(typeContenu.CHEMIN);
            }
        }
    }

    
}

