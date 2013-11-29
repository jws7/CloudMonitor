/*
 * Copyright (C) 2009-2012 School of Computer Science, University of St Andrews. All rights reserved.
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

package EnergyMonitor;

import DataTypes.EnergyMonitorData;
import Utilities.DatabaseConnection;


public class NewEnergyMonitor extends Thread {

	private DeviceMonitor monitor;
	private DatabaseConnection db;
	
	public NewEnergyMonitor(){
		
		// Can extract these details from a config file
		this.monitor = new RairitainPDUMonitor("138.251.198.2", 2);
		this.db = new DatabaseConnection();
	}
	
	/**
	 * Threaded method to run on loop - will get all data for this montior
	 * every 3 secs
	 */
	public void run() {
		while (true) {
			long start = System.currentTimeMillis();
			
			// Retrieve data
			EnergyMonitorData data = this.monitor.getAllData();
			// add to DB
			this.db.insertEnergyInfo(data);
			
			// Calculate sleep
			long finish = System.currentTimeMillis();
			long diff = finish - start;
			long sleep_t = 3000 - diff; // new record every 3 secs
			
			// Now sleep for 3 seconds
			try {
				Thread.sleep(sleep_t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ###########################################################
	/**
	 * TEST METHOD
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Create new monitor for socket and get Active Power
		EnergyMonitor energy = new EnergyMonitor(1, false);
		energy.start();
	}
}