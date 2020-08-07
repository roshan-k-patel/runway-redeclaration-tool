package group5.common;

/**
 * A runway obstacle of a set height
 */
public class Obstacle {
	private String name;
	private double height;
	
	//TODO where are we storing the obstacle's position? should it have a reference to the runway it's on?
	
	/**
	 * 
	 * @param height Height of obstacle
	 * @param name Name of obstacle (identification)
	 */
	public Obstacle(double height, String name) {
		this(height);
		this.name = name;
	}
	
	/**
	 * 
	 * @param height Height of obstacle
	 */
	public Obstacle(double height) {
		this.height = height;
	}
	
	/**
	 * 
	 * @return Name of obstacle (identifier for GUI)
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Give name to rename obstacle to (if user requires)
	 * @param n Name of obstacle
	 */
	public void setName(String n) {
		this.name = n;
	}
	
	/**
	 * 
	 * @return Get height of obstacle
	 */
	public double getHeight() {
		return this.height;
	}
	
	/**
	 * Change height of the obstacle
	 * @param h Height of obstacle (metres)
	 */
	public void setHeight(double h) {
		this.height = h;
	}

	@Override
	public String toString(){
		return this.name;
	}
}
