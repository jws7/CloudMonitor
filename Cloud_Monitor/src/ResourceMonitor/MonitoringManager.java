/*
 * Copyright (C) 2009-2011 School of Computer Science, University of St Andrews. All rights reserved.
 * Project Homepage: http://jws7.net/cloudmonitor
 *
 * CloudMonitor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CloudMonitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with CloudMonitor.  If not, see <http://www.gnu.org/licenses/>.
 */
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
