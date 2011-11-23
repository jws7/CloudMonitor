package Experiments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import DataTypes.EnergyMonitorData;
import DataTypes.MachineUtilisationData;

/**
 * Class to contain all info needed to run a single experiment
 * @author jws7
 *
 */
public class Experiment {

	public Experiment(String string) {
		this.command = string;
	}

	// Command to run
	public String command;
	
	// CPU Guv
	public String cpu_guv;
	
	//Only populted when display is needed then discarded (save memory)
	public ArrayList<MachineUtilisationData> resource_data;
	public ArrayList<EnergyMonitorData> energy_data;
	
	// Start & Finish times
	public Date start;
	public Date finish;
	
	public String benchmarkURL;
	
	// Output
	public String output;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss"); // 2010-08-04 13:51:14.539 //AKA SQL Format

	public String startTime() {
		// Return formated date
        return sdf.format(start);
	}

	public String finishTime() {
		// Return formated date
        return sdf.format(finish);
	}
}
