package main.java.model;

import main.java.geometry.RealCoordinates;

import java.awt.Image;
import java.io.File;
import javax.sound.sampled.*;

public abstract class Ennemi {
    private final String type;
    private RealCoordinates position;
    private int pointsDeVie;
    private int degats;
    private double vitesse;
    private boolean murAtteint;
    private Clip clipEnCours;

    public Ennemi(String type, RealCoordinates position, int niveau, int pointsDeVie, int degats, double vitesse) {
        this.type = type;
        this.position = position;
        this.pointsDeVie = pointsDeVie;
        this.degats = degats;
        this.vitesse = vitesse;
    }

    public String getType() {
        return type;
    }

    public RealCoordinates getPosition() {
        return position;
    }

    public void setPosition(RealCoordinates position) {
        this.position = position;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
    }

    public int getDegats() {
        return degats;
    }

    public void setDegats(int degats) {
        this.degats = degats;
    }

    public double getVitesse() {
        return vitesse;
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
    }

    public boolean murAtteint() {
        return murAtteint;
    }

    public void setMurAtteint() {
        this.murAtteint = true;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", Position: " + position.toString();
    }


    // Implémente le déplacement de l'ennemi d'une cellule à une autre et vérifie s'il a atteint le mur
    public void deplacer(Terrain terrain) {
        if (estElimine()) {
            return;
        }

        RealCoordinates position = this.getPosition();
        int x = (int) position.getX();
        int y = (int) position.getY();
    
        int nouvellePosX = x;
        int nouvellePosY = y + 1;
    
        int lastCellY = terrain.getLargeur() - 4;
    
        if (y == lastCellY) {
            return;
        }

        if (terrain.estPositionValide(x, nouvellePosY) &&
                (terrain.getCellule(x, nouvellePosY).getTypeContenu() == typeContenu.CHEMIN ||
                        terrain.getCellule(x, nouvellePosY).getTypeContenu() == typeContenu.ENNEMI)) {

            Cellule currentCell = terrain.getCellule(x, y);
            Cellule nextCell = terrain.getCellule(nouvellePosX, nouvellePosY);
    
            currentCell.removeEnemy(terrain, this);
            currentCell.setTypeContenu(typeContenu.CHEMIN);
    
            this.setPosition(new RealCoordinates(nouvellePosX, nouvellePosY));
    
            nextCell.addEnemy(this);
            nextCell.setTypeContenu(typeContenu.ENNEMI);
    
            if (nouvellePosY == lastCellY) {
                setMurAtteint();
            }
        }
    }
    

    // Implémente les dégâts infligés par l'ennemi
    public void attaquer(Joueur joueur) {
        int res = joueur.getVies() - getDegats();
        joueur.setVies(res);

        if (joueur.getVies() < 0) {
            joueur.setVies(0);
        }
        System.out.println("Vie : " + joueur.getVies());
        jouerSonAttaque();
    }

    public void subirDegats(int degatsInfliges) {
        this.pointsDeVie -= degatsInfliges;
        jouerSonBlesse();
        if (pointsDeVie < 0) {
            this.pointsDeVie = 0;
            jouerSonMort();
        }
    }

    public boolean estElimine() {
        return this.pointsDeVie <= 0;
    }



    /////////////////////////////////////////
    //  Implémentation des effets sonores  //
    /////////////////////////////////////////

    private void jouerSon(String sonFilePath) {
        try {
            File soundFile = new File(sonFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioInputStream.getFormat();
    
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
    
            int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
            byte[] buffer = new byte[bufferSize];
    
            int bytesRead;
            while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);
            }
    
            line.drain();
            line.close();
            audioInputStream.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void arreterSonEnCours() {
        if (clipEnCours != null && clipEnCours.isRunning()) {
            clipEnCours.stop();
        }
    }

    private void jouerSonAttaque() {
        arreterSonEnCours();
        jouerSon("bin/ressources/sons/rigole.wav");
    }

    private void jouerSonBlesse() {
        arreterSonEnCours();
        jouerSon("bin/ressources/sons/hurt.wav");
    }

    private void jouerSonMort() {
        arreterSonEnCours();
        jouerSon("bin/ressources/sons/meurt.wav");
    }

    public abstract Image getImage();

}
