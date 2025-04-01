package main.java.model;

import main.java.geometry.RealCoordinates;
import java.util.List;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.File;
import javax.sound.sampled.*;

public abstract class Tours {

    protected Joueur joueur;
    protected Terrain terrain;
    protected final String type;
    protected RealCoordinates coordinates;
    protected int prix;
    protected int degats; 
    protected double vitesseAttaque;
    protected int niveau;
    protected boolean[][] portee;
    protected ScheduledExecutorService executor;
    protected String difficulte;


    public Tours(Joueur joueur, Terrain terrain, String type, RealCoordinates coordinates, int prix, int degats, double vitesseAttaque, String diff) {
        this.joueur = joueur;
        this.terrain = terrain;
        this.type = type;
        this.coordinates = coordinates;
        this.niveau = 1;
        this.prix = prix;
        this.degats = degats;
        this.vitesseAttaque = vitesseAttaque;
        initializePortee();
        this.executor = Executors.newSingleThreadScheduledExecutor();
        attaquer(joueur);
        this.difficulte = diff;
        ajusterDegatsEnFonctionDifficulte(niveau);
    }

    public String getType() {
        return type;
    }

    public RealCoordinates getCoordinates() {
        return coordinates;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getPrix() {
        return prix;
    }

    public boolean[][] getPortee() {
        return portee;
    }

    public void setPortee(boolean[][] p) {
        this.portee = p;
    }

    public int getDegat() {
        return this.degats;
    }

    public void setDegat(int d) {
        this.degats = d;
    }

    // Récupère le prix en fonction du type de tour
    public static int getPrixTour(String typeTour) {
        switch (typeTour) {
            case "MARSHMALLOW_ENFLAMME":
                return 80; 
            case "FILAMENT_BARBAPAPA":
                return 60;
            case "LANCE_TOPPING":
                return 20; 
            case "NOUNOURS_GUMMY":
                return 20;
            default:
                return 0;
        }
    }

    public abstract void ameliorerTour();  

    private void jouerSonAttaque() {
        try {
            String sonAttaqueFilePath = "bin/ressources/sons/explosion.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sonAttaqueFilePath).getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialise la portée de la tour à deux cellules (les plus proches du mur): les cellules de la portée sont initialisées à vrai
    public void initializePortee() {
        portee = new boolean[terrain.getHauteur()][terrain.getLargeur()];

        int x = (int)coordinates.getX();
        int y = terrain.getLargeur() - 4;

        portee[x][y] = true;
        portee[x][y - 1] = true;
    }

    // Améliore la portée de la tour en l'augmentant d'une cellule
    public void ameliorerPortee() {
        if (this.niveau < 3) {
            int tourX = (int)coordinates.getX();
            int tourY = terrain.getLargeur() - 4;
        
            for (int i = 1; i <= tourY; i++) {
                if (portee[tourX][i]) {
                    portee[tourX][tourY - 1] = true;
                    break;
                }
            }
        }
    }


    // Crée une liste de cellules et y ajoute toutes les cellules de la portée
    public List<Cellule> getCellulesDansPortee() {
        List<Cellule> cellulesDansPortee = new ArrayList<>();
    
        for (int i = 0; i < terrain.getHauteur(); i++) {
            for (int j = 0; j < terrain.getLargeur(); j++) {
                if (portee[i][j]) {
                    cellulesDansPortee.add(terrain.getCellule(i, j));
                }
            }
        }    
        return cellulesDansPortee;
    }


    // Crée une liste d'ennemis et y ajoute tous les ennemis en vie situés dans la portée de la tour, et
    // les trie en fonction de leur distance à la tour
    public List<Ennemi> getEnnemisDansPortee() {

        List<Ennemi> ennemisDansPortee = new ArrayList<>();
            
        List<Cellule> cellulesDansPortee = getCellulesDansPortee();
    
        for (Cellule cellule : cellulesDansPortee) {
            ennemisDansPortee.addAll(cellule.getListEnnemis());
        }
        ennemisDansPortee.removeIf(Ennemi::estElimine);
    
        ennemisDansPortee.sort(Comparator.comparingDouble(ennemi ->
                calculerDistance(this.coordinates, ennemi.getPosition())));
    
        return ennemisDansPortee;
    }
    

    // Implémente la logique d'attaque des tours : 
    // Si le joueur a perdu, arrête l'exécution du thread en cours
    // Parcourt la liste des ennemis grâce à un itérateur et implémente les dégâts infligés à l'ennemi
    // Si celui-ci est éliminé, traite son élimination
    public void attaquerEnnemisDansPortee(Joueur joueur, List<Ennemi> listEnnemis) { 
        if (joueur.getVies() == 0) {
            executor.shutdown();
            return;
        }

        Iterator<Ennemi> iterator = listEnnemis.iterator();

        while (iterator.hasNext()) {
            Ennemi cibleActuelle = iterator.next();

            jouerSonAttaque();
            cibleActuelle.subirDegats(degats);

            if (cibleActuelle.estElimine()) {
                traiterEnnemiElimine(cibleActuelle);
                iterator.remove();
            }
        }

    }


    // Traite l'élimination d'un ennemi : le supprime de la liste d'ennemis de la cellule, met à jour le contenu
    // s'il est le dernier de cette liste, et incrémente l'argent et le score du joueur
    private void traiterEnnemiElimine(Ennemi ennemi) {
        Cellule celluleEnnemi = terrain.getCellule((int) ennemi.getPosition().getX(), (int) ennemi.getPosition().getY());
        celluleEnnemi.getListEnnemis().remove(ennemi);

        if (celluleEnnemi.getListEnnemis().isEmpty()) {
            celluleEnnemi.setTypeContenu(typeContenu.CHEMIN);
        }

        int recompense = ennemi.getDegats() * 4;
        joueur.gagnerArgent(recompense);
        joueur.augmenterScore(20);
    }
      

    // Planifie des attaques périodiques contre les ennemis dans la portée de la tour
    public void attaquer(Joueur joueur) {
        // augmenter le multiplicateur pour allonger le délai entre les attaques
        executor.scheduleAtFixedRate(() -> attaquerEnnemisDansPortee(joueur, getEnnemisDansPortee()), 0, (long) (vitesseAttaque * 1000), TimeUnit.MILLISECONDS);
    }    
   

    // Augmente les dégats infligés par les tours en fonction du niveau de celle-ci
    public void ajusterDegatsEnFonctionDifficulte(int niv) {
        switch (niv) {
            case 1:
                break;
            case 2:
                this.degats *= 1.2; 
                break;
            case 3:
                this.degats *= 1.5; 
                break;
        }
    }

    // Calcule la distance entre deux coordonnées : utiliser pour calculer la distance entre la tour et l'ennemi
    private double calculerDistance(RealCoordinates position1, RealCoordinates position2) {
        double dx = position2.getX() - position1.getX();
        double dy = position2.getY() - position1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "Type de tour : " + this.type +
                "\nNiveau : " + this.niveau +
                "\nPosition : (" + this.coordinates.getX() + ", " + this.coordinates.getY() + ")" +
                "\nPrix : " + this.prix +
                "\nDégâts : " + this.degats +
                "\nVitesse d'attaque : " + this.vitesseAttaque;
    }


    public abstract Image getImage();

}
