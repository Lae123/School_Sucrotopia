package main.java.model;

import java.awt.Image;
import javax.swing.ImageIcon;

import main.java.geometry.RealCoordinates;

public class MarshmallowEnflamme extends Tours {

    public MarshmallowEnflamme(Joueur joueur, Terrain terrain, RealCoordinates coordinates, String diff) {

        super(joueur, terrain, "MARSHMALLOW_ENFLAMME", coordinates, 80, 12, 1.2, diff);
    }    

    @Override
    public void ameliorerTour() {
        this.degats += 5; 
        this.vitesseAttaque -= 0.2;
        this.prix += 20; 
        this.niveau ++;
        ameliorerPortee();
    }

    @Override
    public Image getImage() {
        String imagePath = "bin/ressources/sprites/Tiles/Chemin.png";
        if (this.niveau == 1){
            imagePath = "bin/ressources/sprites/Tours/tourNiv1-overlay_3.png"; 
        } else if (this.niveau == 2){
            imagePath = "bin/ressources/sprites/Tours/tourNiv2-overlay_3.png";
        } else if (this.niveau == 3){
            imagePath = "bin/ressources/sprites/Tours/tourNiv3-overlay_3.png";
        }

       
        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
       
    }


}

