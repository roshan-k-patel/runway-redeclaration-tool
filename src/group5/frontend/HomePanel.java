package group5.frontend;

import group5.common.*;
import javafx.embed.swing.JFXPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.Color.*;

public class HomePanel extends JPanel {

    private Calculator calculator;
    private FrontEndWindow window;

    private JTable table;
    private DefaultTableModel model;

    private PnlHomeContainer homeContainer;

    private Calculation selectedCalculation;

    private final Color BACKGROUND_COLOR_GREY = new Color(63, 65, 69);

    private JButton btnCalculationBreakdown;

    public HomePanel(FrontEndWindow window, Calculator calculator) {
        super();
        this.window = window;
        this.calculator =  calculator;

        this.setLayout(new BorderLayout());

        homeContainer = new PnlHomeContainer();
        this.add(homeContainer, BorderLayout.CENTER);
//      Border border = BorderFactory.createLineBorder(new Color(63, 65, 69), 15);
//      pnlCalcBreakdown.setBorder(border);

        window.addAll(this);

    }

    public void updatePanel() {
        homeContainer.updatePanel();
    }

    /**
     * Holds all the panels for the content on the HomePanel.
     */
    class PnlHomeContainer extends JPanel {

        private BorderLayout layout;
        private PnlCalcMenu pnlCalcMenu;
        private PnlMapAndList pnlMapAndList;

        /**
         * Adds the main content panel and the calculation panel to the
         */
        public PnlHomeContainer() {
            super();
            layout = new BorderLayout();
            this.setLayout(layout);

            this.pnlCalcMenu = new PnlCalcMenu();
            this.pnlMapAndList = new PnlMapAndList();

            window.addPanels(pnlMapAndList, pnlCalcMenu);

            JPanel dummyPanel1 = new JPanel();
            dummyPanel1.setLayout(new BorderLayout());

            JPanel labelPanel = new JPanel();

            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("eastpanel.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            add(picLabel);

            labelPanel.setBackground(new Color(63, 65, 69));
            labelPanel.add(picLabel);

            dummyPanel1.add(pnlCalcMenu,BorderLayout.NORTH);
            dummyPanel1.add(labelPanel,BorderLayout.SOUTH);
            dummyPanel1.add(new JPanel(),BorderLayout.CENTER);

            dummyPanel1.setBackground(new Color(63, 65, 69));
            dummyPanel1.add(picLabel);

            this.add(dummyPanel1, BorderLayout.EAST);
            this.add(this.pnlMapAndList, BorderLayout.CENTER);

            window.addPanels(dummyPanel1,labelPanel);


        }

        public void updatePanel() {
            pnlMapAndList.updatePanel();
            pnlCalcMenu.updatePanel();
        }

        class PnlMapAndList extends JPanel {

            private CardLayout cardLayout;

            public final String MAPANDLIST = "mapAndList";
            public final String MAP = "map";
            public final String LIST = "list";

            private PnlMap pnlMap;
            private PnlList pnlList;
            private PnlMapList pnlMapList;

            private PnlMapAndListMenu pnlMapAndListMenu;

            private JPanel panelHolder;


            private PnlMapAndList.PnlTopView topView;
            private PnlMapAndList.PnlSideView sideView;

            public PnlMapAndList() {

                this.cardLayout = new CardLayout();

                this.panelHolder = new JPanel();
                this.panelHolder.setLayout(cardLayout);

                this.pnlMapAndListMenu = new PnlMapAndListMenu();

                this.setLayout(new BorderLayout());

                this.pnlMapList = new PnlMapList();

                JPanel dummyPanel1 = new JPanel();
                dummyPanel1.setLayout(new BorderLayout());

                JPanel dummyPanel2 = new JPanel();
                dummyPanel2.setLayout(new GridLayout(2,1));

                this.topView = new PnlTopView();
                this.sideView = new PnlSideView();

                topView.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1),BorderFactory.createBevelBorder(2)));
                sideView.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1),BorderFactory.createBevelBorder(2)));

                dummyPanel2.add(this.topView);
                dummyPanel2.add(this.sideView);

                dummyPanel1.add(dummyPanel2,BorderLayout.CENTER);


                this.panelHolder.add(dummyPanel1,MAP);
                this.panelHolder.add(pnlMapList,MAPANDLIST);

                this.add(pnlMapAndListMenu,BorderLayout.NORTH);
                this.add(panelHolder,BorderLayout.CENTER);

                window.addPanels(topView,sideView);


            }

            public void updatePanel() {
                pnlMapList.updatePanel();
                sideView.updatePanel();
                topView.updatePanel();

            }

            public void displayCard(String str) {
                this.cardLayout.show(panelHolder, str);
            }

            class PnlMapAndListMenu extends JPanel {

                private JButton onlyMap;
                private JButton both;

                public PnlMapAndListMenu(){

                    this.onlyMap = new JButton("Only map");
                    this.both = new JButton("Map and List");

                    onlyMap.addActionListener(e -> {
                        javax.swing.SwingUtilities.invokeLater(new Runnable(){
                            @Override
                            public void run() {
                                pnlMapAndList.displayCard(pnlMapAndList.MAP);
                            }
                        });
                    });

                    both.addActionListener(e -> {
                        javax.swing.SwingUtilities.invokeLater(new Runnable(){
                            @Override
                            public void run() {
                                pnlMapAndList.displayCard(pnlMapAndList.MAPANDLIST);
                            }
                        });
                    });

                    JPanel dummyPanel = new JPanel();

                    dummyPanel.setLayout(new GridLayout(1,3,0,10));

                    dummyPanel.add(onlyMap);
                    dummyPanel.add(both);

                    this.setLayout(new BorderLayout());
                    this.add(dummyPanel,BorderLayout.NORTH);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            pnlMapAndList.displayCard(pnlMapAndList.MAPANDLIST);
                        }
                    });

                }

                public void updatePanel() {
                    // Nothing happens
                }
            }

            class PnlMap extends JPanel {

                private BorderLayout layout;

                private PnlMapMenu pnlMapMenu;
                private PnlMapSelection pnlMapSelection;

                public PnlMap() {
                    super();
                    this.layout = new BorderLayout();

                    this.pnlMapMenu = new PnlMapMenu();
                    this.pnlMapSelection = new PnlMapSelection();

                    this.setLayout(layout);

                    this.add(this.pnlMapMenu, BorderLayout.NORTH);
                    this.add(this.pnlMapSelection, BorderLayout.CENTER);
                    window.addPanels(this);

                }

                public void updatePanel() {
                    pnlMapMenu.updatePanel();
                    pnlMapSelection.updatePanel();
                }

                class PnlMapMenu extends JPanel {

                    private JButton topDown;
                    private JButton sideOn;

                    public PnlMapMenu() {

                        topDown = new JButton("Top Down");
                        sideOn = new JButton("Side On");
                        window.addPanels(this);

                        topDown.addActionListener(e -> {
                            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    pnlMapSelection.selectMap(pnlMapSelection.TOPDOWN);
                                }
                            });
                        });

                        sideOn.addActionListener(e -> {
                            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    pnlMapSelection.selectMap(pnlMapSelection.SIDEON);
                                }
                            });
                        });

                        JPanel dummyPanel = new JPanel();
                        dummyPanel.setLayout(new GridLayout(1,2,0,10));
                        dummyPanel.add(topDown);
                        dummyPanel.add(sideOn);

                        this.setLayout(new BorderLayout());

                        this.add(dummyPanel, BorderLayout.NORTH);

                    }

                    public void updatePanel() {
                        // Nothing gets updated
                    }

                }

                /**
                 * This panel gives the user options to swap between top down and side view panels.
                 */
                class PnlMapSelection extends JPanel {

                    private CardLayout layout;
                    public final String TOPDOWN = "top";
                    public final String SIDEON = "side";

                    private PnlTopView pnlTopView;
                    private PnlSideView pnlSideView;

                    public PnlMapSelection() {
                        super();
                        this.layout = new CardLayout();
                        this.setBackground(BLACK);
                        this.setLayout(layout);

                        this.pnlTopView = new PnlTopView();
                        this.pnlSideView = new PnlSideView();

                        this.add(this.pnlTopView, TOPDOWN);
                        this.add(this.pnlSideView, SIDEON);
                        window.addPanels(pnlTopView, pnlSideView, this);

                        selectMap(TOPDOWN);

                    }

                    public void updatePanel(){
                        this.pnlTopView.updatePanel();
                        this.pnlSideView.updatePanel();
                    }

                    public void selectMap(String str) {
                        layout.show(this, str);
                    }
                }


            }

            public class PnlTopView extends JPanel{

                public PnlTopView(){
                    super(new GridLayout(1,1,1,1));
                    this.setBackground(BACKGROUND_COLOR_GREY);
                    this.setPreferredSize(new Dimension(400,400));

                }

                public void updatePanel() {
                    if (selectedCalculation != null) {
                        updateMap(selectedCalculation);
                    }
                }

                public void updateMap(Calculation calc) {
                    // Has to be here all null pointer as runway can be empty
                    JPanel pnl = this;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            pnl.removeAll();

                            Runway runway = calc.getRunway();

                            JFXPanel topView = new TopView(calc, runway, pnl.getWidth(), pnl.getHeight());


                            topView.revalidate();
                            pnl.add(topView);

                            pnl.validate();
                            pnl.repaint();
                            pnl.revalidate();



                        }
                    });
                }

            }

            public class PnlSideView extends JPanel{

                public PnlSideView(){
                    super(new GridLayout(1,1,1,1));
                    this.setBackground(BACKGROUND_COLOR_GREY);

                    this.setPreferredSize(new Dimension(400,400));
                }

                public void updatePanel() {
                    if (selectedCalculation != null) {
                        updateMap(selectedCalculation);
                    }
                }

                public void updateMap(Calculation calc) {
                    // Has to be here all null pointer as runway can be empty
                    JPanel pnl = this;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            pnl.removeAll();

                            Runway runway = calc.getRunway();

                            JFXPanel sideView = new SideView(calc, runway, pnl.getWidth(), pnl.getHeight());
                            pnl.add(sideView);

                        }
                    });
                }
            }

            class PnlList extends JPanel {

                private JScrollPane pnlCalcHistory;

                public PnlList() {
                    super(new BorderLayout());


                    pnlCalcHistory = new JScrollPane();

                    // Instantiate table
                    table = new JTable(){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

                    // create a table model and set a Column Identifiers to this model
                    Object[] columns = {"ID", "Airport", "Runway", "Plane", "Obstacle", "TORA", "TODA", "ASDA", "LDA","Old TORA","Old TODA","Old ASDA","Old LDA"};
                    model = new DefaultTableModel(){

                        @Override
                        public boolean isCellEditable(int row, int column) {
                            //all cells false
                            return false;
                        }
                    };
                    model.setColumnIdentifiers(columns);


                    // set the model to the table
                    table.setModel(model);
                    table.setBackground(new Color(255, 255, 255));
                    table.setForeground(BLACK);

                    table.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    window.update();
                                }
                            });

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            JTable table =(JTable) e.getSource();
                            Point point = e.getPoint();
                            int row = table.rowAtPoint(point);
                            if (table.getSelectedRow() != -1) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnCalculationBreakdown.setEnabled(true);
                                    }
                                });
                            }
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    window.update();
                                }
                            });
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    });

                    // create JScrollPane for the table
                    pnlCalcHistory = new JScrollPane(table);
//                    pnlCalcHistory.setBounds(0, 0, 1000, 200);
                    JPanel dummyPanel = new JPanel();
                    dummyPanel.setLayout(new BorderLayout());
                    dummyPanel.add(pnlCalcHistory, BorderLayout.CENTER);

                    this.add(pnlCalcHistory,BorderLayout.CENTER);
                    window.addPanels(this);
                }

                public void updatePanel(){
                    int selectedRow = table.getSelectedRow();
                    int selectedColumn = table.getSelectedColumn();
                    int length = model.getRowCount();
                    for (int i = 0; i < length; i++){
                        model.removeRow(0);
                    }
                    for (Calculation c : calculator.getCalculations()) {
                        model.addRow(new Object[]{c,c.getRunway().getAirport(),c.getRunway(),c.getPlane(),c.getObstacle(),c.getNewTORA(),c.getNewTODA(),c.getNewASDA(),c.getNewLDA(),c.getRunway().getTORA(),c.getRunway().getTODA(),c.getRunway().getASDA(),c.getRunway().getLDA()});
                    }
                    try {
                        table.changeSelection(selectedRow,selectedColumn,false,false);
                        selectedCalculation = (Calculation) model.getValueAt(table.getSelectedRow(),0);
                    } catch (Exception e) {

                    }
                }

            }

            /**
             * Class displays both the Map panel and the List panel.
             */
            class PnlMapList extends JPanel {

                private GridLayout layout;

                private PnlMap pnlMap;
                private PnlList pnlList;

                public PnlMapList() {
                    super();
                    this.layout = new GridLayout(2, 1);

                    this.pnlMap = new PnlMap();
                    this.pnlList = new PnlList();

                    this.setLayout(layout);

                    this.add(this.pnlList);
                    this.add(this.pnlMap);

                    window.addPanels(pnlMap);

                }

                public void updatePanel(){
                    this.pnlMap.updatePanel();
                    this.pnlList.updatePanel();
                }

            }

        }

    }

    class PnlCalcMenu extends JPanel {

        private GridLayout layout;

        private JLabel lblAirport;
        private JLabel lblRunway;
        private JLabel lblPlane;
        private JLabel lblObstacle;
        private JLabel lblDistanceCentre;
        private JLabel lblDistanceThreshold;

        // Combo Boxes
        private JComboBox cbxObstacle;
        private JComboBox cbxPlane;
        private JComboBox cbxAirport;
        private JComboBox cbxRunway;

        // JTextFields
        private JTextField txtDistanceCentre;
        private JTextField txtDistanceThreshold;

        public PnlCalcMenu(){
            super();

            this.layout = new GridLayout(8, 1, 1, 10);

            /**
             * The dummy panels are used so that if the window is expanded, the calculation menu doesn't expand with it.
             */
            JPanel dummyPanel1 = new JPanel();
            dummyPanel1.setBackground(BACKGROUND_COLOR_GREY);

            JPanel dummyPanel2 = new JPanel();
            dummyPanel2.setLayout(layout);

            this.setLayout(new BorderLayout());

            this.add(dummyPanel1, BorderLayout.CENTER);
            this.add(dummyPanel2, BorderLayout.NORTH);


            // Labels
            this.lblAirport = new JLabel("Airport:");
            this.lblRunway = new JLabel("Runway:");
            this.lblPlane = new JLabel("Plane:" );
            this.lblObstacle = new JLabel("Obstacle:");
            this.lblDistanceCentre = new JLabel("Distance from centre line (m):  ");
            this.lblDistanceThreshold = new JLabel("Distance from threshold (m):");

            cbxAirport = new JComboBox();
            cbxRunway = new JComboBox();
            cbxPlane = new JComboBox();
            cbxObstacle = new JComboBox();
            txtDistanceCentre = new JTextField();
            txtDistanceThreshold = new JTextField();

            Border border = BorderFactory.createLineBorder(BACKGROUND_COLOR_GREY, 10);
            this.setBorder(border);

            cbxAirport.addActionListener(e -> {
                // Catch NPE if no box is selected.
                try {
                    for (Airport a : calculator.getAirports()) {
                        if (((Airport) cbxAirport.getSelectedItem()).getID() == a.getID()) {
                            cbxRunway.removeAllItems();
                            for (Runway r : a.getRunways()) {
                                cbxRunway.addItem(r);
                            }
                        }
                    }
                } catch (Exception ex) {

                }
            });



            ///////////////// Calculations \\\\\\\\\\\\\\\\\\\\\\\

            // create JButtons and drop downs calculation portion of home tab
            JButton btnCalculate = new JButton("Calculate");
            btnCalculationBreakdown = new JButton("Calculation Breakdown");

            // Add functionality to the calculate button
            btnCalculate.addActionListener(e ->{
                try {
                    // Get the require data
                    Obstacle obstacle = ((Obstacle) cbxObstacle.getSelectedItem());
                    Plane plane = ((Plane) cbxPlane.getSelectedItem());
                    Airport airport = ((Airport) cbxAirport.getSelectedItem());
                    Runway runway = ((Runway) cbxRunway.getSelectedItem());
                    double distanceFromThreshold = Double.parseDouble(txtDistanceThreshold.getText());
                    double distanceFromCentreLine = Double.parseDouble(txtDistanceCentre.getText());

                    // Create the new calculation
                    Calculation calc = new Calculation(runway, plane, obstacle, distanceFromThreshold, distanceFromCentreLine);

                    // Add calculation to the calculator.
                    calculator.addCalculation(calc);
                    window.update();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Please make sure all fields have values and are in the correct format. ");
                }
            });

            btnCalculationBreakdown.addActionListener(e -> {
                    Calculation calc = (Calculation) model.getValueAt(table.getSelectedRow(), 0);

                    CalcLog log = calc.getCalcLog();

                    JOptionPane.showMessageDialog(null, log.getLogsString());
            });

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    btnCalculationBreakdown.setEnabled(false);
                }
            });

            window.addAll(dummyPanel2, lblAirport, cbxAirport, lblRunway, cbxRunway, lblPlane, cbxPlane, lblObstacle, cbxObstacle,
                    lblDistanceCentre, txtDistanceCentre, lblDistanceThreshold, txtDistanceThreshold, btnCalculate, btnCalculationBreakdown);
            window.addLabels(lblAirport, lblRunway, lblPlane, lblObstacle, lblDistanceCentre, lblDistanceThreshold);
            window.addTextFields(txtDistanceCentre, txtDistanceThreshold);
            window.addButtons(btnCalculate,btnCalculationBreakdown);
            window.addPanels(this,dummyPanel1,dummyPanel2);
        }

        public void updatePanel() {
           updateComboBoxes();
        }

        public void updateComboBoxes(){
            // Clear the combo boxes
            cbxPlane.removeAllItems();
            cbxRunway.removeAllItems();
            cbxObstacle.removeAllItems();
            cbxAirport.removeAllItems();

            // Add data from the calculator to the combo boxes
            ArrayList<Plane> planes = calculator.getPlanes();
            ArrayList<Airport> airports = calculator.getAirports();
            ArrayList<Obstacle> obstacles = calculator.getObstacles();

            for (Plane x : planes) {
                cbxPlane.addItem(x);
            }

            for (Airport x : airports) {
                cbxAirport.addItem(x);
            }

            for (Obstacle x : obstacles) {
                cbxObstacle.addItem(x);
            }
        }


    }


}

