package group5.tests;

import group5.backend.Coordinator;
import group5.common.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class should be in the tests folder but I couldn't figure out the class path stuff so I left it here - Luke
 */
public class CoordinatorTest {

    private Calculator calc;
    private ArrayList<Airport> airports;
    private ArrayList<Runway> runways;
    private ArrayList<Plane> planes;
    private ArrayList<Obstacle> obstacles;

    /**
     * Setups basic tests
     */
    @Before
    public void setup(){
        airports = new ArrayList<>();
        runways = new ArrayList<>();
        planes = new ArrayList<>();
        obstacles = new ArrayList<>();
        calc = new Coordinator();

        Airport airport1 = new Airport("Heathrow");

        /* Naming convention of runways is:
        * runwayXY
        * X = airport number
        * Y = number of runway in airport
        *
        * e.g. runway34 belongs to the third airport, and is the fourth runway in that airport.
        *
        * Runways runway1(1+2+3+4) are from the given calculation examples. They are on airport1.
        */
        Runway runway11 = new Runway('R', 90, 3660d,3660,3660, 3353, 307d, null, null,"runway11",airport1,5000);

        Runway runway12 = new Runway('L', 270,"runway12");
        Runway runway13 = new Runway('L', 90,"runway13");
        Runway runway14 = new Runway('R', 270,"runway14");

        airport1.addRunway(runway11);
        airport1.addRunway(runway12);
        airport1.addRunway(runway13);
        airport1.addRunway(runway14);

        Plane plane1 = new Plane("Steve");

        Obstacle obstacle1 = new Obstacle(25d,"sc2");
        Obstacle obstacle2 = new Obstacle(15d, "sc3");

        planes.add(plane1);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);

        airports.add(airport1);

        runways.add(runway11);
        runways.add(runway12);
        runways.add(runway13);
        runways.add(runway14);

        // Setting up scenario 1:
    }

    /**
     * Checking that an airport added is stored in the calculator.
     */
    @Test
    public void testAddAirport() {

        Airport firstVal = airports.get(0);

        calc.addAirport(firstVal);

        Airport secondVal = calc.getAirports().get(0);
        assertEquals(firstVal.getID(),secondVal.getID());

    }

    /**
     * Testing the addRunway() method
     */
    @Test
    public void testAddRunway() {
        Airport airport = new Airport("Airport 1");
        Runway runway = new Runway('r',5,5d,5d,5d,5d,5d,5d,5d,"Test Runway",airport,5000);

        // Add data to the calc
        calc.addAirport(airport);
        calc.addRunway(runway);

        // Test if the calc stores it correctly
        assertTrue(calc.getAirports().get(0).getRunways().contains(runway));
    }

    /**
     * Testing the load method
     */
    @Test
    public void testLoad() {
        calc.load("test.xml");

        assertEquals(calc.getObstacles().size(),3);
        assertEquals(calc.getPlanes().size(),2);
        assertEquals(calc.getAirports().size(),2);

    }



    /**
     * Tests addPlane method on a coordinator.
     */
    @Test
    public void testAddPlane() {
        Plane plane = new Plane("Plane 1");

        calc.addPlane(plane);

        assertEquals("Plane 1",(calc.getPlanes().get(calc.getPlanes().size() - 1)).getName());
    }

    /**
     * Tests addObstacle method on a coordinator.
     */
    @Test
    public void testAddObstacle() {
        Obstacle obstacle = new Obstacle(5d,"Obstacle 1");

        calc.addObstacle(obstacle);

        assertEquals("Obstacle 1",calc.getObstacles().get(calc.getObstacles().size() - 1).getName());
        assertEquals(5d,calc.getObstacles().get(calc.getObstacles().size() - 1).getHeight(),0.1);
    }


    /**
     * Tests the save method on a coordinator.
     */
    @Test
    public void testSave() {

        // Build a calculator and save the contents to a file
        Calculator calc2 = new Coordinator();
        calc2.addObstacle(new Obstacle(5d,"Obstacle 1"));
        calc2.addPlane(new Plane("Plane 1"));
        calc2.addAirport(new Airport("Airport 1"));
        calc2.addRunway(new Runway('r',5,5d,5d,5d,5d,5d,5d,5d,"Runway 1",calc2.getAirports().get(0),5000));
        calc2.addCalculation(new Calculation(calc2.getAirports().get(0).getRunways().get(0),calc2.getPlanes().get(0),calc2.getObstacles().get(0),5d,5d));

        calc2.addObstacle(new Obstacle(50d,"Obstacle 2"));
        calc2.addPlane(new Plane("Plane 2"));
        calc2.addAirport(new Airport("Airport 2"));
        calc2.addRunway(new Runway('l',16,50d,50d,50d,50d,50d,50d,50d,"Runway 2",calc2.getAirports().get(0),5000));
        calc2.addCalculation(new Calculation(calc2.getAirports().get(0).getRunways().get(0),calc2.getPlanes().get(0),calc2.getObstacles().get(0),50d,50d));

        calc2.addObstacle(new Obstacle(500d,"Obstacle 3"));
        calc2.addPlane(new Plane("Plane 3"));
        calc2.addAirport(new Airport("Airport 3"));
        calc2.addRunway(new Runway('r',9,500d,500d,500d,500d,500d,500d,500d,"Runway 3",calc2.getAirports().get(0),5000));
        calc2.addCalculation(new Calculation(calc2.getAirports().get(0).getRunways().get(0),calc2.getPlanes().get(0),calc2.getObstacles().get(0),5d,5d));

        calc2.save("testingXml.xml");

        // Build a new calculator and test to see if the calculator is the same as the old one.
        Calculator calc3 = new Coordinator();
        calc3.load("testingXml.xml") ;

       for (int i = 0; i < calc2.getObstacles().size(); i++){
            // Check to see if the obstacles are changed
            assertEquals(calc3.getObstacles().get(i).getName(), calc2.getObstacles().get(i).getName());
            assertEquals(calc3.getObstacles().get(i).getHeight(), calc2.getObstacles().get(i).getHeight());

            // Check to see if planes are the same
            assertEquals(calc3.getPlanes().get(i).getName(), calc2.getPlanes().get(i).getName());

            // Check to see if airports are the same
            assertEquals(calc3.getAirports().get(i).getName(), calc2.getAirports().get(i).getName());

            // Check to see if calculations are the same
            assertEquals(calc3.getCalculations().get(i).getNewTODA(), calc2.getCalculations().get(i).getNewTODA(), 0.1);
            assertEquals(calc3.getCalculations().get(i).getNewTORA(), calc2.getCalculations().get(i).getNewTORA(), 0.1);
            assertEquals(calc3.getCalculations().get(i).getNewLDA(), calc2.getCalculations().get(i).getNewLDA(), 0.1);
            assertEquals(calc3.getCalculations().get(i).getNewASDA(), calc2.getCalculations().get(i).getNewASDA(), 0.1);
        }
    }

    /**
     *Tests the update method.
     */
    @Test
    public void testAddedUpdated() {

        calc.updated(); // Flushes the updated() method

        // Test if a recently added airport is added to the updated()
        calc.addAirport(new Airport("Airport 1"));
        assertEquals("Airport 1", calc.updated().getAirportsAdded().get(0).getName());

        // Test if a recently added plane is added to the updated()
        calc.addPlane(new Plane("Plane 1"));
        assertEquals("Plane 1", calc.updated().getPlanesAdded().get(0).getName());

        // Test if a recently added obstacle is added to the updated()
        calc.addObstacle(new Obstacle(10d,"Obstacle 1"));
        assertEquals("Obstacle 1", calc.updated().getObstaclesAdded().get(0).getName());

        // Test if a recently added runway is added to the updated()
        calc.addRunway(new Runway('r',5,5d,5d,5d,5d,5d,5d,5d,"r5",calc.getAirports().get(0),5000));
        assertEquals("05R", calc.updated().getRunwaysAdded().get(0).getName());


        // Test if a recently added calculation is added to the updated()
        calc.addCalculation(new Calculation(calc.getAirports().get(0).getRunways().get(0),calc.getPlanes().get(0),calc.getObstacles().get(0),5d,5d));
        assertEquals(calc.getCalculations().size(),1);

    }

    /**
     * Tests that a plane has been removed.
     */
    @Test
    public void testRemovePlane() {
        Calculator calc2 = new Coordinator();

        Plane plane = new Plane("Plane ");

        calc2.addPlane(plane);
        calc2.removePlane(plane);

        assertEquals(0,calc.getPlanes().size());

    }

    /**
     * Tests that a calculation has been removed.
     */
    @Test
    public void testRemoveCalculation() {
        Calculator calc2 = new Coordinator();

        calc2.addAirport(new Airport("Airport 1"));
        calc2.addPlane(new Plane("Plane 1"));
        calc2.addObstacle(new Obstacle(10d,"Obstacle 1"));
        calc2.addRunway(new Runway('l',5,5d,5d,5d,5d,5d,5d,5d,"Runway1",calc2.getAirports().get(0),5000));

        calc2.addCalculation(new Calculation(calc2.getAirports().get(0).getRunways().get(0),calc2.getPlanes().get(0),calc2.getObstacles().get(0),5d,5d));

        calc2.removeCalculation(calc2.getCalculations().get(0));

        assertEquals(0,calc2.getCalculations().size());

    }

    /**
     * Tests that a runway can be removed
     */
    @Test
    public void testRemoveRunway() {
        Calculator calc2 = new Coordinator();

        Airport airport  = new Airport("Airport 1");
        Runway runway = new Runway('l',5,5d,5d,5d,5d,5d,5d,5d,"Runway1",airport,5000);

        calc2.addAirport(airport);

        calc2.addRunway(runway);
        calc2.removeRunway(airport.getID(),runway);

        assertEquals(0,calc2.getAirports().get(0).getRunways().size());
    }

    /**
     * Tests that an airport can be removed
     */
    public void testRemoveAirport() {
        Calculator calc2 = new Coordinator();

        Airport airport  = new Airport("Airport 1");

        calc2.addAirport(airport);
        calc2.removeAirport(airport);

        assertEquals(0,calc.getAirports().get(0));

    }

    /**
     * Tests that an obstacle can be removed.
     */
    @Test
    public void testRemoveObstacle() {
        Calculator calc2 = new Coordinator();

        Obstacle obstacle = new Obstacle(5d,"Obstacle 1");

        calc2.addObstacle(obstacle);
        calc2.removeObstacle(obstacle);

        assertEquals(0,calc.getObstacles().size());
    }
}
