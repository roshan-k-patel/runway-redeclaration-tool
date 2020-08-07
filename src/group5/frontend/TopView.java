package group5.frontend;

import group5.common.Calculation;
import group5.common.Runway;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;


public class TopView extends JFXPanel {

    GraphicsContext gc;
    Canvas canvas;
    Calculation calculation;
    Runway runway;

    int x1,x2,y1,y2,width, height;
    int difference;
    // TODO change to float later for more accurate scaling?
    double scaleFactor;
    double TORA, TODA, ASDA, LDA, length, dfc, dft, blast;
    Color blind;
    Boolean isBlind;

    public TopView(Calculation calculation, Runway runway, int width, int height) {
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        this.calculation = calculation;
        this.runway = runway;
        this.width = width;
        this.height = height;
        x1 = width/8;
        x2 = (width/8)*7+20;
        y1 = height/4;
        y2 = 90;
        difference = (height-(y1+y2))/7;
       /* this.repaint();
        this.revalidate();*/
        // how to scale it from "3360m" to a 500 length picture
        // here it is the length of picture runway / toda of rnuwway
        scaleFactor = (x2-x1)/runway.getLength();

        TORA = calculation.getNewTORA()*scaleFactor;    // 100 for proof
        TODA = calculation.getNewTODA()*scaleFactor;
        ASDA = calculation.getNewASDA()*scaleFactor;    // 300 for proof
        LDA = calculation.getNewLDA()*scaleFactor;
        length = runway.getLength()*scaleFactor;
        dfc = calculation.getDistanceFromCentreLine()*scaleFactor;
        dft = calculation.getDistanceFromThreshold()*scaleFactor;
        blind = Color.GREEN;
        isBlind = false;
        blast = calculation.getPlane().getBlastProtect()*scaleFactor;
        Platform.runLater(() -> {
            initFX(this);
            fillBackground(width, height, Color.GREEN);
            // use runway.getToda instead of new toda as this was the toda before
            drawImages();
            drawMeasurements();

        });

    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    public Scene createScene() {
        Group root  =  new  Group();
        root.getChildren().add(canvas);

        if(runway.isTopRunway()){
            if(calculation.getLandingTowardStatus()){
                drawRightArrow(root, "Landing towards obstacle");
            } else{
                drawLeftArrow(root, "Landing away from obstacle");
            }
        } else{
            if(calculation.getLandingTowardStatus()){
                drawLeftArrow(root, "Landing towards obstacle");
            } else{
                drawRightArrow(root, "Landing away from obstacle");
            }
        }
        Button btnColourBlind = new Button("Colour Blind Mode");
        btnColourBlind.setLayoutX(canvas.getWidth()-120);
        btnColourBlind.setLayoutY(canvas.getHeight()-30);

        this.btnColourBlind(btnColourBlind);
        root.getChildren().add(btnColourBlind);


        drawCompass(root);
        Scene  scene  =  new  Scene(root, 620, 350);
        return (scene);
    }

    protected void fillBackground(int width, int height, Color color){
        gc.setFill(color);
        gc.fillRect(0,0, width, height);
    }
    protected void btnColourBlind(Button btnColourBlind ){
        btnColourBlind.setLayoutX(canvas.getWidth()-120);
        btnColourBlind.setLayoutY(canvas.getHeight()-30);
        btnColourBlind.setOnMouseEntered(e ->{
            btnColourBlind.setStyle("-fx-background-color: transparent, -fx-shadow-highlight-color;");
        });
        btnColourBlind.setOnMouseExited(e->{
            btnColourBlind.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        });
        btnColourBlind.setOnMouseClicked(e->{

                if(!isBlind){
                    isBlind = true;
                    blind = Color.rgb(226,197, 43);
                    fillBackground(width, height, blind);
                    drawImages();
                    drawMeasurements();
                } else{
                    isBlind = false;
                    blind = Color.GREEN;
                    fillBackground(width, height, blind);
                    drawImages();
                    drawMeasurements();
                }
        });
    }
    /////////////////////////////////////////// MEASUREMENTS //////////////////////////////////////////////////

    protected void drawMapScale(int length) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 16));
        int y3 = (int) (height-6.7*difference);
        // we have the +_2 so the vertical line is actually visible and not just a point
        gc.strokeLine(x1, y3, x2, y3); //horizontal line
        gc.strokeLine(x1, y3+2, x1, y3-2);  //vertical line at beginning
        gc.strokeLine(x2, y3+2, x2, y3-2);//vertical line at end
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(String.valueOf(length)+"m", x1, y3+15);
    }

    protected void drawMeasuringLine(double x, double length, double y, String text) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.CENTER);

        gc.strokeLine(x, y, x + length, y);
        gc.strokeLine(x, y + 2, x, y - 2);
        gc.strokeLine(x + length, y + 2, x + length, y - 2);
        gc.fillText(text, x + length / 2, y + 15);
    }
    protected void drawVerticalLine(double x, double length, double y, String text) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.CENTER);

        gc.strokeLine(x, y+length, x, y);
        gc.strokeLine(x+2, y, x-2, y);
        gc.strokeLine(x + 2, y + length, x -2, y + length);
        gc.fillText(text, x + 15, y + length/2 -10);
    }

    protected void drawClearway(int start, int multiplier) {
        int clearway = (int) (TODA/scaleFactor - TORA/scaleFactor);
        drawMeasuringLine(start+multiplier*TORA, multiplier*(clearway*scaleFactor), height-difference, "Clearway: "+clearway+"m");
    }
    protected void drawStopway(int start, int multiplier) {
        int stopway = (int) (ASDA/scaleFactor - TORA/scaleFactor);
        drawMeasuringLine(start+multiplier*TORA, multiplier*(stopway*scaleFactor),height-2*difference , "Stopway: "+stopway+"m");
    }

    protected void drawMeasurements(){
        drawMapScale((int) runway.getLength());
        int obstacleWidth = 10;
        if(runway.isTopRunway()){
            int resa = (int) ((runway.getDisplacedThreshold()*scaleFactor+x1+obstacleWidth)-(x1+obstacleWidth));
            drawMeasuringLine(x2-dft, -TODA, height-6*difference, "TODA: " + (int) calculation.getNewTODA()+"m");
            drawMeasuringLine(x2-dft, -TORA, height-5*difference, "TORA: " + (int) calculation.getNewTORA()+"m");
            drawMeasuringLine(x2-dft, -ASDA, height-4*difference, "ASDA: " + (int) calculation.getNewASDA()+"m");
            drawMeasuringLine(x2-runway.getDisplacedThreshold()*scaleFactor-dft, -LDA, height-3*difference, "LDA: " + (int) calculation.getNewLDA()+"m");
            drawClearway((int) (x2-dft), -1);
            drawStopway((int) (x2-dft), -1);
            drawMeasuringLine(x2-dft, -resa, height-3*difference, "RESA: " + (int) calculation.getRunway().getRESA()+"m");
            drawMeasuringLine(x2-dft-TODA  , -blast, height-6*difference, "Blast allowance: " + (int) calculation.getPlane().getBlastProtect()+"m");
            drawVerticalLine(x2-runway.getDisplacedThreshold()*scaleFactor-dft, y2, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());
        } else{
            int resa = (int) ((runway.getDisplacedThreshold()*scaleFactor+x1)-(x1));
            drawMeasuringLine(x1+dft, TODA, height-6*difference, "TODA: " + (int) calculation.getNewTODA()+"m");
            drawMeasuringLine(x1+dft, TORA, height-5*difference, "TORA: " + (int) calculation.getNewTORA()+"m");
            drawMeasuringLine(x1+dft, ASDA, height-4*difference, "ASDA: " + (int) calculation.getNewASDA()+"m");
            drawMeasuringLine(runway.getDisplacedThreshold()*scaleFactor+x1+dft, LDA, height-3*difference, "LDA: " + (int) calculation.getNewLDA()+"m");
            drawClearway((int) (x1+dft), 1);
            drawStopway((int) (x1+dft), 1);
            drawMeasuringLine(x1+dft, resa, height-3*difference, "RESA: " + (int) calculation.getRunway().getRESA()+"m");
            drawMeasuringLine(x1+dft+TODA  , blast, height-6*difference, "Blast allowance: " + (int) calculation.getPlane().getBlastProtect()+"m");
            drawVerticalLine(runway.getDisplacedThreshold()*scaleFactor+x1+dft, y2, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());

        }


    }

    ////////////////////////////////////////////// IMAGES ///////////////////////////////////////////////////////////

    protected void drawRunway(){
        int yRunwayStart = y1;
        int xRunwayStart = x1;
        int width = x2-x1;
        int height = y2;
        int b1 = calculation.getRunway().getBearing();
        int b2;
        String p1 = String.valueOf(calculation.getRunway().getPosition());
        String p2;

        String bottomRunwayName;
        String topRunwayName;
        if(calculation.getRunway().getParallel() == null){
            if (runway.isTopRunway()){
                topRunwayName = b1 + p1;
                bottomRunwayName = "";
            } else {
                topRunwayName = "";
                bottomRunwayName = b1 + p1;
            }
        } else{
            b2 = calculation.getRunway().getParallel().getBearing();
            p2 =  String.valueOf(calculation.getRunway().getParallel().getPosition());
            if (runway.isTopRunway()){
                topRunwayName = b1 + p1;
                bottomRunwayName = b2 + p2;
            } else {
                topRunwayName = b2 + p2;
                bottomRunwayName = b1 + p1;
            }
        }

        // drawing clear and graded areas
        gc.setFill(Color.rgb(33,142,150));
        gc.fillRect(xRunwayStart-25, yRunwayStart-25, width+50, height+50);
        int w = 400;
        gc.fillRect(xRunwayStart+width/2-w/2, yRunwayStart-50, w, height+100);
        gc.setFill(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Clear and Graded Areas", xRunwayStart+width/2-w/2+w-65, yRunwayStart-40);


        gc.setFill(Color.rgb(104,104,104));
        gc.fillRect(xRunwayStart, yRunwayStart, width, height);

        //this.drawRunwayMarker(xRunwayStart, yRunwayStart+height/2, 255, 255, 255);
        int gap = 40;
        for(int noOfLines = 0; noOfLines < width; noOfLines = noOfLines+gap){
            this.drawRunwayMarker(xRunwayStart+noOfLines, yRunwayStart+height/2, 20, 5);
        }

        gc.setFont(Font.font("Consolas", 30));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(bottomRunwayName, xRunwayStart+50, yRunwayStart+80);

        gc.fillText(topRunwayName, xRunwayStart+width-50, yRunwayStart+30);
    }

    protected void drawRunwayMarker(int xStart, int yStart, int w, int h){
        //gc.strokeRect(xStart, yStart, width, height);
        gc.setFill(Color.rgb(255,255,255));
        gc.fillRect(xStart, yStart, w, h);
    }

    protected void drawCompass(Group root){
        final boolean[] isRotated = {false};
        int compassWidth = 70;
        int compassHeight = 70;
        int x = width - compassWidth - 20;
        int y = height - compassHeight - 40;
        Image image = new Image(getClass().getResource("compass.png").toExternalForm(), compassWidth, compassHeight, true, true);
        Button btnCompass = new Button();

        btnCompass.setStyle("-fx-background-color: transparent;");
        btnCompass.setTranslateX(x);
        btnCompass.setTranslateY(y);
        btnCompass.setGraphic(new ImageView(image));

      /*  btnCompass.setOnMouseEntered(e ->{
            btnCompass.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        });*/
       /* btnCompass.setOnMouseExited(e->{
            btnCompass.setStyle("-fx-background-color: transparent, -fx-shadow-highlight-color;");
        });
        btnCompass.setOnMouseClicked(e->{

        });*/
        root.getChildren().add(btnCompass);
    }

    protected void drawLeftArrow(Group root, String direction){
        int arrowWidth = 100;
        int arrowHeight = 40;
        int x = (int) canvas.getWidth()/2-arrowWidth/2;
        int y = 30;
        Image image = new Image(getClass().getResource("leftplane.png").toExternalForm(), arrowWidth, arrowHeight, true, true);
        Button btnLeft = new Button();
        btnLeft.setStyle("-fx-background-color: transparent;");
        btnLeft.setTranslateX(x);
        btnLeft.setTranslateY(y);
        btnLeft.setGraphic(new ImageView(image));

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(direction, x, y+20);

        root.getChildren().add(btnLeft);
    }
    protected void drawRightArrow(Group root, String direction){
        int arrowWidth = 100;
        int arrowHeight = 40;
        int x = (int) canvas.getWidth()/2-arrowWidth/2;
        int y = 30;
        Image image = new Image(getClass().getResource("rightplane.png").toExternalForm(), arrowWidth, arrowHeight, true, true);
        Button btnLeft = new Button();
        btnLeft.setStyle("-fx-background-color: transparent;");
        btnLeft.setTranslateX(x);
        btnLeft.setTranslateY(y);
        btnLeft.setGraphic(new ImageView(image));

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(direction, x, y+20);

        root.getChildren().add(btnLeft);
    }

    protected void drawObstacle(int width, int height){
        int distanceFromCentreLine;
        int distanceFromThreshold;
        if (runway.isTopRunway()){
            distanceFromCentreLine = (int) (y1+y2/2 -dfc);
            distanceFromThreshold = (int) (x2 - dft -width);
            gc.setFill(Color.RED);
            gc.fillOval(distanceFromThreshold+width/2,distanceFromCentreLine , width, height);
            gc.setFill(Color.rgb(164,20,0));
            gc.setFont(Font.font("Consolas", 12));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(calculation.getObstacle().getName(),distanceFromThreshold-width/2,distanceFromCentreLine + 20);
        } else{
            distanceFromCentreLine = (int) (y1+y2/2 +dfc);
            distanceFromThreshold = (int) (x1+dft-width);
            gc.setFill(Color.RED);
            gc.fillOval(distanceFromThreshold+width/2,distanceFromCentreLine , width, height);
            gc.setFill(Color.rgb(164,20,0));
            gc.setFont(Font.font("Consolas", 12));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(calculation.getObstacle().getName(),distanceFromThreshold-width/2,distanceFromCentreLine + 20);
        }





    }

    protected void drawImages(){
        drawRunway();
        drawObstacle(10,10);

    }
 /*   //////////////////////////////////////////// ROTATED RUNWAY ///////////////////////////////////////////////////////
    protected void drawRotatedRunway(){
        int x1 = width/4;
        int x2 = 90;
        int y1 = height/8;
        int y2 = (height/8)*7;
        int xRunwayStart = x1;
        int yRunwayStart = y1;
        int width = x2;
        int height = y2-y1;
        difference = (width-(x1+x2))/7;
        // how to scale it from "3360m" to a 500 length picture
        // here it is the length of picture runway / toda of rnuwway
        double rotationalScaleFactor =  ((y2-y1)/runway.getLength());

        double rTORA = (calculation.getNewTORA()*rotationalScaleFactor);    // 100 for proof
        double rTODA = (calculation.getNewTODA()*rotationalScaleFactor);
        double rASDA = (calculation.getNewASDA()*rotationalScaleFactor);    // 300 for proof
        double rLDA = (calculation.getNewLDA()*rotationalScaleFactor);
        double rLength =(runway.getLength()*rotationalScaleFactor);
        double rDfc = (calculation.getDistanceFromCentreLine()*rotationalScaleFactor);
        double rDft = (calculation.getDistanceFromThreshold()*rotationalScaleFactor);


        int b1 = calculation.getRunway().getBearing();
        int b2;
        String p1 = String.valueOf(calculation.getRunway().getPosition());
        String p2;

        String bottomRunwayName;
        String topRunwayName;
        if(calculation.getRunway().getParallel() == null){
            if (runway.isTopRunway()){
                topRunwayName = b1 + p1;
                bottomRunwayName = "N/A";
            } else {
                topRunwayName = "N/A";
                bottomRunwayName = b1 + p1;
            }
        } else{
            b2 = calculation.getRunway().getParallel().getBearing();
            p2 =  String.valueOf(calculation.getRunway().getParallel().getPosition());
            if (runway.isTopRunway()){
                topRunwayName = b1 + p1;
                bottomRunwayName = b2 + p2;
            } else {
                topRunwayName = b2 + p2;
                bottomRunwayName = b1 + p1;
            }
        }



        gc.setFill(Color.rgb(104,104,104));
        gc.fillRect(xRunwayStart, yRunwayStart, width, height);

        int gap = 40;
        for(int noOfLines = 0; noOfLines < height; noOfLines = noOfLines+gap){
            this.drawRunwayMarker(xRunwayStart+width/2, yRunwayStart+noOfLines, 5, 20);
        }
        gc.setFont(Font.font("Consolas", 15));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(topRunwayName, xRunwayStart+width-70, yRunwayStart+30);
        gc.fillText(bottomRunwayName, xRunwayStart+70, yRunwayStart+height-20);

        drawRotatedMeasuringLine(width+difference, rTODA, y1, "TODA: " + calculation.getNewTORA());
        *//*drawRotatedMeasuringLine(width+2*difference, TORA, y1, "TORA: " + calculation.getNewTODA());
        drawRotatedMeasuringLine(height+3*difference, ASDA, y1, "ASDA: " + calculation.getNewASDA());
        drawRotatedMeasuringLine(height+4*difference, LDA, y1, "LDA: " + calculation.getNewLDA());*//*


    }
    protected void drawRotatedMeasuringLine(double x, double length, double y, String text) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.CENTER);
//                           bottom       top of tora
        gc.strokeLine(x, y, x, y+length);
        gc.strokeLine(x + 2, y, x - 2, y);
        gc.strokeLine(x + 2, y + length, x -2, y + length);
        gc.fillText(text, x + 15, y + length / 2);
    }*/
}











