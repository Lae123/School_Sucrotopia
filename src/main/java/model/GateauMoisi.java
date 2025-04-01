package main.java.model;

import java.awt.Image;
import javax.swing.ImageIcon;

import main.java.geometry.RealCoordinates;

public class GateauMoisi extends Ennemi {

    public GateauMoisi(RealCoordinates coordinates) {
  
        super("GATEAU_MOISI", coordinates, 1, 15, 2, 1);
    }

    @Override
    public Image getImage(){
       String imagePath = "bin/ressources/sprites/Monstres/cupcake.png"; 
       
        if (getPointsDeVie() > this.getPointsDeVie() / 2) {
            imagePath = "bin/ressources/sprites/Monstres/cupcake.png"; 
        }else{
            imagePath = "bin/ressources/sprites/Monstres/cupcake_attaqué.png";
        }

        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
       
    }

}