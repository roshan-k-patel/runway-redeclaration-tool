package group5.common;

import java.util.ArrayList;

// Class used to store the data regarding what has been updated recently.
public class Updated {

    private ArrayList<Airport> airportsAdded;
    private ArrayList<Calculation> calculationsAdded;
    private ArrayList<Obstacle> obstaclesAdded;
    private ArrayList<Plane> planesAdded;
    private ArrayList<Runway> runwaysAdded;

    private ArrayList<Airport> airportsRemoved;
    private ArrayList<Calculation> calculationsRemoved;
    private ArrayList<Obstacle> obstaclesRemoved;
    private ArrayList<Plane> planesRemoved;
    private ArrayList<Runway> runwaysRemoved;


    public Updated(){
        this.airportsAdded = new ArrayList<>();
        this.airportsRemoved = new ArrayList<>();
        this.calculationsAdded = new ArrayList<>();
        this.calculationsRemoved = new ArrayList<>();
        this.obstaclesAdded = new ArrayList<>();
        this.obstaclesRemoved = new ArrayList<>();
        this.planesAdded = new ArrayList<>();
        this.planesRemoved = new ArrayList<>();
        this.runwaysAdded = new ArrayList<>();
        this.runwaysRemoved = new ArrayList<>();
    }

    public ArrayList<Airport> getAirportsAdded() {
        return airportsAdded;
    }

    public void setAirportsAdded(ArrayList<Airport> airportsAdded) {
        this.airportsAdded = airportsAdded;
    }

    public ArrayList<Calculation> getCalculationsAdded() {
        return calculationsAdded;
    }

    public void setCalculationsAdded(ArrayList<Calculation> calculationsAdded) {
        this.calculationsAdded = calculationsAdded;
    }

    public ArrayList<Obstacle> getObstaclesAdded() {
        return obstaclesAdded;
    }

    public void setObstaclesAdded(ArrayList<Obstacle> obstaclesAdded) {
        this.obstaclesAdded = obstaclesAdded;
    }

    public ArrayList<Plane> getPlanesAdded() {
        return planesAdded;
    }

    public void setPlanesAdded(ArrayList<Plane> planesAdded) {
        this.planesAdded = planesAdded;
    }

    public ArrayList<Runway> getRunwaysAdded() {
        return runwaysAdded;
    }

    public void setRunwaysAdded(ArrayList<Runway> runwaysAdded) {
        this.runwaysAdded = runwaysAdded;
    }

    public ArrayList<Airport> getAirportsRemoved() {
        return airportsRemoved;
    }

    public void setAirportsRemoved(ArrayList<Airport> airportsRemoved) {
        this.airportsRemoved = airportsRemoved;
    }

    public ArrayList<Calculation> getCalculationsRemoved() {
        return calculationsRemoved;
    }

    public void setCalculationsRemoved(ArrayList<Calculation> calculationsRemoved) {
        this.calculationsRemoved = calculationsRemoved;
    }

    public ArrayList<Obstacle> getObstaclesRemoved() {
        return obstaclesRemoved;
    }

    public void setObstaclesRemoved(ArrayList<Obstacle> obstaclesRemoved) {
        this.obstaclesRemoved = obstaclesRemoved;
    }

    public ArrayList<Plane> getPlanesRemoved() {
        return planesRemoved;
    }

    public void setPlanesRemoved(ArrayList<Plane> planesRemoved) {
        this.planesRemoved = planesRemoved;
    }

    public ArrayList<Runway> getRunwaysRemoved() {
        return runwaysRemoved;
    }

    public void setRunwaysRemoved(ArrayList<Runway> runwaysRemoved) {
        this.runwaysRemoved = runwaysRemoved;
    }

    public void addAdded(Airport airport) {
        this.airportsAdded.add(airport);
    }

    public void addAdded(Runway runway) {
        this.runwaysAdded.add(runway);
    }

    public void addAdded(Plane plane) {
        this.planesAdded.add(plane);
    }

    public void addAdded(Obstacle obstacle) {
       this.obstaclesAdded.add(obstacle);
    }

    public void addAdded(Calculation plane) {
        this.calculationsAdded.add(plane);
    }

    public void addRemoved(Airport airport) {
        this.airportsRemoved.add(airport);
    }

    public void addRemoved(Runway runway) {
        this.runwaysRemoved.add(runway);
    }

    public void addRemoved(Plane plane) {
        this.planesRemoved.add(plane);
    }

    public void addRemoved(Obstacle obstacle) {
        this.obstaclesRemoved.add(obstacle);
    }

    public void addRemoved(Calculation calculation) {
        this.calculationsRemoved.add(calculation);
    }

}
