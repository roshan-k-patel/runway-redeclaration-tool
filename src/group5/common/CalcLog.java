package group5.common;
import java.util.Date;

/**
 * Stores a log of the calculations resulting in redeclared values, along with metadata (time of creation, names of the
 * runway/plane, and a UID)
 */
public class CalcLog {
	//need an ordered list of these CalcLogs somewhere
	private int calcID;
	private Date time;
	private String runwayName, planeName;
	//TORA TODA ASDA LDA
	private float[] previousVals; 
	private float[] newVals;
	
	/**
	 * Textual component showing breakdown of calculation
	 */
	private String[] logs;
	
	public CalcLog(String runwayName, String planeName, String[] logs) {
		this.time = new Date();
		//calc id
		this.runwayName = runwayName;
		this.planeName = planeName;
		this.logs = logs;
	}
	
	/**
	 * @return Array of all strings of logs for calculation
	 */
	public String[] getLogs() {
		return this.logs;
	}
	
	/**
	 * @return Concatenated string of calculation logs
	 */
	public String getLogsString() {
		String output = "";
		for (String s : this.logs) {
			output += s + "\n";
		}
		
		return output.substring(0, output.length() - 1);
	}
	
	/**
	 * @return Format of all data of calculation log object to string array
	 */
	public String[] printAll() {
		String timeString = this.time.getDate() + "\\" + this.time.getMonth() + "\\" + this.time.getYear() + ": " +
				this.time.getSeconds() + ":" + this.time.getMinutes() + ":" + this.time.getHours();
		
		return new String[] {timeString, "Plane: " + this.planeName, "Runway: " + this.runwayName, 
				this.logs[0], this.logs[1], this.logs[2], this.logs[3],};
	}
	
	/**
	 * @return Format of all data of calculation log object to a single string
	 */
	public String printAllString() {
		String output = "";
		for (String s : this.printAll()) {
			output += s + "\n";
		}
		
		return output.substring(0, output.length() - 1);
	}
	
	/**
	 * @return previous TORA, TODA, ASDA, LDA
	 */
	public float[] getPrevVals() {
		return this.previousVals;
	}
	
	/**
	 * @return new TORA, TODA, ASDA, LDA
	 */
	public float[] getNewVals() {
		return this.newVals;
	}
}
