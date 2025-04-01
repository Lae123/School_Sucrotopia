package main.java.model;

import java.awt.Image;
import javax.swing.ImageIcon;

import main.java.geometry.RealCoordinates;

public class ChocolatMutant extends Ennemi {

    public ChocolatMutant(RealCoordinates coordinates) {

        super("CHOCOLAT_MUTANT", coordinates, 1, 20, 6, 1);
    }


    @Override
    public Image getImage(){
       String imagePath = "bin/ressources/sprites/Monstres/chocolat.png"; 
        
        if (getPointsDeVie() > this.getPointsDeVie() / 2) {
            imagePath = "bin/ressources/sprites/Monstres/chocolat.png"; 
        }else{
            imagePath = "bin/ressources/sprites/Monstres/chocolat_attaque.png";
        }

        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
       
    }
}