package main.java.model;

import main.java.geometry.RealCoordinates;
import java.util.List;
import java.util.ArrayList;

public class Cellule {
    private RealCoordinates coordinates;
    private typeContenu contenu;
    private Tours tour;
    private List<Ennemi> listEnnemis;

    public Cellule() {
        this.contenu = typeContenu.VIDE;
        this.tour = null;
        this.listEnnemis = new ArrayList<Ennemi>();
    }

    public RealCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coord) {
        this.coordinates = convertirCoordonnees(coord);
    }

    private RealCoordinates convertirCoordonnees(String coord) {
        char colonneChar = coord.charAt(0);
        int ligne = Character.getNumericValue(coord.charAt(1));

        double x = colonneChar - 'A' + 1;
        double y = ligne;

        return new RealCoordinates(x, y);
    }

    public void setTypeContenu(typeContenu type) {
        this.contenu = type;
    }

    public void setCoordinates(RealCoordinates a) {
        this.coordinates.setCoordinates(a);
    }

    public typeContenu getTypeContenu() {
        return contenu;
    }

    public Tours getTour() {
        return tour;
    }

    public void setTour(Tours tour) {
        this.tour = tour;
    }

    public List<Ennemi> getListEnnemis() {
        return listEnnemis;
    }

    public void addEnemy(Ennemi enemy) {
        listEnnemis.add(enemy);
    }

    public void removeEnemy(Terrain terrain, Ennemi enemy) {
        listEnnemis.remove(enemy);
    }

}
