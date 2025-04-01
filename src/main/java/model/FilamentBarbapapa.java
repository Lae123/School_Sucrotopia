package main.java.model;

import java.awt.Image;
import javax.swing.ImageIcon;

import main.java.geometry.RealCoordinates;

public class FilamentBarbapapa extends Tours {

    public FilamentBarbapapa(Joueur joueur, Terrain terrain, RealCoordinates coordinates, String diff) {
 
        super(joueur, terrain, "FILAMENT_BARBAPAPA", coordinates, 60, 10, 1.2, diff);
    }

    @Override
    public void ameliorerTour() {
        this.degats += 5; 
        this.vitesseAttaque -= 0.2;
        this.prix += 15; 
        this.niveau ++;
        ameliorerPortee();
    }

    @Override
    public Image getImage() {
        String imagePath = "bin/ressources/sprites/Tiles/Chemin.png";
        if (this.niveau == 1){
            imagePath = "bin/ressources/sprites/Tours/tourNiv1-overlay_1.png"; 
        } else if (this.niveau == 2){
            imagePath = "bin/ressources/sprites/Tours/tourNiv2-overlay_1.png";
        } else if (this.niveau == 3){
            imagePath = "bin/ressources/sprites/Tours/tourNiv3-overlay_1.png";
        }
       
        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
       
    }
}

