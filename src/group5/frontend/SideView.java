package group5.frontend;

import group5.common.Calculation;
import group5.common.Runway;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
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

public class SideView extends JFXPanel{

    GraphicsContext gc;
    Canvas canvas;
    Calculation calculation;
    Runway runway;

    int x1,x2,y1,y2,width, height;
    int difference;
    // TODO change to float later for more accurate scaling?
    double scaleFactor;
    double TORA, TODA, ASDA, LDA, length, dfc, dft, resa, stripend, blast, obstacleHeight;
    int obstacleWidth = 10;
    Color blind;
    Boolean isBlind;

    public SideView(Calculation calculation, Runway runway, int width, int height) {
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        this.calculation = calculation;
        this.runway = runway;
        this.width = width;
        this.height = height;
        x1 = width/8;
        x2 = (width/8)*7;
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

        stripend = calculation.getRunway().getStripEnd()*scaleFactor;
        blast = calculation.getPlane().getBlastProtect()*scaleFactor;
        obstacleHeight = calculation.getObstacle().getHeight();
        blind = Color.GREY;
        isBlind = false;
        Platform.runLater(() -> {
            initFX(this);
            fillBackground(width, height, blind);
            // use runway.getToda instead of new toda as this was the toda before
            //  int resaLength = (int) ((runway.getDisplacedThreshold()*scaleFactor+x1+dft)-(x1+obstacleWidth));

            drawMapScale((int) runway.getLength());
            if(runway.isTopRunway()){
                drawMeasuringLine(x2-dft, -TODA, height-8*difference, "TODA: " + (int) calculation.getNewTODA()+"m");
                drawMeasuringLine(x2-dft, -TORA, height-7*difference, "TORA: " + (int) calculation.getNewTORA()+"m");
                drawMeasuringLine(x2-dft, -ASDA, height-6*difference, "ASDA: " + (int) calculation.getNewASDA()+"m");
                drawClearway((int) (x2-dft), -1);
                drawStopway((int) (x2-dft), -1);
                //TODO Make width for obstacle correct
                drawObstacle(obstacleWidth, (int)calculation.getObstacle().getHeight());
                drawMeasuringLine(x2-dft-TODA  , -blast, height-9*difference, "Blast allowance: " + (int) calculation.getPlane().getBlastProtect()+"m");
                drawVerticalLine(x2-runway.getDisplacedThreshold()*scaleFactor-dft, 10, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());

            } else{
                drawMeasuringLine(x1+dft, TODA, height-8*difference, "TODA: " + (int) calculation.getNewTODA()+"m");
                drawMeasuringLine(x1+dft, TORA, height-7*difference, "TORA: " + (int) calculation.getNewTORA()+"m");
                drawMeasuringLine(x1+dft, ASDA, height-6*difference, "ASDA: " + (int) calculation.getNewASDA()+"m");
                drawClearway((int) (x1+dft), 1);
                drawStopway((int) (x1+dft), 1);
                drawObstacle(obstacleWidth, (int)calculation.getObstacle().getHeight());
                drawMeasuringLine(x1+dft+TODA  , blast, height-9*difference, "Blast allowance: " + (int) calculation.getPlane().getBlastProtect()+"m");
                drawVerticalLine(runway.getDisplacedThreshold()*scaleFactor+x1+dft, 10, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());

            }
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
        Button btnLandingView = new Button("Landing view");
        Button btnTakeOffView = new Button("Take-off view");
        Button btnColourBlind = new Button("Colour Blind Mode");


        btnLandingView.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        btnLandingView.setTranslateX(canvas.getWidth()-90);
        btnLandingView.setTranslateY(0);

        btnTakeOffView.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        btnTakeOffView.setTranslateX(canvas.getWidth()-90);
        btnTakeOffView.setTranslateY(30);

        this.btnLandingView(btnLandingView);
        this.btnTakeOffView(btnTakeOffView);
        this.btnColourBlind(btnColourBlind);

        root.getChildren().add(btnLandingView);
        root.getChildren().add(btnTakeOffView);
        root.getChildren().add(btnColourBlind);

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

        Scene  scene  =  new  Scene(root, 620, 350);
        return (scene);
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
            } else{
                isBlind = false;
                blind = Color.GREY;
                fillBackground(width, height, blind);
            }
            gc.setFill(Color.rgb(0,0,0));
            gc.setFont(Font.font("Consolas", 20));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("Please select landing or take-off view", width/2, height/2);
        });
    }

    protected void btnLandingView(Button btnLandingView){
        btnLandingView.setOnMouseEntered(e ->{
            btnLandingView.setStyle("-fx-background-color: transparent, -fx-shadow-highlight-color;");
        });
        btnLandingView.setOnMouseExited(e->{
            btnLandingView.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        });
        btnLandingView.setOnMouseClicked(e->{
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            fillBackground(width, height, blind);
            drawMapScale((int) runway.getLength());
            btnLandingView.setStyle("-fx-background-color: transparent, -fx-shadow-highlight-color;");
            if(runway.isTopRunway()){
                resa = (int) ((runway.getDisplacedThreshold()*scaleFactor+x1+obstacleWidth)-(x1+obstacleWidth));
                drawMeasuringLine(x2-runway.getDisplacedThreshold()*scaleFactor-dft, -LDA, height-8*difference, "LDA: " + (int) calculation.getNewLDA()+"m");
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                drawMeasuringLine(x2-dft, -resa, height-7*difference, "RESA: " + (int) calculation.getRunway().getRESA()+"m");
                //TODO Make width for obstacle correct
                drawObstacle(obstacleWidth, (int)calculation.getObstacle().getHeight());
                // diagnal line
                int l = (int) (x2-runway.getDisplacedThreshold()*scaleFactor-dft-LDA);
                gc.setLineWidth(0.5);
                gc.strokeLine(l, y1, x2-dft,y1-obstacleHeight) ;

                gc.setFill(Color.rgb(0,0,0));
                gc.setFont(Font.font("Consolas", 10));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText("1:50", x2-dft-20, y1-obstacleHeight);
                drawVerticalLine(x2-runway.getDisplacedThreshold()*scaleFactor-dft, 10, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());

            } else{
                resa = (int) ((runway.getDisplacedThreshold()*scaleFactor+x1)-(x1));
                drawMeasuringLine(runway.getDisplacedThreshold()*scaleFactor+x1+dft, LDA, height-8*difference, "LDA: " + (int) calculation.getNewLDA()+"m");
                drawMeasuringLine(x1+dft, resa, height-7*difference, "RESA: " + (int) calculation.getRunway().getRESA()+"m");
                drawObstacle(obstacleWidth, (int)calculation.getObstacle().getHeight());
                // diagnal line
                int l = (int) (runway.getDisplacedThreshold()*scaleFactor+x1+dft);
                gc.setLineWidth(0.5);
                gc.strokeLine(l, y1, x1+dft,y1-obstacleHeight) ;

                gc.setFill(Color.rgb(0,0,0));
                gc.setFont(Font.font("Consolas", 10));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText("1:50", x1+dft+20, y1-obstacleHeight);
                drawVerticalLine(runway.getDisplacedThreshold()*scaleFactor+x1+dft, 10, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());


            }
        });
    }
    protected void btnTakeOffView(Button btnTakeOffView){
        btnTakeOffView.setOnMouseEntered(e ->{
            btnTakeOffView.setStyle("-fx-background-color: transparent, -fx-shadow-highlight-color;");
        });
        btnTakeOffView.setOnMouseExited(e->{
            btnTakeOffView.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        });
        btnTakeOffView.setOnMouseClicked(e->{
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            fillBackground(width, height, blind);
            drawMapScale((int) runway.getLength());
            btnTakeOffView.setStyle("-fx-background-color: transparent, -fx-shadow-highlight-color;");
            if(runway.isTopRunway()){
                drawMeasuringLine(x2-dft, -TODA, height-8*difference, "TODA: " + (int) calculation.getNewTODA()+"m");
                drawMeasuringLine(x2-dft, -TORA, height-7*difference, "TORA: " + (int) calculation.getNewTORA()+"m");
                drawMeasuringLine(x2-dft, -ASDA, height-6*difference, "ASDA: " + (int) calculation.getNewASDA()+"m");
                drawClearway((int) (x2-dft), -1);
                drawStopway((int) (x2-dft), -1);
                //TODO Make width for obstacle correct
                drawObstacle(obstacleWidth, (int)calculation.getObstacle().getHeight());
                drawMeasuringLine(x2-dft-TODA  , -blast, height-9*difference, "Blast allowance: " + (int) calculation.getPlane().getBlastProtect()+"m");
                drawVerticalLine(x2-runway.getDisplacedThreshold()*scaleFactor-dft, 10, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());

            } else{
                drawMeasuringLine(x1+dft, TODA, height-8*difference, "TODA: " + (int) calculation.getNewTODA()+"m");
                drawMeasuringLine(x1+dft, TORA, height-7*difference, "TORA: " + (int) calculation.getNewTORA()+"m");
                drawMeasuringLine(x1+dft, ASDA, height-6*difference, "ASDA: " + (int) calculation.getNewASDA()+"m");
                drawClearway((int) (x1+dft), 1);
                drawStopway((int) (x1+dft), 1);
                drawObstacle(obstacleWidth, (int)calculation.getObstacle().getHeight());
                drawMeasuringLine(x1+dft+TODA  , blast, height-9*difference, "Blast allowance: " + (int) calculation.getPlane().getBlastProtect()+"m");
                drawVerticalLine(runway.getDisplacedThreshold()*scaleFactor+x1+dft, 10, y1, "Threshold: "+(int) calculation.getRunway().getDisplacedThreshold());

            }
        });
    }

    protected void fillBackground(int width, int height, Color color){
        gc.setFill(color);
        gc.fillRect(0,0, width, height);
    }
    /////////////////////////////////////////// MEASUREMENTS //////////////////////////////////////////////////

    protected void drawMapScale(int length) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(Font.font("Consolas", 16));
        // we have the +_2 so the vertical line is actually visible and not just a point
        gc.strokeLine(x1, y1, x2, y1); //horizontal line
        gc.strokeLine(x1, y1+2, x1, y1-2);  //vertical line at beginning
        gc.strokeLine(x2, y1+2, x2, y1-2);//vertical line at end
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(String.valueOf(length)+"m", x1, y1+15);
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
    ////////////////////////////////////////////// IMAGES ///////////////////////////////////////////////////////////

    protected void drawObstacle(int width, int height){
        int distanceFromThreshold;
        gc.setFill(Color.RED);
        if (runway.isTopRunway()){
            gc.fillRect(x2-dft-width/2, y1-height, width, height);
            distanceFromThreshold = (int) (x2 - dft -width);
            gc.setFill(Color.RED);
            gc.setFont(Font.font("Consolas", 12));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(calculation.getObstacle().getName() + ": " + calculation.getObstacle().getHeight()+"m",distanceFromThreshold-width/2,y1+25);
        } else{
            distanceFromThreshold = (int) (x1+dft-width);
            gc.fillRect(x1+dft-width+width/2, y1-height, width, height);
            gc.setFill(Color.RED);
            gc.setFont(Font.font("Consolas", 12));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(calculation.getObstacle().getName() + ": " + calculation.getObstacle().getHeight()+"m",distanceFromThreshold-width/2,y1+25);
        }

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


}
