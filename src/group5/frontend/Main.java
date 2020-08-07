/*
package group5.frontend;

import group5.common.Obstacle;
import group5.common.Plane;
import group5.common.Runway;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {

        //Frame for the main window
        JFrame window = new JFrame();
        window.setVisible(true);
        window.setSize(1200, 600);

        //creates a JTable for left side of pnlHome
        JTable homeTable = new JTable();

        // create a table model and set a Column Identifiers to this model
        Object[] columns = {"Runway", "Plane", "Flight No.", "Obstacle", "Gate From", "Gate To"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        // set the model to the table
        homeTable.setModel(model);

        // create JScrollPane for the table
        JScrollPane pnlHomeLeft = new JScrollPane(homeTable);
        pnlHomeLeft.setBounds(0, 0, 1000, 200);


        // create JButtons and dropdowns for calculation portion of home tab
        JButton btnCalcHistory = new JButton("Calculation History");
        JButton btnCalculate = new JButton("Calculate");
        JComboBox boxPlane = new JComboBox();
        JComboBox boxRunway = new JComboBox();
        JComboBox boxObstacle = new JComboBox();

        // right side panel add drop downs buttons to their own panel
        JPanel pnlHomeDropdownInputs = new JPanel();
        pnlHomeDropdownInputs.setLayout(new GridLayout(1, 3));
        pnlHomeDropdownInputs.add(boxPlane);
        pnlHomeDropdownInputs.add(boxRunway);
        pnlHomeDropdownInputs.add(boxObstacle);

        // add other right side panel inputs
        JPanel pnlHomeInputs = new JPanel();
        pnlHomeInputs.setLayout(new GridLayout(5, 2));

        // generate labels and textboxes for pnlHomeInputs panel
        JLabel todaLabel = new JLabel();
        todaLabel.setText("Take-Off Distance Available (TODA): ");
        JLabel toraLabel = new JLabel();
        toraLabel.setText("Take-Off Run Available (TORA): ");
        JLabel asdaLabel = new JLabel();
        asdaLabel.setText("Accelerate-Stop Distance Available (ASDA): ");
        JLabel ldaLabel = new JLabel();
        ldaLabel.setText("Landing Distance Available (LDA): ");
        JLabel flightNoLabel = new JLabel();
        flightNoLabel.setText("Flight Number: ");

        JTextField todaText = new JTextField(20);
        JTextField toraText = new JTextField(20);
        JTextField asdaText = new JTextField(20);
        JTextField ldaText = new JTextField(20);
        JTextField flightNoText = new JTextField(20);


        // add labels and textboxes to pnlHomeInputs panel
        pnlHomeInputs.add(todaLabel);
        pnlHomeInputs.add(todaText);
        pnlHomeInputs.add(toraLabel);
        pnlHomeInputs.add(toraText);
        pnlHomeInputs.add(asdaLabel);
        pnlHomeInputs.add(asdaText);
        pnlHomeInputs.add(ldaLabel);
        pnlHomeInputs.add(ldaText);
        pnlHomeInputs.add(flightNoLabel);
        pnlHomeInputs.add(flightNoText);


        // divide the right side of home into two panels - top and bottom, and add the input panels to the top one
        JPanel pnlHomeRight = new JPanel();
        pnlHomeRight.setLayout(new GridLayout(2, 1));

        JPanel pnlHomeRightTop = new JPanel();
        JPanel pnlHomeRightBtm = new JPanel();
        pnlHomeRightBtm.setLayout(new BorderLayout());
        pnlHomeRight.add(pnlHomeRightTop);
        pnlHomeRight.add(pnlHomeRightBtm);

        pnlHomeRightTop.add(pnlHomeDropdownInputs);
        pnlHomeRightTop.add(pnlHomeInputs);
        pnlHomeRightTop.add(btnCalcHistory);
        pnlHomeRightTop.add(btnCalculate);


        //picture for runway visualisation
        BufferedImage myPicture = null;
        try {
            String currentDirectory = System.getProperty("user.dir");
            myPicture = ImageIO.read(new File(currentDirectory + "/src/group5/frontend/runway_strip.png"));
        } catch (IOException e) {
            System.out.println("Error file not found");
            e.printStackTrace();
        }

        //got commit test
        //scales the image to the size of the JPanel
        Image scaledImage = myPicture.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));


        pnlHomeRightBtm.add(picLabel, BorderLayout.CENTER);


        JPanel pnlHome = new JPanel();
        pnlHome.setLayout(new GridLayout(1, 1));
        pnlHome.add(pnlHomeLeft);
        pnlHome.add(pnlHomeRight);


/////////////////////////////////////////////////// Import and settings panels

        // making labels components
        // @TODO Underline and title
        // JLabel lblAirport = new JLabel("Airport");
        JLabel lblRunway = new JLabel("Runway");
        JLabel lblObstacle = new JLabel("Obstacle");
        JLabel lblPlane = new JLabel("Plane");

        //------------------ airport components ------------------
        JLabel lblAId = new JLabel("Airport ID: ");
        JTextField txtAId = new JTextField(20);

        JLabel lblAName = new JLabel("Airport Name: ");
        JTextField txtAName = new JTextField(20);

        JButton btnAddAirport = new JButton("ADD");


        //------------------ runway components ------------------
        JLabel lblRName = new JLabel("Enter Airport-Runway (e.g. Heathrow - Terminal 1):");
        JTextField txtRName = new JTextField(10);

        JLabel lblRBearing = new JLabel("Runway Bearing: ");
        JTextField txtRBearing = new JTextField(10);

        JLabel lblRLength = new JLabel("Length of Runaway: ");
        JTextField txtRLength = new JTextField(10);

        JLabel lblRWidth = new JLabel("Width of Runaway: ");
        JTextField txtRWidth = new JTextField(10);

        JButton btnAddRunway = new JButton("ADD");

        //------------------ obstacle components ------------------
        JLabel lblOName = new JLabel("Obstacle Name: ");
        JTextField txtOName = new JTextField(10);

        JLabel lblOHeight = new JLabel("Height of Obstacle: ");
        JTextField txtOHeight = new JTextField(10);

        JButton btnAddObstacle = new JButton("ADD");

        //------------------ plane components ------------------
        JLabel lblModel = new JLabel("Enter Model (e.g. Boeing 777):");
        JTextField txtModel = new JTextField(10);

        JLabel lblBlastProtection = new JLabel("Blast Protection: ");
        JTextField txtBlastProtection = new JTextField(10);

        JLabel lblPLength = new JLabel("Length of Plane: ");
        JTextField txtPLength = new JTextField(10);

        JLabel lblPWidth = new JLabel("Width of Plane: ");
        JTextField txtPWidth = new JTextField(10);

        JButton btnAddPlane = new JButton("ADD");

        //------------------ file handler components ------------------
        JButton btnImport = new JButton("Import");
        JLabel lblFilePath = new JLabel("Please select XML file to upload: ");
        JTextField txtFilePath = new JTextField();
        // open file explorer when import button clicked
        btnImport.addActionListener(e -> {
            final JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = fc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fc.getSelectedFile();
                txtFilePath.setText(selectedFile.getAbsolutePath());
                //System.out.println(selectedFile.getAbsolutePath());
            }
        });


        // panels
        JPanel pnlImport = new JPanel();
        JPanel pnlSettings = new JPanel(new BorderLayout());

        //////////////// IMPORT /////////////////
        pnlImport.setLayout(new GridLayout(2, 2, 1, 1));

        /// airport layout ///
        JPanel pnlAirport = new JPanel(new GridLayout(15, 2, 0, 7));
        addAll(pnlAirport, lblAirport, lblARunway, cbxARunway, lblAId, txtAId, lblAName, txtAName, btnAddAirport);


        /// runway layout ///
        JPanel pnlRunway = new JPanel(new GridLayout(10, 2, 0, 7));
        //JPanel pnlRunway = new JPanel();
        //pnlRunway.setLayout(new BoxLayout(pnlRunway, BoxLayout.Y_AXIS));

        addAll(pnlRunway, lblRunway, lblRName, txtRName, lblRBearing, txtRBearing, lblRLength, txtRLength, lblRWidth, txtRWidth, btnAddRunway);
        //pnlRunway.add(cbxRParallel);

        /// obstacles layout ///
        JPanel pnlObstacles = new JPanel(new GridLayout(10, 2, 0, 7));
        addAll(pnlObstacles, lblObstacle, lblOName, txtOName, lblOHeight, txtOHeight, btnAddObstacle );

        /// planes layout ///
        JPanel pnlPlanes = new JPanel(new GridLayout(10, 2, 0, 7));
        addAll(pnlPlanes, lblPlane, lblModel, txtModel, lblBlastProtection, txtBlastProtection, lblPLength, txtPLength, lblPWidth, txtPWidth, btnAddPlane);

        /// file handler layout ///
        JPanel pnlImportXml = new JPanel(new GridLayout(10, 2, 0, 7));
        addAll(pnlImportXml, lblFilePath, txtFilePath, btnImport);

        /// add all panels to import ///
        addAll(pnlImport, pnlRunway, pnlObstacles, pnlPlanes, pnlImportXml );


        //////////////// SETTINGS /////////////////

        JPanel pnlSettingsLeft = new JPanel();
        pnlSettingsLeft.setLayout(new BoxLayout(pnlSettingsLeft, BoxLayout.PAGE_AXIS));

        JButton btnUserGuide = new JButton("Download User Guide    ");
        JButton btnColour = new JButton("Change Colour Scheme");

        addAll(pnlSettingsLeft, btnUserGuide, btnColour);

        pnlSettings.add(pnlSettingsLeft, BorderLayout.LINE_START);


        // when colour button pressed, open colour selector
        btnColour.addActionListener(e -> {
            Color c = JColorChooser.showDialog(null, "Choose a Color", btnColour.getForeground());
            if (c != null)
                btnColour.setBackground(c);
        });









        //////////////// TAB DESIGN /////////////////
        // panels
        ArrayList<JPanel> panels = new ArrayList();
        addToArrayList(panels, pnlHomeDropdownInputs, pnlHomeInputs, pnlHomeRight, pnlHomeRightBtm, pnlHomeRightTop, // HOME LEFT SCROLL PANE NOT HERE
                pnlImportXml, pnlObstacles, pnlPlanes, pnlRunway, pnlHome, pnlImport, pnlSettings);
        for(JPanel p : panels){
            p.setBackground(Color.decode("#718487"));
        }

        // borders
        Border blackline = BorderFactory.createLineBorder(Color.decode("#133337"), 10, true);
        pnlRunway.setBorder(blackline);
        pnlObstacles.setBorder(blackline);
        pnlPlanes.setBorder(blackline);
        pnlImportXml.setBorder(blackline);

        // buttons
        ArrayList<JButton> buttons = new ArrayList();
        addToArrayList(buttons, btnAddObstacle, btnAddPlane, btnAddRunway, btnCalcHistory, btnCalculate, btnColour, btnImport, btnUserGuide);
        for(JButton b : buttons){
            b.setBackground(new Color(42, 71, 75));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
        }

        // text fields
        ArrayList<JTextField> textfields = new ArrayList();
        addToArrayList(textfields, txtBlastProtection, txtFilePath, txtModel, txtOHeight, txtOName, txtPLength, txtPWidth, txtRBearing, txtRLength,txtRName,txtRWidth,
                asdaText, flightNoText, ldaText, todaText, toraText);
        for(JTextField t : textfields){
            t.setBackground(new Color(137, 153, 155));
            t.setForeground(Color.WHITE);
            t.setBorder(null);
        }

        // labels
        ArrayList<JLabel> labels = new ArrayList();
        addToArrayList(labels, lblBlastProtection, lblFilePath, lblModel, lblObstacle, lblOHeight, lblOName, lblPlane, lblPLength, lblPWidth, lblRBearing, lblRLength, lblRName, lblRunway, lblRWidth, asdaLabel, flightNoLabel, ldaLabel, picLabel, todaLabel, toraLabel);
        for(JLabel l : labels){
            l.setBackground(new Color(137, 153, 155));
            l.setForeground(Color.WHITE);
            l.setBorder(null);
        }

        // table
        homeTable.setBackground(new Color(42, 71, 75));
        homeTable.setForeground(Color.WHITE);

        // create and add tabs to tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Home", pnlHome);
        tabs.add("Import", pnlImport);
        tabs.add("Settings", pnlSettings);

        window.add(tabs);


    }

    public static void addAll(JPanel panel, JComponent... cs){
        for(JComponent c : cs){
            panel.add(c);
        }
    }
    public static void addToArrayList(ArrayList al, JComponent... cs){
        for(JComponent c : cs){
            al.add(c);
        }
    }

}
*/
