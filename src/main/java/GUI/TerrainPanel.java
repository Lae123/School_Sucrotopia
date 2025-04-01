
package main.java.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.java.model.*;

public class TerrainPanel extends JPanel {

        private Terrain terrain;
        private TerrainGui terrainGui;
        private Jeu jeu;

    public TerrainPanel(Terrain terrain, TerrainGui a, Jeu jeu) {
        this.terrain = terrain;
        this.terrainGui = a;
        this.jeu = jeu;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellSize = 50;
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                Cellule cellule = terrain.getCellule(y, x);

                if (cellule.getTypeContenu() == typeContenu.EMPLACEMENT_TOUR) {
                    terrainGui.ameliorerTourDepuisClic(x, y);
                } else {
                    terrainGui.placerTourDepuisClic(x, y);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (terrain != null) {
            afficherTerrain(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int cellSize = 50;
        int width = terrain.getLargeur() * cellSize;
        int height = terrain.getHauteur() * cellSize;
        System.out.println("Cell Size: " + cellSize);
        System.out.println("Width: " + width + ", Height: " + height);
        return new Dimension(width, height);
    }

    // Affiche le terrain à partir des ressources du jeu
    private void afficherTerrain(Graphics g) {
        int cellSize = 50;
    
        for (int i = 0; i < terrain.getHauteur(); i++) {
            for (int j = 0; j < terrain.getLargeur(); j++) {
                int x = j * cellSize;
                int y = i * cellSize;
    
                Cellule cellule = terrain.getCellule(i, j);
                ImageIcon murImageIcon;

                if (cellule.getTypeContenu() == typeContenu.MUR && jeu.getJoueur().getVies() <= jeu.getJoueur().getInitialHealth() / 2) {
                    murImageIcon = new ImageIcon("bin/ressources/sprites/Tiles/mur_blesse.png");
                } else {
                    murImageIcon = new ImageIcon("bin/ressources/sprites/Tiles/wall.png");
                }


                Image murImage = murImageIcon.getImage();
    
                // Dessine l'image correspondante pour chaque type de contenu
                ImageIcon terrainImageIcon = getImage(cellule, i, j);
                Image terrainImage = terrainImageIcon.getImage();
    
                Image defaultImage = getDefaultIcon(i, j).getImage();
                g.drawImage(defaultImage, x, y, cellSize, cellSize, this);
    
                if (cellule.getTypeContenu() == typeContenu.ENNEMI) {
                    afficherEnnemis(g, cellule, x, y, cellSize);
                } else if (cellule.getTypeContenu() == typeContenu.MUR) {
                    g.drawImage(murImage, x, y, cellSize, cellSize, this);
                } else if (cellule.getTypeContenu() == typeContenu.EMPLACEMENT_TOUR) {
                    afficherTours(g, cellule, x, y, cellSize);
                } else {
                    g.drawImage(terrainImage, x, y, cellSize, cellSize, this);
                }
            }
        }
    }
    
    
    private ImageIcon getDefaultIcon(int row, int col) {
        if (terrain.getCellule(row, col).getTypeContenu() == typeContenu.EMPLACEMENT_TOUR) {
            return new ImageIcon("bin/ressources/sprites/Tiles/choco.png");
        } else {
            return new ImageIcon("bin/ressources/sprites/Tiles/all.png");
        }
    }
    
    
    private void afficherEnnemis(Graphics g, Cellule cellule, int x, int y, int cellSize) {
        int ennemiCount = cellule.getListEnnemis().size();
        int horizontalOffset = cellSize / 4;
    
        for (int k = 0; k < ennemiCount; k++) {
            Ennemi ennemi = cellule.getListEnnemis().get(k);
            int ennemiX = (int) ennemi.getPosition().getY();
            int ennemiY = (int) ennemi.getPosition().getX();
    
            int drawX = ennemiX * cellSize + k * horizontalOffset;
            int drawY = ennemiY * cellSize;

            Image ennemiImage = ennemi.getImage();
    
            g.drawImage(ennemiImage, drawX, drawY, cellSize, cellSize, this);
        }
    }
    
    private void afficherTours(Graphics g, Cellule cellule, int x, int y, int cellSize) {
        Image tourImage = cellule.getTour().getImage();
    
        g.drawImage(tourImage, x, y, cellSize, cellSize, this);
    }
    
    
    private ImageIcon getImage(Cellule cellule, int row, int col) {

        switch (cellule.getTypeContenu()) {
            case CHEMIN:
                return getDefaultIcon(row, col);

            case MUR:
                int playerHealth = jeu.getJoueur().getVies();
                int playerHealthBlesse =  jeu.getJoueur().getVies() / 2;
                ImageIcon murImageIcon;

                if (playerHealth <= playerHealthBlesse && cellule.getTypeContenu() == typeContenu.MUR) {
                    murImageIcon = new ImageIcon("bin/ressources/sprites/Tiles/mur_blesse.png");
                } else {
                    murImageIcon = new ImageIcon("bin/ressources/sprites/Tiles/wall.png");
                }

                return murImageIcon;
            case VIDE:
                return new ImageIcon("bin/ressources/sprites/Tiles/choco.png");

            default:
                return new ImageIcon("bin/ressources/sprites/Tiles/all.png");
        }
        
    }
    

}
