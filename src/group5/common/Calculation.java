package group5.common;

import java.util.UUID;

/**
 * Places all data given for calculation in this class during its construction, then prompt calc'd results
 * There are two types of calculations, either taking off towards and landing towards, or taking off away 
 * and landing away.
 */
public class Calculation {
	/**
	 * if true, landing towards, if false, away
	 */
	private boolean landingTowards;

	private Runway runway;
	private Plane plane;
	private Obstacle obstacle;
	
	/**
	 * ID required for calculations to be referenced in a database (user can look up)
	 */
	private int id;
	private CalcLog log;
	private String[] calclogString;
	
	private double distanceFromThreshold;
	private double distanceFromCentreLine;

	public double getOldTORA() {
		return oldTORA;
	}

	public void setOldTORA(double oldTORA) {
		this.oldTORA = oldTORA;
	}

	public double getOldTODA() {
		return oldTODA;
	}

	public void setOldTODA(double oldTODA) {
		this.oldTODA = oldTODA;
	}

	public double getOldASDA() {
		return oldASDA;
	}

	public void setOldASDA(double oldASDA) {
		this.oldASDA = oldASDA;
	}

	public double getOldLDA() {
		return oldLDA;
	}

	public void setOldLDA(double oldLDA) {
		this.oldLDA = oldLDA;
	}

	double[] newVals;
	double newTORA, newTODA, newASDA, newLDA;
	double oldTORA, oldTODA, oldASDA, oldLDA;

	/**
	 *
	 * @param runway Takes the runway that the calculation is performed on.
	 * @param plane Takes the plane that the
	 * @param obstacle Takes the obstacles that is on the runway
	 * @param distanceFromThreshold The distance the obstacle is from the threshold
	 * @param distanceFromCentreLine The distance the obstacle is from the centre line
	 */
	public Calculation(Runway runway, Plane plane, Obstacle obstacle, 
			double distanceFromThreshold, double distanceFromCentreLine) {
		this.runway = runway;
		this.plane = plane;
		this.obstacle = obstacle;

		this.oldASDA = runway.getASDA();
		this.oldLDA = runway.getLDA();
		this.oldTODA = runway.getTODA();
		this.oldTORA = runway.getTORA();
		
		this.distanceFromThreshold = distanceFromThreshold;
		this.distanceFromCentreLine = distanceFromCentreLine;
		
		this.newVals = new double[4];
		this.calclogString = new String[4];

		this.id = 0;

		this.calculate(); // Runs this immediately when instantiated
	}


	/**
     * Runway /= Runway Strip. The whole strip is the the whole concrete/tarmac/whatever section designated for use. The
	 * runway is the bit the planes should be on, and shouldn't be elsewhere except for an emergency.
	 *
	 *
	 *																						        <--Clearway-->
	 *------------------------------------------------------------------------------------------------------------
	 *
	 *
	 * 		<----------------------------------- TORA -------------------------------------------->
	 * 		---------------------------------------------------------------------------------------   Strip End
	 * Strip|   Displacement |					Distance from threshold							   |<------------>
	 *<---->|<-------------->|<-------------------------------------------------------->           |
     * End  |   Threshold    |                      		         		  slopeCalc		       |
	 * 		|   PLANE->      |<---------------------------------------------><---------> X         |
	 * 		|                |               New TORA                                    ^         |
	 * 		|                |                                                       OBSTACLE      |
	 * 		---------------------------------------------------------------------------------------
	 * 						 ^
	 * 					This line is the threshold
     *
	 * ------------------------------------------------------------------------------------------------------------
	 *
	 */
	
	/**
	 * @return Array of newly calculated TORA, TODA, ASDA, LDA for taking off towards the obstacle
	 */
	private double[] takeOffTowards() {
		double newTORA = distanceFromThreshold + runway.getDisplacedThreshold() - 
				slopeCalculation(plane, obstacle.getHeight()) - runway.getStripEnd();
		double newTODA = newTORA;
		double newASDA = newTORA;
		double newLDA = distanceFromThreshold - runway.getRESA() - runway.getStripEnd();

		return new double[] {newTORA, newTODA, newASDA, newLDA};
	}
	
	/**
	 * @return Array of newly calculated TORA, TODA, ASDA, LDA for landing towards the obstacle
	 */
	private double[] landingTowards() {
		this.landingTowards = true;
		return takeOffTowards(); //valid as long as landingtowards = takeofftowards
	}
	
	/**
	 * @return Array of newly calculated TORA, TODA, ASDA, LDA for taking off away from the obstacle
	 */
	private double[] takeOffAway() {
        double newTORA = runway.getTORA() - distanceFromThreshold - 
        		plane.getBlastProtect() - runway.getDisplacedThreshold(); // When taking off
		double newTODA = newTORA + runway.getClearway();
		double newASDA = newTORA + runway.getStopway();
		double newLDA = runway.getLDA() - distanceFromThreshold - 
				slopeCalculation(plane, obstacle.getHeight()) - runway.getStripEnd();

		return new double[] {newTORA, newTODA, newASDA, newLDA};
	}

	/**
	 * @return Array of newly calculated TORA, TODA, ASDA, LDA for landing away from the obstacle
	 */
	private double[] landingAway() {
		double newTORA = runway.getTORA() - distanceFromThreshold - runway.getDisplacedThreshold() 
				- runway.getRESA() - runway.getStripEnd(); // When landing
		double newTODA = newTORA + runway.getClearway();
		double newASDA = newTORA + runway.getStopway();
		double newLDA = runway.getLDA() - distanceFromThreshold - 
				slopeCalculation(plane, obstacle.getHeight()) - runway.getStripEnd();
		
		this.landingTowards = false;
		return new double[] {newTORA, newTODA, newASDA, newLDA};
	}

	/**
	 * Calculate the slope (default ratio 1:50).
	 * @param plane Takes the plane being calculated.
	 * @param height Takes the height of the obstacle.
	 * @return Returns the slopeCalculation value.
	 */
	private double slopeCalculation(Plane plane, double height) {
		return plane.getRatio() * height;
	}

	/**
	 * Performs the calculations.
	 */
	public void calculate() {
		//if object 75m or greater dist. from centreline no redeclaration necessary
		if (distanceFromCentreLine > 75) {
			newVals = new double[] {runway.getTORA(), runway.getTODA(), runway.getASDA(), runway.getLDA()};
			
			//calclog entries
			this.calclogString[0] = "New TORA = Previous TORA (" + newVals[0] + ")";
			this.calclogString[1] = "New TODA = Previous TODA (" + newVals[1] + ")";
			this.calclogString[2] = "New ASDA = Previous ASDA (" + newVals[2] + ")";
			this.calclogString[3] = "New LDA = Previous LDA (" + newVals[3] + ")";
		} else {
			if (takeOffAway()[3] > takeOffTowards()[3]) {
				newVals[3] = takeOffAway()[3]; //LDA
				
				//calculation log entry
				this.calclogString[3] = "New LDA (" + newVals[3] + ") = " +
				"Previous LDA (" + runway.getLDA() + ") - Distance from Threshold (" + distanceFromThreshold + 
				") - Slope Ratio * Obstacle Height (" + plane.getRatio() + " * " + obstacle.getHeight() + 
				") - Strip End (" + runway.getStripEnd() + ")";
			} else {
				newVals[3] = takeOffTowards()[3]; //LDA
				
				//calculation log entry
				this.calclogString[3] = "New LDA (" + newVals[3] + ") = " +
				"Distance from Threshold (" + distanceFromThreshold + ") - RESA (" + runway.getRESA() + 
				") - Strip End (" + runway.getStripEnd() + ")";
			}
			
			double[] tempVals;
			if (landingTowards()[0] > landingAway()[0]) {
				tempVals = landingTowards();
				newVals[0] = tempVals[0]; //TORA
				newVals[1] = tempVals[1]; //TODA
				newVals[2] = tempVals[2]; //ASDA
				
				//calculation log entries
				this.calclogString[0] = "New TORA (" + newVals[0] + ") = Previous TORA (" + runway.getTORA() + 
						") - Distance from Threshold (" + distanceFromThreshold + ") - Blast Protection (" + 
						plane.getBlastProtect() + ") - Displaced Threshold (" + runway.getDisplacedThreshold() + ")";
				this.calclogString[1] = "New TODA (" + newVals[1] + ") = New TORA (" + newVals[0] + ") + Clearway (" +
						runway.getClearway() + ")";
				this.calclogString[2] = "New ASDA (" + newVals[2] + ") = New TORA (" + newVals[0] + ") + Stopway (" +
						runway.getClearway() + ")";
				
			} else {
				tempVals = landingAway();
				newVals[0] = tempVals[0]; //TORA
				newVals[1] = tempVals[1]; //TODA
				newVals[2] = tempVals[2]; //ASDA
				
				//calculation log entries
				this.calclogString[0] = "New TORA (" + newVals[0] + ") = Previous TORA (" + runway.getTORA() + 
						") - Distance from Threshold (" + distanceFromThreshold + ") - Displaced Threshold (" +
						runway.getDisplacedThreshold() + ") - RESA (" + runway.getRESA() + ") - Strip End (" +
						runway.getStripEnd() + ")";
				this.calclogString[1] = "New TODA (" + newVals[1] + ") = New TORA (" + newVals[0] + ") + Clearway (" +
						runway.getClearway() + ")";
				this.calclogString[2] = "New ASDA (" + newVals[2] + ") = New TORA (" + newVals[0] + ") + Stopway (" +
						runway.getClearway() + ")";
			}
		}
		
		//create log
		this.generateCalcLog();
	}
	
	private void generateCalcLog() {
		//names or actual objects?
		this.log = new CalcLog(this.runway.getName(), this.runway.getAirport().getName(), this.calclogString);
	}

	/**
	 * @return redeclared TORA
	 */
	public double getNewTORA() {
		return newVals[0];
	}

	/**
	 * @return redeclared TODA
	 */
	public double getNewTODA() {
		return newVals[1];
	}

	/**
	 * @return redeclared ASDA
	 */
	public double getNewASDA() {
		return newVals[2];
	}

	/**
	 * @return redeclared LDA
	 */
	public double getNewLDA() {
		return newVals[3];
	}

	public Runway getRunway() {
		return runway;
	}

	public void setRunway(Runway runway) {
		this.runway = runway;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public Obstacle getObstacle() {
		return obstacle;
	}

	public void setObstacle(Obstacle obstacle) {
		this.obstacle = obstacle;
	}

	public double getDistanceFromThreshold() {
		return distanceFromThreshold;
	}

	public void setDistanceFromThreshold(double distanceFromThreshold) {
		this.distanceFromThreshold = distanceFromThreshold;
	}

	public double getDistanceFromCentreLine() {
		return distanceFromCentreLine;
	}

	public void setDistanceFromCentreLine(double distanceFromCentreLine) {
		this.distanceFromCentreLine = distanceFromCentreLine;
	}
	
	public CalcLog getCalcLog() {
		return this.log;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public boolean getLandingTowardStatus() {
		return this.landingTowards;
	}
	
	@Override
	public String toString() {
		return Integer.toString(id);
	}
}
