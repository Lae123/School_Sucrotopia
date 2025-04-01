package main.java.model;

import java.awt.Image;
import javax.swing.ImageIcon;

import main.java.geometry.RealCoordinates;

public class GeleeEnragee extends Ennemi {

    public GeleeEnragee(RealCoordinates coordinates) {
 
        super("GELEE_ENRAGEE", coordinates, 1, 15, 3, 1);
    }

    @Override
    public Image getImage(){
       String imagePath = "bin/ressources/sprites/Monstres/ficelle.png";
       
        if (getPointsDeVie() > this.getPointsDeVie() / 2) {
            imagePath = "bin/ressources/sprites/Monstres/ficelle.png"; 
        }else{
            imagePath = "bin/ressources/sprites/Monstres/ficelle_attaque.png";
        }

        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
       
    }

    

}