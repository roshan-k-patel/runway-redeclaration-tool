package group5.common;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Airport class associates to all of its constituent runways
 */
public class Airport {	
	private ArrayList<Runway> runways;
	private String name;
	private int id;
	
	public Airport() {
		this.runways = new ArrayList<Runway>();
		this.id = UUID.randomUUID().hashCode(); //UUID example, implementation not final
	}
	
	/**
	 * 
	 * @param name Name of airport
	 */
	public Airport(String name) {
		this();
		this.name = name;
	}
	
	/**
	 * Add a runway to the Airport's ArrayList
	 * @param r Runway to add
	 */
	public void addRunway(Runway r) {
		//TODO create parallel runway (L -> R; bearing2 = 360 - bearing1) and use setParallel() on both
		this.runways.add(r);
	}
	
	/**
	 * 
	 * @return The UID of the airport
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * 
	 * @return The name of the airport (e.g. Heathrow)
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @return Return an arraylist of all of the airport's runways
	 */
	public ArrayList<Runway> getRunways() {
		return this.runways;
	}

	/**
	 * Removes a runway from
	 * @return
	 */
	public boolean removeRunway(Runway r){
		this.runways.remove(r);
		return true;
	}

	@Override
	public String toString(){
		return this.name;
	}
}
