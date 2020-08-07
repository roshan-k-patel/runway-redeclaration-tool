package group5.frontend;

import group5.common.Calculator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;


public class FrontEndWindow {

    private Calculator calculator;

    private JFrame window;

    private ArrayList<JLabel> labels;
    private ArrayList<JTextField> textFields;
    private ArrayList<JButton> buttons;
    private ArrayList<JPanel> panels;

    private JPanel pnlHome;
    private JPanel pnlMenu;


    public static Border BLACKLINE = BorderFactory.createLineBorder(Color.decode("#133337"), 4, true);

    public FrontEndWindow(Calculator calculator){



        this.calculator = calculator;
        this.labels = new ArrayList<>();
        this.textFields = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.panels = new ArrayList<>();

        // Frame for the main window
        window = new JFrame();

        try {
            Image image = new ImageIcon("logo.png").getImage();
            window.setIconImage(image);
        }catch(Exception e){
            System.out.println("Application icon not found");
        }

        FrontEndWindow tempThis = this;

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                tempThis.update();
            }
        });

        // Create Panels
        pnlHome = new HomePanel(this, calculator);
        pnlMenu = new MenuPanel(this, calculator);

        // Add panels to the list of panels.
        addPanels(pnlHome, pnlMenu);

        // Set settings for panels
        for (JPanel p : panels) {
            //dark mode
            p.setBackground(new Color(63, 65, 69));
            //light-mode
            // p.setBackground(new Color(225,225,225));

        }

        // Set settings for buttons
        for (JButton b : buttons) {
            b.setBackground(new Color(103, 134, 219));
            //dark-mode
            b.setForeground(new Color(0, 0, 0));
            //light-mode
            // b.setForeground(new Color(15, 15, 15));
            b.setFocusPainted(false);
            Border border = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
            b.setBorder(border);
        }

        // Set settings for text fields
        for (JTextField t : textFields) {
            t.setBackground(new Color(255, 255, 255));
            t.setForeground(new Color(0, 0, 0));
            t.setBorder(null);
        }

        // Set settings for labels
        for (JLabel l : labels) {
            //dark-mode
            l.setForeground(new Color(225, 225, 225));
            //light-mode
            //   l.setForeground(new Color(0,0,0));
            l.setBorder(null);
        }




        window.setLayout(new BorderLayout());
        window.add(pnlMenu,BorderLayout.NORTH);
        window.add(pnlHome,BorderLayout.CENTER);
        window.getContentPane().setBackground(new Color(103, 134, 219));
        window.setVisible(true);
        window.setSize(1280, 800);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void update() {
        ((HomePanel) pnlHome).updatePanel();
        ((MenuPanel) pnlMenu).updateComboBoxes();
        ((MenuPanel) pnlMenu).updateAirportJtable();
        ((MenuPanel) pnlMenu).updatePlaneJtable();
        ((MenuPanel) pnlMenu).updateObstaclesJtable();
        ((MenuPanel) pnlMenu).updateRunwaysJtable();
        ((MenuPanel) pnlMenu).updateChangeLog();


    }

    /**
     * Call this method when planes/airports/runways/obstacles are changed, or when they could have been changed.
     */
    public void updateComboBoxes() {

    }


    public void addAll(JPanel panel, JComponent... cs) {
        for (JComponent c : cs) {
            panel.add(c);
        }
    }

    public void addTextFields(JTextField... txts) {
        for (JTextField txt : txts) {
            textFields.add(txt);
        }
    }

    public void addLabels(JLabel... lbls) {
        for (JLabel lbl : lbls) {
            labels.add(lbl);
        }
    }

    public void addButtons(JButton... btns) {
        for (JButton btn : btns) {
            buttons.add(btn);
        }
    }

    public void addPanels(JPanel... pnls) {
        for (JPanel pnl : pnls) {
            panels.add(pnl);
        }
    }

    public void addToArrayList(ArrayList al, JComponent... cs) {
        for (JComponent c : cs) {
            al.add(c);
        }
    }

    public void makeWindowFitScreen(JFrame window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize(screenSize.width, screenSize.height);
    }

    public void darkMode(){
        for (JPanel p : panels) {
            //dark mode
            p.setBackground(new Color(63, 65, 69));
            Border border = BorderFactory.createLineBorder(new Color(63, 65, 69));
            p.setBorder(border);
        }

        for (JButton b : buttons) {
            //dark-mode
            b.setForeground(new Color(0, 0, 0));
        }

        // Set settings for labels
        for (JLabel l : labels) {
            //dark-mode
            l.setForeground(new Color(225, 225, 225));
        }

    }

    public void lightMode(){
        for (JPanel p : panels) {
            //light-mode
             p.setBackground(new Color(225,225,225));
            Border border = BorderFactory.createLineBorder(new Color(255, 255, 255) );
                p.setBorder(border);
        }

        for (JButton b : buttons) {
            //light-mode
             b.setForeground(new Color(15, 15, 15));

        }

        // Set settings for labels
        for (JLabel l : labels) {
            //light-mode
            l.setForeground(new Color(0,0,0));
        }

    }

    public void colourblindMode(){

        for (JPanel p : panels) {
            //light-mode
            p.setBackground(new Color(33,142,150));
            Border border = BorderFactory.createLineBorder(new Color(226,197, 43) );
            p.setBorder(border);
        }

        for (JButton b : buttons) {
            //light-mode
            b.setForeground(new Color(226,197, 43));

        }

        // Set settings for labels
        for (JLabel l : labels) {
            //light-mode
            l.setForeground(new Color(226,197, 43));
        }
    }

    public JFrame getWindow(){
        return this.window;
    }





}