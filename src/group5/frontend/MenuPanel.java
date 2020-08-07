package group5.frontend;

import group5.common.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;



public class MenuPanel extends JPanel {

    JPanel pnlImportXML;
    JPanel pnlRunway;
    JPanel pnlObstacles;
    JPanel pnlPlanes;
    JPanel pnlAirport;
    JPanel pnlColourScheme;
    JPanel pnlChangeLog;
    Random random;
    DefaultTableModel mdlAirport;
    JTable tblAirports;
    DefaultTableModel mdlPlanes;
    JTable tblPlanes;
    DefaultTableModel mdlObstacles;
    JTable tblObstacles;
    DefaultTableModel mdlRunways;
    JTable tblRunways;
    DefaultTableModel mdlChangeLog;
    JTable tblChangeLog;
    ArrayList listChangeLog = new ArrayList();


    private FrontEndWindow window;
    Calculator calculator;
    private boolean darkChecker = true;

    public JComboBox cbxAirport;

    public MenuPanel(FrontEndWindow window, Calculator calculator) {
        super();

        this.window = window;
        this.calculator = calculator;

        this.setLayout(new GridLayout(2, 2, 1, 1));
        pnlImportXML = new ImportXMLPanel();
        pnlRunway = new RunwayPanel();
        pnlObstacles = new ObstaclesPanel();
        pnlPlanes = new PlanePanel();
        pnlAirport = new AirportPanel();
        pnlColourScheme = new SettingsLeft();
        pnlChangeLog = new ChangeLogPanel();


        // Create and add JMenu
        JMenuBar menuBar = new JMenuBar();

        // Create menu buttons and sub-buttons
        JMenu file = new JMenu("File");
        JMenuItem addAirport = new JMenuItem("Airports");
        file.add(addAirport);
        JFrame addApFrame = new JFrame();
        addApFrame.add(pnlAirport);
        addAirport.addActionListener(e -> {
            addApFrame.setSize(550, 250);
            addApFrame.setVisible(true);
        });

        JMenuItem addPlane = new JMenuItem("Planes");
        file.add(addPlane);
        JFrame addPFrame = new JFrame();
        addPFrame.add(pnlPlanes);
        addPlane.addActionListener(e -> {
            addPFrame.setSize(400, 425);
            // addPFrame.pack();
            addPFrame.setVisible(true);

        });

        JMenuItem addObstacle = new JMenuItem("Obstacles");
        file.add(addObstacle);
        JFrame addObstacleFrame = new JFrame();
        addObstacleFrame.add(pnlObstacles);
        addObstacle.addActionListener(e -> {
            addObstacleFrame.setSize(700, 350);
            addObstacleFrame.setVisible(true);

        });

        JMenuItem addRunway = new JMenuItem("Runways");
        file.add(addRunway);
        JFrame addRunwayFrame = new JFrame();
        addRunwayFrame.add(pnlRunway);
        addRunway.addActionListener(e -> {
            addRunwayFrame.pack();
            addRunwayFrame.setVisible(true);

        });

        JMenuItem importXml = new JMenuItem("Import XML");
        file.add(importXml);
        JFrame importXmlFrame = new JFrame();
        importXmlFrame.add(pnlImportXML);
        importXml.addActionListener(e -> {
            importXmlFrame.setVisible(true);
            importXmlFrame.setSize(400, 300);
        });

        JMenuItem exportXml = new JMenuItem("Export to XML");
        file.add(exportXml);
        exportXml.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);
            chooser.setSelectedFile(new File("exported_redeclaration.xml"));
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("User saved current program configuration to: " +
                        chooser.getSelectedFile().getAbsolutePath());
                calculator.save(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        //JMenu changelog button

        JMenu changeLog = new JMenu("Change Log");
        // TODO check if its better after new to have JMenu or JCheckBoxMenuItem etc.
        JMenuItem changeLogItem = new JMenuItem("Open");
        changeLog.add(changeLogItem);
        JFrame changeLogFrame = new JFrame();
        changeLogFrame.add(pnlChangeLog);

        changeLogItem.addActionListener(e -> {
            changeLogFrame.setVisible(true);
            changeLogFrame.pack();
        });

        //JMenu help button
        JMenu help = new JMenu("Help");
        JMenuItem userGuide = new JMenuItem("User Guide");
        help.add(userGuide);
        userGuide.addActionListener(e -> {
            File guide = new File("userguide.pdf");
            try {
                Desktop.getDesktop().open(guide);
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        });

        //TODO make colour settings a JMenu click button not a new panel
        JMenu settings = new JMenu("Settings");

        JMenuItem colourSettings = new JMenu("Colour Scheme");
        JMenuItem customiseColourScheme = new JMenuItem("Menu Divider");
        JMenuItem darkLightMode = new JMenuItem("Switch Dark/Light Mode");
        colourSettings.add(customiseColourScheme);
        colourSettings.add(darkLightMode);
        settings.add(colourSettings);
        JMenuItem colourblindMode = new JMenuItem("Colour-blind Mode");
        colourSettings.add(colourblindMode);


        customiseColourScheme.addActionListener(e -> {

            Color c = JColorChooser.showDialog(null, "Choose a Color", pnlAirport.getForeground());
            if (c != null)
                this.setBackground(c);
        });

        darkLightMode.addActionListener(e -> {
            if (darkChecker) {
                window.lightMode();
                darkChecker = false;
            } else {
                window.darkMode();
                darkChecker = true;
            }
        });

        colourblindMode.addActionListener(e -> {
            window.colourblindMode();
        });


        // Adding menus to menu bar
        menuBar.add(file);
        menuBar.add(settings);
        menuBar.add(help);
        menuBar.add(changeLog);


        window.addAll(this, menuBar);
        window.addPanels(pnlImportXML, pnlRunway, pnlObstacles, pnlPlanes, pnlAirport, pnlColourScheme);

    }

    class ChangeLogPanel extends JPanel {

        public ChangeLogPanel() {
            super(new BorderLayout());

            tblChangeLog = new JTable();

            Object[] columnsChangeLog = {"Description", "Timestamp"};

            mdlChangeLog = new DefaultTableModel(){

                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };

            mdlChangeLog.setColumnIdentifiers(columnsChangeLog);

            tblChangeLog.setModel(mdlChangeLog);

            tblChangeLog.getColumnModel().getColumn(0).setPreferredWidth(650);
            tblChangeLog.getColumnModel().getColumn(1).setPreferredWidth(150);

            JScrollPane scrollPaneChangeLogTable = new JScrollPane(tblChangeLog);
            scrollPaneChangeLogTable.setPreferredSize(new Dimension(800, 300));



            this.add(scrollPaneChangeLogTable,BorderLayout.CENTER);

        }
    }

    class ImportXMLPanel extends JPanel {

        public ImportXMLPanel() {
            super(new GridLayout(10, 2, 0, 7));

            this.setBorder(window.BLACKLINE);

            //------------------ file handler components ------------------
            JButton btnImport = new JButton("IMPORT");
            JLabel lblFilePath = new JLabel("Please select XML file to upload: ");
            JTextField txtFilePath = new JTextField();
            // open file explorer when import button clicked
            btnImport.addActionListener(e -> {
                final JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = fc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    txtFilePath.setText(selectedFile.getAbsolutePath());
                    calculator.load(selectedFile.getAbsolutePath());
                    //System.out.println(selectedFile.getAbsolutePath());
                    window.update();
                }
            });


            window.addAll(this, lblFilePath, txtFilePath, btnImport);
            window.addButtons(btnImport);
            window.addLabels(lblFilePath);
            window.addTextFields(txtFilePath);
        }
    }

    class ObstaclesPanel extends JPanel {

        public ObstaclesPanel() {
            super(new BorderLayout(10, 10));

            Border border = BorderFactory.createLineBorder(new Color(63, 65, 69), 10);
            this.setBorder(border);

            //------------------ obstacle components ------------------
            JLabel lblOName = new JLabel("Obstacle Name: (e.g. Standard Cargo Crate) ");
            JTextField txtOName = new JTextField(10);

            JLabel lblOHeight = new JLabel("Height of Obstacle: (Meters e.g. 27.2)");
            JTextField txtOHeight = new JTextField(10);


            JButton btnAddObstacle = new JButton("Add");
            JButton btnDelObstacle = new JButton("Delete");

            // JTable
            tblObstacles = new JTable();

            Object[] columnsObstacle = {"Name", "Height (m)"};

            mdlObstacles = new DefaultTableModel(){

                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };

            mdlObstacles.setColumnIdentifiers(columnsObstacle);

            tblObstacles.setModel(mdlObstacles);

            JScrollPane scrollPaneObstaclesTable = new JScrollPane(tblObstacles);
            scrollPaneObstaclesTable.setBounds(0, 0, 300, 200);
            tblObstacles.getColumnModel().getColumn(0).setPreferredWidth(250);
            tblObstacles.getColumnModel().getColumn(1).setPreferredWidth(50);

            updateObstaclesJtable();

            btnAddObstacle.addActionListener(e -> {

                int obstacleListLength = calculator.getObstacles().size();
                int i = 0;
                boolean duplicate = false;
                while (i < obstacleListLength) {
                    if (txtOName.getText().equals(calculator.getObstacles().get(i).getName())) {
                        duplicate = true;
                        i++;
                    }
                    i++;
                }

                if (txtOName.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Please enter an Obstacle name.");

                } else if (duplicate) {
                    JOptionPane.showMessageDialog(null, "This obstacle already exists.");

                } else {
                    String oName = txtOName.getText();
                    try {
                        Double oHeight = Double.parseDouble(txtOHeight.getText());

                        calculator.addObstacle(new Obstacle(oHeight, oName));

                        // Update combo boxes
                        window.update();


                        JOptionPane.showMessageDialog(null, "Added Obstacle: " + txtOName.getText());

                        // Clear text boxes
                        txtOName.setText("");
                        txtOHeight.setText("");

                    } catch (Exception e1) {

                        JOptionPane.showMessageDialog(null, "Height must be a numerical value.");

                    }
                }

            });

            btnDelObstacle.addActionListener(e -> {
                try {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + calculator.getObstacles().get(tblObstacles.getSelectedRow()).getName() + "?");

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        calculator.getObstacles().remove(tblObstacles.getSelectedRow());
                        window.update();
                    }

                } catch (IndexOutOfBoundsException e1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");

                }
            });

            JPanel pnlTop = new JPanel(new GridLayout(9, 2, 0, 7));

            window.addAll(pnlTop, lblOName, txtOName, lblOHeight, txtOHeight, btnAddObstacle, btnDelObstacle);
            this.add(pnlTop, BorderLayout.EAST);
            this.add(scrollPaneObstaclesTable, BorderLayout.CENTER);
            window.addLabels(lblOName, lblOHeight);
            window.addTextFields(txtOHeight, txtOName);
            window.addButtons(btnAddObstacle, btnDelObstacle);
            window.addPanels(pnlTop);
        }
    }

    class RunwayPanel extends JPanel {

        public RunwayPanel() {
            super(new BorderLayout(10, 10));

            this.setBorder(window.BLACKLINE);

            JLabel lblAirport = new JLabel("Select Airport:");

            cbxAirport = new JComboBox();

            for (Airport x : calculator.getAirports()) {
                cbxAirport.addItem(x);
            }

            JLabel lblRBearing = new JLabel("Runway Bearing:");
            JComboBox cbxRBearing = new JComboBox();

            for (int i = 01; i <= 36; i++) {
                cbxRBearing.addItem(i);
            }

            JPanel pnlAirport = new JPanel(new GridLayout(1, 2, 0, 7));
            pnlAirport.add(lblAirport);
            pnlAirport.add(cbxAirport);
            JPanel pnlBearing = new JPanel(new GridLayout(1, 2, 0, 7));
            pnlBearing.add(lblRBearing);
            pnlBearing.add(cbxRBearing);

            JLabel lblRType = new JLabel("Runway Type:");
            JComboBox cbxType = new JComboBox();
            cbxType.addItem("Single Landing");
            cbxType.addItem("Parallel Landing");

            JLabel lblPos = new JLabel("Position Marker:");
            JComboBox cbxPos = new JComboBox();
            cbxPos.addItem("C");
            cbxPos.addItem("No Marker");
            cbxPos.addItem("L");
            cbxPos.addItem("R");
            cbxPos.setEnabled(true);

            cbxType.addActionListener(e -> {
                if (cbxType.getSelectedItem().equals(cbxType.getItemAt(0))) {
                    cbxPos.setEnabled(true);
                    cbxPos.removeAllItems();
                    cbxPos.addItem("C");
                    cbxPos.addItem("No Marker");
                    cbxPos.addItem("L");
                    cbxPos.addItem("R");
                } else {
                    cbxPos.setEnabled(true);
                    cbxPos.removeAllItems();
                    cbxPos.addItem("L");
                    cbxPos.addItem("R");
                }
            });

            JLabel lblTora = new JLabel("TORA (m):");
            JTextField txtTora = new JTextField(10);
            JLabel lblToda = new JLabel("TODA (m):");
            JTextField txtToda = new JTextField(10);
            JLabel lblAsda = new JLabel("ASDA (m):");
            JTextField txtAsda = new JTextField(10);
            JLabel lblLda = new JLabel("LDA (m):");
            JTextField txtLda = new JTextField(10);

            JPanel pnlType = new JPanel();
            pnlType.setLayout(new GridLayout(1, 2, 0, 7));
            pnlType.add(lblRType);
            pnlType.add(cbxType);
            JPanel pnlPos = new JPanel(new GridLayout(1, 2, 0, 7));
            pnlPos.add(lblPos);
            pnlPos.add(cbxPos);

            JPanel pnlAttributes = new JPanel();
            pnlAttributes.setLayout(new GridLayout(1, 2, 0, 7));
            pnlAttributes.add(lblTora);
            pnlAttributes.add(txtTora);
            JPanel pnlAttributes2 = new JPanel(new GridLayout(1, 2, 0, 7));
            pnlAttributes2.add(lblToda);
            pnlAttributes2.add(txtToda);

            JPanel pnlAttributes3 = new JPanel();
            pnlAttributes3.setLayout(new GridLayout(1, 2, 0, 7));
            pnlAttributes3.add(lblAsda);
            pnlAttributes3.add(txtAsda);
            JPanel pnlAttributes4 = new JPanel(new GridLayout(1, 2, 0, 7));
            pnlAttributes4.add(lblLda);
            pnlAttributes4.add(txtLda);


            JPanel pnlAttributes5 = new JPanel();
            pnlAttributes5.setLayout(new GridLayout(1, 2, 0, 7));
            JLabel lblRunwayLength = new JLabel("Runway Length (m):              ");
            JTextField txtRunwayLength = new JTextField();
            pnlAttributes5.add(lblRunwayLength);
            pnlAttributes5.add(txtRunwayLength);

            JButton btnAddRunway = new JButton("ADD");

            btnAddRunway.addActionListener(e -> {
                // Get data from fields
                try {
                    int rBearing = (int) cbxRBearing.getSelectedItem();
                    char position = ((String) cbxPos.getSelectedItem()).charAt(0);
                    double tora = Double.parseDouble(txtTora.getText());
                    double toda = Double.parseDouble(txtToda.getText());
                    double asda = Double.parseDouble(txtAsda.getText());
                    double lda = Double.parseDouble(txtLda.getText());
                    double length = Double.parseDouble(txtRunwayLength.getText());
                    char type = ((String) cbxType.getSelectedItem()).charAt(0);

                    if (position == 'N' | position == 'n') {
                        position = 0;
                    }

                    String name;

                    if (rBearing <= 9) {
                        name = "0" + rBearing + position;
                    } else {
                        name = "" + rBearing + position;

                    }

                    int runwayListLength = calculator.getRunways().size();
                    int i = 0;
                    boolean duplicate = false;
                    while (i < runwayListLength) {
                        if (name.equals(calculator.getRunways().get(i).getName()) && cbxAirport.getSelectedItem().equals(calculator.getRunways().get(i).getAirport())) {
                            duplicate = true;
                            i++;
                        }
                        i++;
                    }

                    if (!duplicate) {


                        //checks attributes for obvious errors
                        if (toda >= tora && toda >= lda && asda >= tora && length >= tora && length >= asda && length >= lda) {

                            // Add plane to calculator
                            Runway runway = new Runway(position, rBearing, tora, toda, asda, lda, null, null, null, name, (Airport) cbxAirport.getSelectedItem(), length);
                            calculator.addRunway(runway);

                            if (position == 0 | position == 'C' | position == 'L' && type == 'S' | position == 'R' && type == 'S') {
                                JOptionPane.showMessageDialog(null, "Added Runway: " + name + " to " + ((Airport) cbxAirport.getSelectedItem()).getName() + " Airport");
                            }


                            if (position == 'l' | position == 'L' && type == 'P') {
                                char newPosition = 'R';
                                int newBearing = rBearing + 18;
                                if (newBearing > 36) {
                                    newBearing = newBearing - 36;
                                }

                                String newName = "" + newBearing + newPosition;

                                Runway runway2 = new Runway(newPosition, newBearing, toda, tora, asda, lda, null, null, null, newName, (Airport) cbxAirport.getSelectedItem(), length);
                                calculator.addRunway(runway2);
                                runway.setParallel(runway2);
                                runway2.setParallel(runway);

                                JOptionPane.showMessageDialog(null, "Added Runway: " + name + " and " + newName + " to " + ((Airport) cbxAirport.getSelectedItem()).getName() + " Airport");

                            } else if (position == 'R' | position == 'r' && type == 'P') {
                                char newPosition = 'L';
                                int newBearing = rBearing + 18;
                                if (newBearing > 36) {
                                    newBearing = newBearing - 36;
                                }

                                String newName = "" + newBearing + newPosition;

                                Runway runway2 = new Runway(newPosition, newBearing, tora, toda, asda, lda, null, null, null, newName, (Airport) cbxAirport.getSelectedItem(), length);
                                calculator.addRunway(runway2);
                                runway.setParallel(runway2);
                                runway2.setParallel(runway);

                                JOptionPane.showMessageDialog(null, "Added Runway: " + name + " and " + newName + " to " + ((Airport) cbxAirport.getSelectedItem()).getName() + " Airport");


                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Please check that: \n\n" +
                                    "TODA >= TORA \n" +
                                    "TODA >= LDA\n" +
                                    "ASDA >= TORA\n" +
                                    "RUNWAY LENGTH >= TORA, ASDA, LDA");

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "This runway already exists.");
                    }

                } catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(null, "Please ensure the numerical fields are filled correctly.");


                }

                // Update combo boxes
                window.update();


            });

            JButton btnDelRunway = new JButton("Delete");

            btnDelRunway.addActionListener(e -> {
                try {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + calculator.getRunways().get(tblRunways.getSelectedRow()).getName() + "?");

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        calculator.getRunways().remove(tblRunways.getSelectedRow());
                        window.update();
                    }

                } catch (IndexOutOfBoundsException e1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");

                }

            });

            // JTable
            tblRunways = new JTable();

            Object[] columnsRunways = {"Runway", "Airport", "TORA", "TODA", "ASDA", "LDA", "Length"};

            mdlRunways = new DefaultTableModel(){

                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            mdlRunways.setColumnIdentifiers(columnsRunways);

            tblRunways.setModel(mdlRunways);

            JScrollPane scrollPaneRunwaysTable = new JScrollPane(tblRunways);
            scrollPaneRunwaysTable.setBounds(0, 0, 300, 200);

            tblRunways.getColumnModel().getColumn(0).setPreferredWidth(28);
            tblRunways.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblRunways.getColumnModel().getColumn(2).setPreferredWidth(28);
            tblRunways.getColumnModel().getColumn(3).setPreferredWidth(28);
            tblRunways.getColumnModel().getColumn(4).setPreferredWidth(28);
            tblRunways.getColumnModel().getColumn(5).setPreferredWidth(28);
            tblRunways.getColumnModel().getColumn(6).setPreferredWidth(28);

            JPanel pnlRight = new JPanel();
            pnlRight.setLayout(new GridLayout(11, 2, 10, 10));

            JPanel pnlLeft = new JPanel();
            pnlLeft.add(scrollPaneRunwaysTable);

            updateRunwaysJtable();


            window.addAll(pnlRight, pnlAirport, pnlBearing, pnlType, pnlPos, pnlAttributes, pnlAttributes2, pnlAttributes3, pnlAttributes4, pnlAttributes5, btnAddRunway, btnDelRunway);
            this.add(pnlRight, BorderLayout.EAST);
            this.add(pnlLeft, BorderLayout.WEST);
            window.addLabels( lblAirport, lblRBearing, lblRType, lblPos, lblTora, lblToda, lblAsda, lblLda, lblRunwayLength);
            window.addTextFields(txtTora, txtToda, txtAsda, txtLda, txtRunwayLength);
            window.addButtons(btnAddRunway, btnDelRunway);
            window.addPanels(pnlAirport, pnlBearing, pnlType, pnlPos, pnlAttributes, pnlAttributes2, pnlAttributes3, pnlAttributes4, pnlAttributes5, pnlLeft, pnlRight);
        }

    }

    class PlanePanel extends JPanel {


        public PlanePanel() {
            super(new BorderLayout(10, 10));
            Border border = BorderFactory.createLineBorder(new Color(63, 65, 69), 10);
            this.setBorder(border);
            //------------------ plane components ------------------

            JLabel lblModel = new JLabel("Enter Model ");
            JTextField txtModel = new JTextField(10);

            JLabel lblBlastProtection = new JLabel("Blast Protection (m):");
            JTextField txtBlastProtection = new JTextField(10);

            JLabel lblPLength = new JLabel("Length of Plane(m): ");
            JTextField txtPLength = new JTextField(10);

            JLabel lblPWidth = new JLabel("Wing-Span(m): ");
            JTextField txtPWidth = new JTextField(10);

            JPanel pnlAttr1 = new JPanel();
            pnlAttr1.setLayout(new GridLayout(1, 2, 0, 7));
            pnlAttr1.add(lblModel);
            pnlAttr1.add(txtModel);

            JPanel pnlAttr2 = new JPanel();
            pnlAttr2.setLayout(new GridLayout(1, 2, 0, 7));
            pnlAttr2.add(lblBlastProtection);
            pnlAttr2.add(txtBlastProtection);

            JPanel pnlAttr3 = new JPanel();
            pnlAttr3.setLayout(new GridLayout(1, 2, 0, 7));
            pnlAttr3.add(lblPLength);
            pnlAttr3.add(txtPLength);

            JPanel pnlAttr4 = new JPanel();
            pnlAttr4.setLayout(new GridLayout(1, 2, 0, 7));
            pnlAttr4.add(lblPWidth);
            pnlAttr4.add(txtPWidth);

            // Button to add a plane to the database
            JButton btnAddPlane = new JButton("Add");
            JButton btnDelPlane = new JButton("Delete");

            // JTable
            tblPlanes = new JTable();

            Object[] columnsAirport = {"Model", "Blast Protection"};

            mdlPlanes = new DefaultTableModel(){

                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            mdlPlanes.setColumnIdentifiers(columnsAirport);

            tblPlanes.setModel(mdlPlanes);

            JScrollPane scrollPanePlanesTable = new JScrollPane(tblPlanes);
            scrollPanePlanesTable.setBounds(0, 0, 200, 200);

            tblPlanes.getColumnModel().getColumn(0).setPreferredWidth(175);
            tblPlanes.getColumnModel().getColumn(1).setPreferredWidth(25);

            updatePlaneJtable();


            btnAddPlane.addActionListener(e -> {


                int planeListLength = calculator.getPlanes().size();
                int i = 0;
                boolean duplicate = false;
                while (i < planeListLength) {
                    if (txtModel.getText().equals(calculator.getPlanes().get(i).getName())) {
                        duplicate = true;
                        i++;
                    }
                    i++;
                }


                if (txtModel.getText().isBlank() | txtBlastProtection.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Please fill out all fields.");

                } else if (duplicate) {
                    JOptionPane.showMessageDialog(null, "This plane already exists.");

                } else {
                    // Get data from fields
                    String name = txtModel.getText();
                    try {
                        double blastProtection = Double.parseDouble(txtBlastProtection.getText());

                        double ratio = (Double.parseDouble(txtPWidth.getText())) / (Double.parseDouble(txtPLength.getText()));

                        // Add plane to calculator
                        calculator.addPlane(new Plane(name, blastProtection, ratio));

                        // Update combo boxes
                        window.update();

                        JOptionPane.showMessageDialog(null, "Added Plane: " + txtModel.getText());

                        // Clear text boxes
                        txtModel.setText("");
                        txtBlastProtection.setText("");
                        txtPLength.setText("");
                        txtPWidth.setText("");

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Blast protection, length and wing-span must be numerical values.");

                    }

                }
            });

            btnDelPlane.addActionListener(e -> {
                try {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + calculator.getPlanes().get(tblPlanes.getSelectedRow()).getName() + "?");

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        calculator.getPlanes().remove(tblPlanes.getSelectedRow());
                        window.update();
                    }

                } catch (IndexOutOfBoundsException e1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");

                }
            });

            JPanel pnladdPlane = new JPanel(new GridLayout(6, 2, 10, 7));
            window.addAll(pnladdPlane, pnlAttr1, pnlAttr2, pnlAttr3, pnlAttr4, btnAddPlane, btnDelPlane);

            pnladdPlane.setBorder(border);
            scrollPanePlanesTable.setBorder(border);

            this.add(pnladdPlane, BorderLayout.SOUTH);
            this.add(scrollPanePlanesTable, BorderLayout.CENTER);
            window.addLabels( lblModel, lblBlastProtection, lblPLength, lblPWidth);
            window.addTextFields(txtModel, txtBlastProtection, txtPLength, txtPWidth);
            window.addButtons(btnAddPlane, btnDelPlane);
            window.addPanels(pnladdPlane, pnlAttr1, pnlAttr2, pnlAttr3, pnlAttr4);


        }
    }

    class AirportPanel extends JPanel {

        public AirportPanel() {
            super(new BorderLayout(10, 10));

            JPanel pnlAddAirport = new JPanel(new GridLayout(1, 4, 10, 10));


            Border border = BorderFactory.createLineBorder(new Color(63, 65, 69), 10);
            this.setBorder(border);

            // Above JTable
            JLabel lblNameAirport = new JLabel("Airport Name: ");
            JTextField txtAirportName = new JTextField(10);
            pnlAddAirport.add(lblNameAirport);
            pnlAddAirport.add(txtAirportName);
            JLabel lblAirportCode = new JLabel("Airport Code");
            JTextField txtAirportCode = new JTextField();
            pnlAddAirport.add(lblAirportCode);
            pnlAddAirport.add(txtAirportCode);



            JPanel pnlTopHalf = new JPanel(new GridLayout(1, 1));
            pnlTopHalf.add(pnlAddAirport);


            // JTable
            tblAirports = new JTable();

            Object[] columnsAirport = {"Airport", "Runways"};

            mdlAirport = new DefaultTableModel(){

                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            mdlAirport.setColumnIdentifiers(columnsAirport);

            tblAirports.setModel(mdlAirport);

            JScrollPane scrollPaneAirportTable = new JScrollPane(tblAirports);
            scrollPaneAirportTable.setBounds(0, 0, 700, 500);

            updateAirportJtable();

            // East of JTable

            JPanel pnlEast = new JPanel();

            JButton btnAddAirport = new JButton("Add");
            btnAddAirport.setSize(50, 20);
            JButton btnDelAirport = new JButton("Delete");
            btnDelAirport.setSize(50, 20);


            pnlEast.add(btnAddAirport);
            pnlEast.add(btnDelAirport);


            btnAddAirport.addActionListener(e -> {
                int airportListLength = calculator.getAirports().size();
                int i = 0;
                boolean duplicate = false;

                while (i < airportListLength) {
                    if (txtAirportName.getText().equals(calculator.getAirports().get(i).getName())) {
                        duplicate = true;
                        i++;
                    }
                    i++;
                }

                if (txtAirportName.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Please enter an airport name.");

                } else if (duplicate) {
                    JOptionPane.showMessageDialog(null, "This airport already exists.");

                } else if (txtAirportCode.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Please enter an airport code e.g. HW.");
                } else {

                    String AName = txtAirportName.getText();
                    String ACode = txtAirportCode.getText();
                    calculator.addAirport(new Airport("" + AName + " (" + ACode + ")"));

                    // Update combo boxes
                    window.update();
                    JOptionPane.showMessageDialog(null, "Added Airport: " + txtAirportName.getText() + ".\nTo manage " + txtAirportName.getText() + "'s Runways, go to File -> Runways");

                    // Clear text boxes
                    txtAirportName.setText("");



                }


            });

            btnDelAirport.addActionListener(e -> {
                try {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + calculator.getAirports().get(tblAirports.getSelectedRow()).getName() + "? This will also remove all associated runways.");

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        calculator.getAirports().remove(tblAirports.getSelectedRow());
                        window.update();
                    }

                } catch (IndexOutOfBoundsException e1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");

                }

            });

            this.add(pnlTopHalf, BorderLayout.NORTH);
            this.add(scrollPaneAirportTable, BorderLayout.CENTER);
            this.add(pnlEast, BorderLayout.EAST);
            window.addLabels(lblNameAirport, lblAirportCode);
            window.addTextFields(txtAirportName, txtAirportCode);
            window.addPanels(this, pnlAddAirport, pnlEast);
        }
    }


    class SettingsLeft extends JPanel {

        public SettingsLeft() {
            super();

        }
    }

    public void updateComboBoxes() {

        cbxAirport.removeAllItems();

        for (Airport x : calculator.getAirports()) {
            cbxAirport.addItem(x);
        }

    }

    public void updateAirportJtable() {
        mdlAirport.setRowCount(0);
        int airportRow = 0;

        for (Airport x : calculator.getAirports()) {
            mdlAirport.addRow(new Airport[]{x});
            tblAirports.setValueAt(x.getName(), airportRow, 0);
            tblAirports.setValueAt(x.getRunways(), airportRow, 1);
            airportRow++;
        }
    }

    public void updatePlaneJtable() {
        mdlPlanes.setRowCount(0);
        int planeRow = 0;

        for (Plane x : calculator.getPlanes()) {
            mdlPlanes.addRow(new Plane[]{x});
            tblPlanes.setValueAt(x.getName(), planeRow, 0);
            tblPlanes.setValueAt(x.getBlastProtect(), planeRow, 1);
            planeRow++;
        }
    }

    public void updateObstaclesJtable() {
        mdlObstacles.setRowCount(0);
        int obstaclesRow = 0;

        for (Obstacle x : calculator.getObstacles()) {
            mdlObstacles.addRow(new Obstacle[]{x});
            tblObstacles.setValueAt(x.getName(), obstaclesRow, 0);
            tblObstacles.setValueAt(x.getHeight(), obstaclesRow, 1);
            obstaclesRow++;
        }
    }

    public void updateRunwaysJtable() {
        mdlRunways.setRowCount(0);
        int runwaysRow = 0;

        for (Runway x : calculator.getRunways()) {
            mdlRunways.addRow(new Runway[]{x});
            tblRunways.setValueAt(x.getName(), runwaysRow, 0);
            tblRunways.setValueAt(x.getAirport(), runwaysRow, 1);
            tblRunways.setValueAt(x.getTORA(), runwaysRow, 2);
            tblRunways.setValueAt(x.getTODA(), runwaysRow, 3);
            tblRunways.setValueAt(x.getASDA(), runwaysRow, 4);
            tblRunways.setValueAt(x.getLDA(), runwaysRow, 5);
            tblRunways.setValueAt(x.getLength(), runwaysRow, 6);

            runwaysRow++;
        }

    }

    public void updateChangeLog() {
            Updated updates = calculator.updated();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
            String d = dtf.format(now);


                for (Airport x : updates.getAirportsAdded()) {
                    String s = "[AIRPORT]           " + x.getName() + " has been added";
                    listChangeLog.add(s);
                    listChangeLog.add(d);
                }

                for (Obstacle x : updates.getObstaclesAdded()) {
                    String s = "[OBSTACLE]        " + x.getName() + " with a height of " + x.getHeight() + " has been added";
                    listChangeLog.add(s);
                    listChangeLog.add(d);

                }

                for (Plane x : updates.getPlanesAdded()) {
                    String s = "[PLANE]               " + x.getName() + " with blast protection: " + x.getBlastProtect() + " has been added";
                    listChangeLog.add(s);
                    listChangeLog.add(d);

                }

                for (Runway x : updates.getRunwaysAdded()) {
                    String s = "[RUNWAY]            " + x.getName() + " has been added to " + x.getAirport() + " \n Tora = " + x.getTORA()
                            + " Toda = " + x.getTODA() + " \n Asda = " + x.getASDA() + " Lda = " + x.getLDA();
                    listChangeLog.add(s);
                    listChangeLog.add(d);
                }

                calculator.updated();

                int changeLogRow = 0;
                
                for (int arrayIndex = 0; arrayIndex < listChangeLog.size(); arrayIndex = arrayIndex + 2) {
                    mdlChangeLog.addRow(new Object[] {});
                    tblChangeLog.setValueAt(listChangeLog.get(arrayIndex), changeLogRow, 0);
                    tblChangeLog.setValueAt(listChangeLog.get(arrayIndex + 1), changeLogRow, 1);
                    changeLogRow++;

                }

            }


}


