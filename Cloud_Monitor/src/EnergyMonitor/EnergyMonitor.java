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

package EnergyMonitor;

import java.io.File;
import java.net.InetAddress;
import java.util.Scanner;

import Utilities.DatabaseConnection;

import uk.ac.standrews.cs.nds.util.SSH2ConnectionWrapper;

public class EnergyMonitor extends Thread {

	// Fields
	private String PDU_IPAddress = "138.251.198.6"; // IP address of PDU
	private int socket; // Socket number to query
	private DatabaseConnection db;	
	private boolean debug = false;

	/**
	 * Constructor : IP of the PDU & Socket to query
	 * 
	 * @param ip 
	 * @param i = PDU Outlet to query (NODE20 = 5, NODE23 = 4) 
	 */
	public EnergyMonitor(String ip, int i, boolean d) {
		this(i, d);
		this.PDU_IPAddress = ip;	
	}

	/**
	 * Second constructor, sets socket to use
	 * 
	 * @param i
	 */
	public EnergyMonitor(int i, boolean d) {
		this.socket = i;
		this.debug = d;
		
		try {		
			//Set up DB
			this.db = new DatabaseConnection(d);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Threaded method to run on loop - will get all PDU data for this socket
	 * every 3 secs
	 */
	public void run() {
		while (true) {
			long start = System.currentTimeMillis();
			
			
			EnergyMonitorPollerSSH emh = new EnergyMonitorPollerSSH(this.socket, this.debug, this.db);
			emh.start();

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
	
	public String getPDU_IPAddress() {
		return PDU_IPAddress;
	}

	public void setPDU_IPAddress(String pDU_IPAddress) {
		PDU_IPAddress = pDU_IPAddress;
	}

	// ###########################################################
	/**
	 * TEST METHOD
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int socket = Integer.parseInt(args[0]);
		
		// Create new monitor for socket and get Active Power
		EnergyMonitor energy = new EnergyMonitor(socket, true);
		energy.start();
	}
}