package main.java.geometry;

public class RealCoordinates {
    private double x;
    private double y;

    public RealCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setCoordinates(RealCoordinates a){
        this.x = a.x;
        this.y = a.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }    

}