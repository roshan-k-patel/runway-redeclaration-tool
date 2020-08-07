package group5.backend;

import group5.common.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Filehandler class which will handle loading data from files
 * I used some code from this website to help parse the XML documents.
 * https://www.journaldev.com/898/read-xml-file-java-dom-parser
 */
public class FileHandler
{

    /**
     * The following must match the expected XML file input. These are the expected tags in the XML file.
     */
    public static final String TAG_HEIGHT = "height";
    public static final String TAG_NAME = "name";
    public static final String TAG_BLAST_PROTECTION = "blast_protection";
    public static final String TAG_RATIO = "ratio";
    public static final String TAG_POSITION = "position";
    public static final String TAG_TORA = "tora";
    public static final String TAG_TODA = "toda";
    public static final String TAG_ASDA = "asda";
    public static final String TAG_LDA = "lda";
    public static final String TAG_STRIP_END = "strip_end";
    public static final String TAG_DISPLACED_THRESHOLD = "displaced_threshold";
    public static final String TAG_RESA = "resa";
    public static final String TAG_BEARING = "bearing";
    public static final String TAG_RUNWAY = "runway";
    public static final String TAG_RUNWAY_AIRPORT = "runway_airport";
    public static final String TAG_CALCULATION = "calculation";
    public static final String TAG_AIRPORT = "airport";
    public static final String TAG_PLANE = "plane";
    public static final String TAG_OBSTACLE = "obstacle";
    public static final String TAG_DISTANCE_FROM_THRESHOLD = "distance_from_threshold";
    public static final String TAG_DISTANCE_FROM_CENTER_LINE = "distance_from_center_line";
    private static final String TAG_CALC_AIRPORT =  "calc_airport";
    private static final String TAG_CALC_RUNWAY =  "calc_runway";
    private static final String TAG_CALC_OBSTACLE=  "calc_obstacle";
    private static final String TAG_CALC_PLANE =  "calc_plane";

    private Coordinator coordinator; // Used to add the read inputs to the coordinator, or to write from the coordinator // TODO

    private String filepath;

    /**
     * Constructor which takes the file path of an XML file, and a coordinator which the data is to be read or written to.
     * @param filepath
     * @param coordinator
     */
    public FileHandler(String filepath, Coordinator coordinator){
        this.coordinator = coordinator;
        this.filepath = filepath;

    }

    public void save() throws IOException {
        FileWriter file = new FileWriter(filepath);

        String text = "<root>\n";

        synchronized (coordinator) {
            for (Airport a : coordinator.getAirports()){
                text += convertAirport(a) + "\n";

                for (Runway r : a.getRunways()) {
                    text += convertRunway(r);
                    text += "\n";
                }

            }

            text += "\n";
            for (Obstacle o : coordinator.getObstacles()) {
                text += convertObstacle(o);
                text += "\n";
            }

            text += "\n";
            for (Plane p : coordinator.getPlanes()) {
                text += convertPlane(p);
                text += "\n";
            }

            text += "\n";
            for (Calculation c : coordinator.getCalculations()) {
                text += convertCalculation(c);
                text += "\n";
            }
        }

        text += "\n</root>";

        file.write(text);

        file.close();

    }

    public String convertRunway(Runway r) {
        String value = convertToXML2("position",String.valueOf(r.getPosition())) + "\n\t" +
                       convertToXML2("bearing",String.valueOf(r.getBearing())) + "\n\t" +
                       convertToXML2("tora",String.valueOf(r.getTORA())) + "\n\t" +
                       convertToXML2("toda",String.valueOf(r.getTODA())) + "\n\t" +
                       convertToXML2("asda",String.valueOf(r.getASDA())) + "\n\t" +
                       convertToXML2("lda",String.valueOf(r.getLDA())) + "\n\t" +
                       convertToXML2("displaced_threshold",String.valueOf(r.getDisplacedThreshold())) + "\n\t" +
                       convertToXML2("strip_end",String.valueOf(r.getStripEnd())) + "\n\t" +
                       convertToXML2("resa",String.valueOf(r.getRESA())) + "\n\t" +
                       convertToXML2("name",String.valueOf(r.getName())) + "\n\t" +
                       convertToXML2("runway_airport",String.valueOf(r.getAirport().getName()));
        return convertToXML("runway",value);
    }

    public String convertCalculation(Calculation c) {
        String value = convertToXML2("calc_plane", c.getPlane().getName()) + "\n\t" +
                       convertToXML2("calc_obstacle", c.getObstacle().getName()) + "\n\t" +
                       convertToXML2("calc_runway", c.getRunway().getName()) + "\n\t" +
                       convertToXML2("calc_airport",c.getRunway().getAirport().getName()) + "\n\t" +
                       convertToXML2("distance_from_threshold", String.valueOf(c.getDistanceFromThreshold())) + "\n\t" +
                       convertToXML2("distance_from_center_line", String.valueOf(c.getDistanceFromCentreLine()));
        return convertToXML("calculation",value);
    }

    public String convertAirport(Airport a) {
        String value = convertToXML2("name",a.getName());
        return convertToXML("airport",value);
    }

    public String convertPlane(Plane p) {
        String value = convertToXML2("blast_protection",String.valueOf(p.getBlastProtect())) + "\n\t" +
                       convertToXML2("ratio",String.valueOf(p.getRatio())) + "\n\t" +
                       convertToXML2("name",p.getName());
        return convertToXML("plane",value);
    }

    public String convertObstacle(Obstacle o){
        String value = convertToXML2("height",String.valueOf(o.getHeight())) + "\n\t" +
                       convertToXML2("name",o.getName());
        return convertToXML("obstacle", value);
    }

    public String convertToXML(String tag, String value){
        return "\t<" + tag + ">\n\t" + value + "\n\t</" + tag + ">";
    }

    public String convertToXML2(String tag, String value) {
        return "\t<" + tag + ">" + value + "</" + tag + ">";
    }


    /** * This function takes in a document, and pushes the read information to the coordinator class.
     */
    public void load() {

        // Open the file and create needed objects.
        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();

        DocumentBuilder builder; // Creates the required objects and passes them to load()
         try {
             builder = factory.newDocumentBuilder();
             Document document = builder.parse(xmlFile);
             document.getDocumentElement().normalize();
             // Creates a NodeList from the documents, identifying by the tag used.
             NodeList airportNodeList = document.getElementsByTagName(TAG_AIRPORT);
            NodeList runwayNodeList = document.getElementsByTagName(TAG_RUNWAY);
            NodeList calculationNodeList = document.getElementsByTagName(TAG_CALCULATION);
            NodeList obstacleNodeList = document.getElementsByTagName(TAG_OBSTACLE);
            NodeList planeNodeList = document.getElementsByTagName(TAG_PLANE);

            // Starts by creating the data which does not require from others (Airport, obstacle and planes are not dependent)
            ArrayList<Airport> airports = loadNodeList(airportNodeList,new ArrayList<Airport>(),TAG_AIRPORT);
            ArrayList<Obstacle> obstacles = loadNodeList(obstacleNodeList,new ArrayList<Obstacle>(),TAG_OBSTACLE);
            ArrayList<Plane> planes = loadNodeList(planeNodeList,new ArrayList<Plane>(),TAG_PLANE);

            // Adds the data before moving onto the dependent data.
            coordinator.addAirports(airports);
            coordinator.addObstacle(obstacles);
            coordinator.addPlanes(planes);

            // Creates a list of POGO classes for runways, storing the runway with a paired airport name.
            ArrayList<Runway> runways = loadNodeList(runwayNodeList,new ArrayList<Runway>(),TAG_RUNWAY);

            for (Runway r : runways) {
                coordinator.addRunway(r);
            }


            // Once everything else is done, this assumes that all the data needed is in the coordinator.
            ArrayList<Calculation> calculations = loadNodeList(calculationNodeList, new ArrayList<Calculation>(),TAG_CALCULATION);

            coordinator.addCalculations(calculations);

         } catch (ParserConfigurationException e) {
             e.printStackTrace();
         } catch (SAXException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    /**
     * Generic type method which will parse each nodeList for each node found. For example, will call parseAirport on all airport nodes.
     * @param nodeList
     * @param list
     * @param type
     * @param <E>
     * @return
     */
    private <E> ArrayList<E> loadNodeList(NodeList nodeList, ArrayList<E> list,String type) {
        if (list == null) {
            throw new NullPointerException("List was null");
        }
        for (int i = 0; i < nodeList.getLength(); i++) {

            if (type.equals(TAG_AIRPORT)) {
                list.add((E) parseAirport(nodeList.item(i)));
            } else if (type.equals(TAG_RUNWAY)) {
                list.add((E) parseRunway(nodeList.item(i)));
            } else if (type.equals(TAG_PLANE)) {
                list.add((E) parsePlane(nodeList.item(i)));
            } else if (type.equals(TAG_OBSTACLE)) {
                list.add((E) parseObstacle(nodeList.item(i)));
            } else if (type.equals(TAG_AIRPORT)) {
                list.add((E) parseAirport(nodeList.item(i)));
            } else if (type.equals(TAG_CALCULATION)) {
                list.add((E) parseCalculation(nodeList.item(i)));
            }

        }
        return list;

    }

    /**
     * This method takes in a node, and builds an airport using the data from the tags.
     * @param node The node which has the airport information within.
     * @return
     */
    private static Airport parseAirport(Node node){
        Airport airport = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            // Uses data directly from the XML file to build the airport.
            Element element = (Element) node;
            String name = getTagValue(TAG_NAME,element);

            airport = new Airport(name);
        }
        return airport;
    }

    /**
     * This method takes in a node, and builds an calculation using the data from the tags.
     * @param node The node which has the calculation information within.
     * @return
     */
    private Calculation parseCalculation(Node node) {
        Calculation calculation = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            // Uses the lists in the coordinator to find the other objects needed.
            Plane plane = coordinator.getPlane(getTagValue(TAG_CALC_PLANE,element));
            Obstacle obstacle = coordinator.getObstacle(getTagValue(TAG_CALC_OBSTACLE,element));
            String runwayName = getTagValue(TAG_CALC_RUNWAY,element);
            Airport airport = coordinator.getAirport(getTagValue(TAG_CALC_AIRPORT,element));

            // These inputs are directly from the XML file.
            double distanceFromThreshold = Double.parseDouble(getTagValue(TAG_DISTANCE_FROM_THRESHOLD,element));
            double distanceFromCenterLine = Double.parseDouble(getTagValue(TAG_DISTANCE_FROM_CENTER_LINE,element));

            // eMakes sure that the runway found is in the specified airport.
            Runway runway = null;
            for (Runway r : airport.getRunways()){
                if (r.getName().equals(runwayName)){
                    runway = r;
                }
            }

            // Builds the calculation using the data provided.
            calculation = new Calculation(runway,plane,obstacle,distanceFromThreshold,distanceFromCenterLine);

        }
        return calculation;
    }

    // Class used to store an input runway with its airport name
    private final class RunwayAirport {
        private String airportName;
        private Runway runway;

        public RunwayAirport(String airportName, Runway runway){
            this.airportName = airportName;
            this.runway =  runway;
        }
        public Runway getRunway() {
            return runway;
        }

        public String getAirportName(){
            return airportName;
        }
    }

    /**
     * This class builds a runway paired with an airport name from the data within the node.
      * @param node Stores the data within the node.
     * @return
     */
    private Runway parseRunway(Node node) {
        Runway runway = null;
        String airportName = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            // Uses data directly from the XML file to build the runway.
            char position = getTagValue(TAG_POSITION,element).charAt(0);
            int bearing = Integer.parseInt(getTagValue(TAG_BEARING,element));
            double tora = Double.parseDouble(getTagValue(TAG_TORA,element));
            double toda = Double.parseDouble(getTagValue(TAG_TODA,element));
            double asda = Double.parseDouble(getTagValue(TAG_ASDA,element));
            double lda = Double.parseDouble(getTagValue(TAG_LDA, element));
            double displacedThreshold = Double.parseDouble(getTagValue(TAG_DISPLACED_THRESHOLD,element));
            double stripEnd = Double.parseDouble(getTagValue(TAG_STRIP_END,element));
            double resa = Double.parseDouble(getTagValue(TAG_RESA,element));
            String name = getTagValue(TAG_NAME,element);
            airportName = getTagValue(TAG_RUNWAY_AIRPORT,element);

            runway = new Runway(position,bearing,tora,toda,asda,lda,displacedThreshold,stripEnd,resa,name, coordinator.getAirport(airportName),5000);

            // Stores the airport name which the runway is associated with.

        }
        return runway;
    }

    /**
     * Using the information in the node, builds a plane object.
     * @param node Used to build the plane
     * @return
     */
    private Plane parsePlane(Node node) {
        Plane plane = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            // Uses the data directly from the XML file to build a plane.
            String name = getTagValue(TAG_NAME,element);
            double blastProtection = Double.parseDouble(getTagValue(TAG_BLAST_PROTECTION,element));
            double ratio = Double.parseDouble(getTagValue(TAG_RATIO,element));


            plane = new Plane(name, blastProtection, ratio);

        }

        return plane;
    }

    /**
     * Builds an obstacle from the given data from the XML file.
     * @param node Stores the data from the XML file.
     * @return
     */
    private static Obstacle parseObstacle(Node node) {
        Obstacle obstacle = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            // Builds an obstacle from data directly from the XML file.
            Element element = (Element) node;
            double height = Double.parseDouble(getTagValue(TAG_HEIGHT, element));
            String name = getTagValue(TAG_NAME, element);

            obstacle = new Obstacle(height,name);
        }

        return obstacle;
    }

    /**
     * This method takes in a tag (the tag found in the XML file) and an element, and returns the value found inbetween the tags.
     * This method was heavily influenced by the following website's informatoin:
     * https://www.journaldev.com/898/read-xml-file-java-dom-parser
     * @param tag The name of the tag
     * @param element The element given.
     * @return
     */
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue(); // Returns the value inbetween the nodes.
    }

}
