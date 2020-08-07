package group5.common;

/**
 * A plane is a vehicle which needs to safely take off/land on runways
 */
public class Plane {
	private double blastProtection, ratio;
	private String name;
	private static final double DEFAULT_BLAST_PROTECTION = 300;
	private static final double DEFAULT_RATIO = 50;

	/**
	 * Constructor for a detailed plane description.
	 * @param name Name of the plane
	 * @param blastProtection Blast protection (metres)
	 * @param ratio Ratio of slope descent (1:X)
	 */
	public Plane(String name, Double blastProtection, Double ratio) {
		this.name = name;
		if (blastProtection == null) {
			this.blastProtection = DEFAULT_BLAST_PROTECTION;
		} else {
			this.blastProtection = blastProtection;
		}
		if (ratio == null) {
			this.ratio = DEFAULT_RATIO;
		} else {
			this.ratio = ratio;
		}
	}

	/**
	 * Basic constructor if length, width or blast protection is not known
	 * @param name Name of the plane
	 */
	public Plane(String name) {
		this(name, null, null);
	}
	
	/**
	 * 
	 * @return The blast protection of the plane (metres)
	 */
	public double getBlastProtect() {
		return this.blastProtection;
	}
	
	/**
	 * 
	 * @return The name/callsign of the plane (for identification)
	 */
	public String getName() {
		return this.name;
	}
	
	public double getRatio() {
		return this.ratio;
	}

	@Override
	public String toString(){
		return this.name;
	}
}
