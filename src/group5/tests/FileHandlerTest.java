package group5.tests;


import group5.backend.Coordinator;
import group5.backend.FileHandler;
import group5.common.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class FileHandlerTest {

    FileHandler handler;
    Coordinator coordinator;

    /**
     * Sets up basic tests.
     */
    @Before
    public void setup(){

        coordinator = new Coordinator();
        handler = new FileHandler("test.xml",coordinator);

    }

    /**
     * Tests whether the file handler takes in the expected input.
     */
    @Test
    public void testReadingObstacle() {
        ArrayList<Obstacle> testData = coordinator.getObstacles();
        ArrayList<Obstacle> expectedData = new ArrayList<Obstacle>();

        expectedData.add(new Obstacle(10d,"crate"));
        expectedData.add(new Obstacle(20d,"boulder"));
        expectedData.add(new Obstacle(50d,"Tower"));

        for (int i = 0; i < testData.size(); i++){
            assertEquals(expectedData.get(i).getHeight(),testData.get(i).getHeight(),0.1);
            assertEquals(expectedData.get(i).getName(),testData.get(i).getName());
        }
    }

    /**
     * Tests whether the file handler takes in the expected input for planes.
     */
    @Test
    public void testReadingPlanes(){
        ArrayList<Plane> testData = coordinator.getPlanes();
        ArrayList<Plane> expectedData = new ArrayList<Plane>();

        expectedData.add(new Plane("Boeing 747",50d,0.5));

        for (int i = 0; i < testData.size(); i++){
            Plane plane1 = expectedData.get(i);
            Plane plane2 = testData.get(i);

            assertEquals(plane1.getName(),plane2.getName());
            assertEquals(plane1.getBlastProtect(),plane2.getBlastProtect(),0.1);
            assertEquals(plane1.getRatio(),plane2.getBlastProtect(),0.1);
        }

    }


    /**
     * This tests that airports are correctly entered.
     */
    @Test
    public void testReadingAirports(){
        ArrayList<Airport> testData = coordinator.getAirports();
        ArrayList<Airport> expectedData = new ArrayList<Airport>();

        expectedData.add(new Airport("Heathrow"));
        expectedData.add(new Airport("Gatwick"));

        for (int i = 0; i < testData.size(); i++){
            Airport a1 = testData.get(i);
            Airport a2 = testData.get(i);

            assertEquals(a1.getName(),a2.getName());
        }
    }

    /**
     * This tests that runways are correctly entered for a single runway.
     */
    @Test
    public void testReadingRunways(){
        Airport a1 = new Airport("Heathrow");

        Runway r1 = new Runway('r',10,10d,10d,10d,10d,10d,10d,10d,"Runway 1",a1,5000);
        coordinator.addRunway(r1);
        Runway e1 = a1.getRunways().get(0);

        assertEquals(r1.getName(),e1.getName());
        assertEquals(r1.getStripEnd(),e1.getStripEnd(),0.1);
        assertEquals(r1.getTORA(),e1.getTORA(),0.1);
        assertEquals(r1.getTODA(),e1.getTODA(),0.1);
        assertEquals(r1.getBearing(),e1.getBearing(),0.1);
        assertEquals(r1.getLength(),e1.getLength(),0.1);

    }

    /**
     * This tests that calculations are correctly entered for a single calculation
     */
    @Test
    public void testReadingCalculations(){
        ArrayList<Calculation> c1 = coordinator.getCalculations();
        ArrayList<Calculation> c2 = new ArrayList<Calculation>();

        Runway r1 = new Runway('r',10,10d,10d,10d,10d,10d,10d,10d,"10r",null,5000);
        Plane p1 = new Plane("concord",30d,0.6);
        Obstacle o1 = new Obstacle(20d,"boulder");



        c2.add(new Calculation(r1,p1,o1,100d,100d));

        for (int i = 0; i < c1.size(); i++){
            Calculation calc1 = c1.get(i);
            Calculation calc2 = c2.get(i);

            assertEquals(calc1.getNewTORA(),calc2.getNewTORA(),0.1);
            assertEquals(calc1.getNewASDA(),calc2.getNewASDA(),0.1);
            assertEquals(calc1.getNewLDA(),calc2.getNewLDA(),0.1);
            assertEquals(calc1.getNewTODA(),calc2.getNewTODA(),0.1);

        }


    }
}
