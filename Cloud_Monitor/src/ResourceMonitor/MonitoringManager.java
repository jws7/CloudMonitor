package ResourceMonitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import DataTypes.SysInfoData;
import ResourceMonitor.SysInfo;
import Utilities.DatabaseConnection;

public class MonitoringManager{
	
	// Fields
	private DatabaseConnection db;				 // Database Connection
	private SysInfo sysInfo;  	 // System Info (only needed once).

	
	/**
	 * Main method to begin.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args){
		
		// Re-direct StdErr to a file for analysis.
		try {
			System.setErr(new PrintStream(new FileOutputStream("errorlog.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Create a monitoring class
		new MonitoringManager();
	}
	
	
	/**
	 * Constructor
	 */
	public MonitoringManager(){
		
		System.out.println("Creating a MonitoringManager");
		this.sysInfo = new SysInfo();
		
		//Set up DB -- TODO
		this.db = new DatabaseConnection();
		
		// Get static system information.
		SysInfoData sysInfoResults = sysInfo.stats();
		db.insertSystemInfo(sysInfoResults);  /// will input sysinfo into DB
	

		// Extract machine ID
		String machineID = sysInfoResults.machine_id;
		System.out.println("MachineID: " + machineID);
		System.out.println(sysInfoResults);
		
		
		MachineUtilisationMonitor mu = new MachineUtilisationMonitor(machineID);
		new Thread(mu).start();

		//EnergyMonitor energy = new EnergyMonitor(socket, false);
		//energy.start();
	}
}
