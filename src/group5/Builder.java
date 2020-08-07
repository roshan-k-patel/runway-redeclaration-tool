package group5;

import group5.backend.Coordinator;
import group5.common.*;
import group5.frontend.FrontEndWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Contains static methods for building the project.
 */
public class Builder extends JFrame {


    public static void main(String[] args) {



        try {


            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            //Set Font and size
            UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 16)));
            UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 14)));
            UIManager.put("TextField.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 14)));
            UIManager.put("ComboBox.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 14)));
            UIManager.put("Table.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 14)));
            UIManager.put("Menu.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 14)));
            UIManager.put("MenuItem.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 14)));



        } catch (Exception e) {
            // handle exception
        }




        //Generating preloaded objects

        Calculator calc = new Coordinator();

        Airport heathrow = new Airport("Heathrow (HW)");
        Airport gatwick = new Airport("Gatwick (GW)");
        Airport stansted = new Airport("Stansted (ST)");

        Runway hw1 = new Runway('C', 6, 3660, 3660, 3660, 3000, null, null, null, null, heathrow, 4500);


        calc.addAirport(heathrow);
        calc.addAirport(gatwick);
        calc.addAirport(stansted);

        calc.addRunway(hw1);

        calc.addPlane(new Plane("Airbus A220"));
        calc.addPlane(new Plane("Airbus A330"));
        calc.addPlane(new Plane("Boeing 737"));
        calc.addPlane(new Plane("Boeing 747"));
        calc.addPlane(new Plane("Boeing 777"));


        calc.addObstacle(new Obstacle(5, "Standard cargo crate"));
        calc.addObstacle(new Obstacle(20, "Broken down Boeing 737"));
        calc.addObstacle(new Obstacle(3, "Forklift"));
        calc.addObstacle(new Obstacle(25, "Stacked cargo crates"));
        calc.addObstacle(new Obstacle(15, "Large cargo crate"));




        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrontEndWindow window = new FrontEndWindow(calc);
            }
        });


    }


}