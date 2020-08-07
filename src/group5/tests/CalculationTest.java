package group5.tests;

import group5.backend.Coordinator;
import group5.common.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class CalculationTest {

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
        
        Runway runwayb = new Runway('L', 90, 3902d, 3902d, 3902d, 3595d, 306d, null, null, "behind", airport1, 5000);
        
        airport1.addRunway(runway11);
        airport1.addRunway(runway12);
        airport1.addRunway(runway13);
        airport1.addRunway(runway14);
        airport1.addRunway(runwayb);

        Plane plane1 = new Plane("Steve");

        Obstacle obstacle1 = new Obstacle(25d,"sc2");
        Obstacle obstacle2 = new Obstacle(15d, "sc3");
        Obstacle obstacleb = new Obstacle(12d, "b");

        planes.add(plane1);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacleb);

        airports.add(airport1);

        runways.add(runway11);
        runways.add(runway12);
        runways.add(runway13);
        runways.add(runway14);
        runways.add(runwayb);

        // Setting up scenario 1:
    }

    /**
     * Testing the Calculate class to see if it correctly calculates correct new runway distances.
     */
    @Test
    public void testCalculate() {
        double[] actualValues = new double[]{1850d,1850d,1850d,2553d};
        Calculation calculation = new Calculation(runways.get(0),planes.get(0),obstacles.get(0),2853d,20);
        double[] newValues = new double[]{calculation.getNewTORA(),calculation.getNewTODA(),calculation.getNewASDA(),calculation.getNewLDA()};

        assertTrue(Arrays.equals(actualValues,newValues));

        actualValues = new double[]{2903d,2903d,2903d,2393d};
        calculation = new Calculation(runways.get(0),planes.get(0),obstacles.get(1), 150d, 60d);
        newValues = new double[]{calculation.getNewTORA(),calculation.getNewTODA(),calculation.getNewASDA(),calculation.getNewLDA()};

        assertTrue(Arrays.equals(actualValues,newValues));
        
        actualValues = new double[]{3346d, 3346d ,3346d, 2985d};
        calculation = new Calculation(runways.get(4),planes.get(0),obstacles.get(2), -50, 0d);
        newValues = new double[]{calculation.getNewTORA(), calculation.getNewTODA(), calculation.getNewASDA(), calculation.getNewLDA()};
        
        assertTrue(Arrays.equals(actualValues,newValues));
    }

    /**
     * Testing the logging in the calculate class.
     */
    @Test
    public void testLogging() {
        Calculation calculation = new Calculation(runways.get(0),planes.get(0),obstacles.get(0),2853d,20);
        CalcLog calcLog = calculation.getCalcLog();

        System.out.println(calcLog.getLogsString());
        String val1 = "New TORA (1850.0) = Previous TORA (3660.0) - Distance from Threshold (2853.0) - Blast Protection (300.0) - Displaced Threshold (307.0)";
        String val2 = "New TODA (1850.0) = New TORA (1850.0) + Clearway (0.0)";
        String val3 = "New ASDA (1850.0) = New TORA (1850.0) + Stopway (0.0)";
        String val4 = "New LDA (2553.0) = Distance from Threshold (2853.0) - RESA (240.0) - Strip End (60.0)";
        assertArrayEquals(new String[]{val1,val2,val3,val4},calcLog.getLogs());

    }
}
