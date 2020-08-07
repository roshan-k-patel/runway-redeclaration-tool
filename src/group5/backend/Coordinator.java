package group5.backend;

import group5.common.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Coordinator implements Calculator
{

	private ArrayList<Airport> airports;
	private ArrayList<Calculation> calculations;
	private ArrayList<Plane> planes;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<CalcLog> logs;
	private ArrayList<Runway> runways;
	private Updated updated;

	/**
	 * Basic constructor which creates group5.backend.Coordinator with empty lists.
	 */
	public Coordinator()
	{
		this.airports = new ArrayList<Airport>(){
			@Override
			public boolean add(Airport a){
				boolean value = super.add(a);
				updated.addAdded(a);
				return value;
			}
		};

		this.calculations = new ArrayList<Calculation>(){
			@Override
			public boolean add(Calculation c){
				boolean value = super.add(c);
				updated.addAdded(c);
				return value;
			}
		};

		this.planes = new ArrayList<Plane>(){
			@Override
			public boolean add(Plane p){
				boolean value = super.add(p);
				updated.addAdded(p);
				return value;
			}
		};

		this.obstacles = new ArrayList<Obstacle>(){
			@Override
			public boolean add(Obstacle o){
				boolean value = super.add(o);
				updated.addAdded(o);
				return value;
			}
		};
		
		this.runways = new ArrayList<Runway>(){
			@Override
			public boolean add(Runway r){
				boolean value = super.add(r);
				updated.addAdded(r);
				return value;
			}
		};
		
		this.logs = new ArrayList<CalcLog>();

		this.updated = new Updated();
	}

	@Override
	public boolean addAirport(Airport a)
	{
	    synchronized (this) {
			this.airports.add(a);

		}
		return true;
	}

	@Override
	public int addRunway(Runway runway) {
		synchronized (this) {
			runway.getAirport().addRunway(runway);
			this.runways.add(runway);
			updated.addAdded(runway);
			return 0;
		}
	}

	@Override
	public boolean load(String fileName) {
		synchronized (this) {
			try {
				FileHandler fileHandler = new FileHandler(fileName, this);
				fileHandler.load();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

    @Override
    public boolean addCalculation(Calculation calculation) {
		calculation.setId(nextID());
		synchronized (this) {
			this.calculations.add(calculation);
			this.updated.addAdded(calculation);
			this.logs.add(calculation.getCalcLog());
		}
		return false;
	}

	@Override
	public boolean removePlane(Plane p) {
	    synchronized (this) {
	    	if (planes.contains(p)){
	    		planes.remove(p);
	    		updated.addRemoved(p);
	    		return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeObstacle(Obstacle o) {
		synchronized (this) {
			if (obstacles.contains(o)){
				obstacles.remove(o);
				updated.addRemoved(o);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAirport(Airport a) {
		synchronized (this) {
			if (airports.contains(a)){
				airports.remove(a);
				updated.addRemoved(a);
				return true;
			}
		}	return false;
	}

	@Override
	public boolean removeCalculation(Calculation c) {
		synchronized (this) {
			if (calculations.contains(c)){
				calculations.remove(c);
				logs.remove(c.getCalcLog());
				updated.addRemoved(c);
				return true;
			}
		}return false;
	}
	
	@Override
	public boolean removeRunway(int id, Runway r) {
		synchronized (this){
			this.runways.remove(r);
			for (Airport a : airports) {
				if (a.getRunways().contains(r)){
					a.removeRunway(r);
					updated.addRemoved(r);
				}
			}
		}
		return false;
	}

	@Override
	public Updated updated() {
	    Updated returnUpdated = null;
		synchronized (this) {
			returnUpdated = this.updated;
			this.updated = new Updated();

		}
		return returnUpdated;
	}

	@Override
	public boolean addPlane(Plane p)
	{
		synchronized (this) {
			this.planes.add(p);
		}
		return true;
	}

	@Override
	public boolean addObstacle(Obstacle o)
	{
		synchronized (this) {
			this.obstacles.add(o);
		}
		return true;
	}
	
	@Override
	public boolean addLog(CalcLog l)
	{
		synchronized (this) {
			this.logs.add(l);
		}
		return true;
	}

	@Override
	public ArrayList<Airport> getAirports()
	{
		synchronized (this) {
			return this.airports;
		}
	}

	@Override
	public ArrayList<Obstacle> getObstacles() {
		synchronized (this) {
			return this.obstacles;
		}
	}

	@Override
	public ArrayList<Plane> getPlanes()
	{
		synchronized (this) {
			return this.planes;
		}
	}
	
	@Override
	public ArrayList<CalcLog> getLogs()
	{
		synchronized (this) {
			return this.logs;
		}
	}

	@Override
	public boolean save(String filename) {
		FileHandler handler = new FileHandler(filename,this);
		try {
			handler.save();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean save() {
		return false;
	}

	@Override
	public ArrayList<Calculation> getCalculations()
	{
		synchronized (this) {
			return this.calculations;
		}
	}

	public Airport getAirport(String name) {
		synchronized (this) {
			return getItem(name, airports);
		}
	}

	public Calculation getCalculation(String name) {
		synchronized (this) {
			return getItem(name, calculations);
		}
	}
	
	/**
	 * 
	 * @param name of CALCULATION that log is attached to
	 * @return Log of certain calculation
	 */
	public CalcLog getLogThroughCalc(String name) {
		synchronized (this) {
			return getItem(name, calculations).getCalcLog();
		}
	}
	
	public CalcLog getLog(String name) {
		synchronized (this) {
			return getItem(name, logs);
		}
	}

	public Obstacle getObstacle(String name) {
		synchronized (this) {
			return getItem(name, obstacles);
		}
	}

	public Plane getPlane(String name) {
		synchronized (this) {
			return getItem(name, planes);
		}
	}

	@Override
	public ArrayList<Runway> getRunways() {
		synchronized (this) {
			return this.runways;
		}
	}


	public <E> E getItem(String name, ArrayList<E> list){
		synchronized (this) {
			for (E listItem : list) {
				if (listItem.toString().equals(name)) {
					return listItem;
				}
			}
			return null;
		}
	}

	public void addAirports(List<Airport> list) {
		synchronized (this) {
			for (Airport a : list) {
				addAirport(a);
			}
		}
	}

	public void addPlanes(List<Plane> list) {
		synchronized (this) {
			for (Plane p : list) {
				addPlane(p);
			}
		}
	}

	public void addObstacle(List<Obstacle> list){
		synchronized (this) {
			for (Obstacle o : list) {
				addObstacle(o);
			}
		}
	}

	public void addCalculations(List<Calculation> list){
		synchronized (this) {
			for (Calculation c : list) {
				addCalculation(c);
				addLog(c.getCalcLog());
			}
		}
	}

	public int nextID(){
		synchronized (this) {
			int highest = 0;
			for (Calculation c : calculations) {
				if (Integer.parseInt(c.toString()) > highest) {
					highest = Integer.parseInt(c.toString());
				}
			}

			return highest + 1;
		}
	}


}
