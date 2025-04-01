package main.java.model;

import java.awt.Image;
import javax.swing.ImageIcon;

import main.java.geometry.RealCoordinates;

public class LanceTopping extends Tours {

    public LanceTopping(Joueur joueur, Terrain terrain, RealCoordinates coordinates, String difficulte) {
    
        super(joueur, terrain, "NOUNOURS_GUMMY", coordinates, 20, 6, 1.2, difficulte);
    }
    
    @Override
    public void ameliorerTour() {
        this.degats += 5; 
        this.vitesseAttaque -= 0.2;
        this.prix += 10; 
        this.niveau ++;
        ameliorerPortee();
    }

    @Override
    public Image getImage() {
        String imagePath = "bin/ressources/sprites/Tiles/Chemin.png";
        if (this.niveau == 1){
            imagePath = "bin/ressources/sprites/Tours/tourNiv1-overlay_2.png"; 
        } else if (this.niveau == 2){
            imagePath = "bin/ressources/sprites/Tours/tourNiv2-overlay_2.png";
        } else if (this.niveau == 3){
            imagePath = "bin/ressources/sprites/Tours/tourNiv3-overlay_2.png";
        }

       
        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
          
    }

}
