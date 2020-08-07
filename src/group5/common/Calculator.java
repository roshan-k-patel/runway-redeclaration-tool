package group5.common;

import java.util.ArrayList;

/**
 * Interface for the Coordinator
 */
public interface Calculator {

    /**
     *
     * @return Returns the list of airports currently stored in the calculator
     */
    ArrayList<Airport> getAirports();

    /**
     *
     * @return Returns the list of obstacles currently stored in the calculator
     */
    ArrayList<Obstacle> getObstacles();

    /**
     *
     * @return Returns the list of calculations currently stored in the calculator
     */
    ArrayList<Calculation> getCalculations();

    /**
     *
     * @return Returns the list of planes currently stored in the calculator
     */
    ArrayList<Plane> getPlanes();

    /**
     * Saves the current lists of planes, obstacles, runways and airports to a text file.
     * @param filename Takes in the name to save to.
     * @return Returns true if the save was successful, and false if it failed.
     */
    boolean save(String filename);

    /**
     * Saves the current lists of planes, obstacles, runways and airports to a text file.
     * @return Returns true if the save was successful, and false if it failed.
     */
    boolean save();

    /**
     *
     * @param plane The plane that is being added to the calculator.
     * @return Returns true if the plane was sucessfully added, and false if it failed.
     */
    boolean addPlane(Plane plane);

    /**
     *
     * @param obstacle
     * @return Returns true if the obstacle was successfully added, and false if it failed.
     */
    boolean addObstacle(Obstacle obstacle);

    /**
     *
     * @param airport
     * @return Returns true if the airport was successfully added, and false if it failed.
     */
    boolean addAirport(Airport airport);

    /**
     * Example use would be addRunway(2,runwayObject), where runwayObject has type runway and 2 is the ID of the airport that the runway is associated with.
     * @param runway Parameter of runway which is to be added to the calculator.
     * @return Returns 0 if runway was sucessfully added, 1 if the ID of the airport was not contained on the calculator, and 2 if failed for other reasons..
     */
    int addRunway(Runway runway);

    /**
     *
     * @return Returns true if the file was successfully loaded, and false if it failed.
     */
    boolean load(String fileName);

    /**
     *
     * @param calculation Add a calculation.
     * @return Returns true if the calculation was successfully added, and false if it failed.
     */
    boolean addCalculation(Calculation calculation);

    /**
     *
     * @param p Remove a specified plane.
     * @return Returns true if the removal was successful, false if it failed.
     */
    boolean removePlane(Plane p);

    /**
     *
     * @param o Remove a specified plane.
     * @return Returns true if the removal was successful, false if it failed.
     */
    boolean removeObstacle(Obstacle o);

    /**
     *
     * @param a Remove a specified plane.
     * @return Returns true if the removal was successful, false if it failed.
     */
    boolean removeAirport(Airport a);

    /**
     *
     * @param c Remove a specified plane.
     * @return Returns true if the removal was successful, false if it failed.
     */
    boolean removeCalculation(Calculation c);

    /**
     *
     * @param r Remove a specified plane.
     * @param id Specifies which airport which the runway is owned by
     * @return Returns true if the removal was successful, false if it failed.
     */
    boolean removeRunway(int id, Runway r);

    /**
     *
     * @return This returns a list of all of the objects added or removed from the
     */
    Updated updated();

	boolean addLog(CalcLog l);

	ArrayList<CalcLog> getLogs();

	ArrayList<Runway> getRunways();


}