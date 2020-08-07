package group5.common;

/**
 * Runway class stores its all its relevant parameters as class variables
 */
public class Runway {	
	/**
	 * Stores position as 'L', 'R', or 'C'
	 */
	private char position;
	private double width, length, stopway, clearway, tora, lda, stripEnd, displacedThreshold, resa;
	private int bearing;
	private String name;
	private Airport airport;
	
	private static final double STRIP_END_DEFAULT = 60;
	private static final double RESA_DEFAULT = 240;
	
	/**
	 * Reference to parallel runway (e.g. 09L to 27R)
	 */
	private Runway parallel;

	/**
	 * Basic constructor
	 * @param position Char of the direction of the runway ('L', 'R', 'C')
	 * @param bearing Bearing (angle to north) of runway in degrees
	 */
	public Runway(char position, int bearing, String name) {
		this.position = position;
		this.bearing = bearing;
		this.name = name;
		
		//ensure doesn't overwrite actual parallel
		this.parallel = null;
	}

	public Runway(int bearing, String name) {
		this.bearing = bearing;
		this.name = name;

		//ensure doesn't overwrite actual parallel
		this.parallel = null;
	}
	
	/**
	 * In which the strip end and displaced threshold are known
	 * @param position Char of the direction of the runway ('L', 'R', 'C')
	 * @param bearing Bearing (angle to north) of runway in degrees
	 * @param tora Take-Off Run Available (metres)
	 * @param toda Take-Off Distance Available: TORA + clearway (metres)
	 * @param asda Accelerate-Stop Distance Available: TORA + stopway (metres)
	 * @param lda Landing Distance Available: TORA - displaced threshold (metres)
	 * @param displacedThreshold Take-off at point other than physical beginning/end of runway
	 * @param stripEnd Area between end of runway and end of runway strip
	 * @param resa
	 * @param airport Reference to the airport that the runway is on.
	 */
	public Runway (char position, int bearing, double tora, double toda, double asda,
			double lda, Double displacedThreshold, Double stripEnd, Double resa, String name,Airport airport, double length) {
		this(position, bearing, name);
		
		this.tora = tora;
		this.clearway = toda - tora;
		this.stopway = asda - tora;
		this.lda = lda;
		this.airport = airport;
		this.length = length;

		if (displacedThreshold == null) {
			this.displacedThreshold = tora - lda;
		} else {
			this.displacedThreshold = displacedThreshold;
		}
		if (stripEnd == null) {
			this.stripEnd = STRIP_END_DEFAULT;
		} else {
			this.stripEnd = stripEnd;
		}
		if (resa == null) {
			this.resa = RESA_DEFAULT;
		} else {
			this.resa = resa;
		}
	}

	public Runway (int bearing, double tora, double toda, double asda,
				   double lda, Double displacedThreshold, Double stripEnd, Double resa, String name,Airport airport, double length) {
		this(bearing, name);

		this.tora = tora;
		this.clearway = toda - tora;
		this.stopway = asda - tora;
		this.lda = lda;
		this.airport = airport;
		this.length = length;

		if (displacedThreshold == null) {
			this.displacedThreshold = tora - lda;
		} else {
			this.displacedThreshold = displacedThreshold;
		}
		if (stripEnd == null) {
			this.stripEnd = STRIP_END_DEFAULT;
		} else {
			this.stripEnd = stripEnd;
		}
		if (resa == null) {
			this.resa = RESA_DEFAULT;
		} else {
			this.resa = resa;
		}
	}


	public Airport getAirport(){
		return this.airport;
	}

	/**
	 * 
	 * @return Take Off Run Available: length of runway available for takeoff; total dist. from point aircraft can
	 * take off to point where cannot support weight (aircraft should have left runway by this point
	 */
	public double getTORA() {
		return this.tora;
	}

	/**
	 * @return Area beyond TORA (accounted for in TODA); may be used during aircraft initial climb to specified height
	 */
	public double getClearway() {
		return this.clearway;
	}
	
	/**
	 * 
	 * @return Area beyond TORA (accounted for in ASDA) which may be used in aborted take-off for safe stopping
	 */
	public double getStopway() {
		return this.stopway;
	}
	
	/**
	 * 
	 * @return TODA (take off distance available): TORA length plus any clearway (area beyond runway considered free 
	 * from obstructions); total dist. usable for safe take-off and initial ascent to specified height
	 */
	public double getTODA() {
		return this.tora + this.clearway;
	}
	
	/**
	 * 
	 * @return Accelerate-Stop Distance Available (metres): TORA plus any stopway (area not in TORA, but can be 
	 * used to safely stop aircraft in emergency); total dist. for aborted take-off
	 */
	public double getASDA() {
		return this.tora + this.stopway;
	}
	
	/**
	 * 
	 * @return Landing Distance Available (metres): length of runway available for landing; start at threshold 
	 * (typically same as TORA start, but may differ due to displacement from obstacles or operations)
	 */
	public double getLDA() {
		return this.lda;
	}
	
	/**
	 * 
	 * @return Length of runway (metres)
	 */
	public double getLength() {
		return this.length;
	}
	
	/**
	 * 
	 * @return Width of runway (metres)
	 */
	public double getWidth() {
		return this.width;
	}
	
	/**
	 * 
	 * @return Parallel runway (e.g. 09L to 27R)
	 */
	public Runway getParallel() {
		return this.parallel;
	}
	
	/**
	 * 
	 * @return Bearing (angle to north) of runway in degrees
	 */
	public int getBearing() {
		return this.bearing;
	}
	
	/**
	 * 
	 * @return Located at point other than physical beginning/end of runway; can be used for takeoff but not landing
	 */
	public double getDisplacedThreshold() {
		return this.displacedThreshold;
	}
	
	/**
	 * 
	 * @return Runway End Safety Area: area symmetrical about extended runway centreline and adjacent to end of strip;
	 * reducing risk of damage to aircraft undershooting/overrunning runway
	 */
	public double getRESA() {
		return this.resa;
	}
	
	/**
	 * 
	 * @return Area between end of runway and end of runway strip
	 */
	public double getStripEnd() {
		return this.stripEnd;
	}
	
	/**
	 * Set new TORA
	 * @param t Length in metres
	 */
	public void setTORA(double t) {
		this.tora = t;
	}
	
	//TODO set TODA, ASDA, LDA?
	
	/**
	 * Set the runway parallel to this runway
	 * @param p Parallel runway
	 */
	public void setParallel(Runway p) {
		this.parallel = p;
	}
	
	/**
	 * 
	 * @return Runway name in correct format (e.g "09L")
	 */
	public String getName() {
		if (this.bearing < 10) {
			return "0" + (this.bearing) + String.valueOf(this.position).toUpperCase();
		} else {
			return Integer.toString(this.bearing) + String.valueOf(this.position).toUpperCase();
		}
	}

	public Boolean isTopRunway(){
			if(bearing > 18){
				return true;
			} else{
				return false;
			}
	}

	public char getPosition(){
		return position;
	}

	@Override
	public String toString(){
		return this.getName();
	}
}
