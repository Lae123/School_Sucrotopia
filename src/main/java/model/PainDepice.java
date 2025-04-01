package main.java.model;

import java.awt.Image;
import javax.swing.ImageIcon;

import main.java.geometry.RealCoordinates;

public class PainDepice extends Ennemi {

    public PainDepice(RealCoordinates coordinates) {

        super("PAIN_DEPICE", coordinates, 1, 18, 4, 1);
    }

    @Override
    public Image getImage(){
        String imagePath = "bin/ressources/sprites/Monstres/painDepice.png"; 

        if (getPointsDeVie() > this.getPointsDeVie() / 2) {
            imagePath = "bin/ressources/sprites/Monstres/painDepice.png"; 
        }else{
            imagePath = "bin/ressources/sprites/Monstres/pain_Depice_attaque.png";
        }

        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
       
    }

}