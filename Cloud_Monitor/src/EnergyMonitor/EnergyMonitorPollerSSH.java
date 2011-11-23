package EnergyMonitor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import DataTypes.Data;
import DataTypes.EnergyMonitorData;
import Utilities.DatabaseConnection;

import uk.ac.standrews.cs.nds.util.*;

public class EnergyMonitorPollerSSH extends Thread {

	// Fields
	private String PDU_IPAddress = "10.1.0.2"; // IP address of PDU
	private int socket; // Socket number to query
	private SSH2ConnectionWrapper ssh; // SSH connection to controller -- hack
	private EnergyMonitorData data;
	private boolean debug = false;
	private DatabaseConnection db;				 // Database Connection


	/**
	 * Second constructor, sets socket to use
	 * 
	 * @param i
	 */
	public EnergyMonitorPollerSSH(int i, boolean f, SSH2ConnectionWrapper s,  DatabaseConnection d) {
		this.socket = i;
		this.ssh = s;
		this.db = d;
		this.debug = f;
		
		if (debug) {
			System.out.println("Debug on");
			System.out
					.println("Version with time between SNMP Call & Setting up ByteInput");
		}

		// Create data storage object
		this.data = new EnergyMonitorData();
	}

	/**
	 * Threaded method to run on loop - will get all PDU data for this socket
	 * every 3 secs
	 */
	public void run() {

			EnergyMonitorData e_data = getAllData();
			System.out.println(e_data);
	}

	/**
	 * Main method for collecting data
	 * 
	 * -- Calls each data method inturn
	 * 
	 * @return
	 */
	public EnergyMonitorData getAllData() {
		
		if(this.db == null){
			System.err.println("Database is null");
			System.exit(-1);
		}

		try {
			// Comment out this line to do update only
			this.data = new EnergyMonitorData();
			
			this.data.socket = this.socket;

			// Populate data
			int power = getActivePower();
			if (power > 1)
				this.data.activePower = power;

			int voltage = getVoltage();
			if (voltage > 1)
				this.data.voltage = voltage;

			float current = getCurrent();
			if (current > 0.1f)
				this.data.current = current;

			int wattHours = getWattHours();
			if (wattHours > 1)
				this.data.wattHours = wattHours;

			// attempt to insert into DB
			this.db.insertEnergyInfo(data);

			// And return to caller
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int executeCommand(String cmd) {

		// print("ExecuteCommand method invoked");
		try {

			// Print command to run
			if (debug)
				print("[DEBUG] \"" + cmd + "\"");

			// Create output stream to use
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// Execute command on remote host
			long start = System.currentTimeMillis();
			Process runProcess = Processes.runProcess(cmd, this.ssh, out,
					System.err);
			if (debug) {
				print("[DEBUG] Process running. Time: " + start);
			}

			// Wait for subprocess to finish... then we can read in the buffered
			// stream
			runProcess.waitFor();

			// Convert output stream to an input stream we can read (via
			// ByteArray)
			InputStream in = new ByteArrayInputStream(out.toByteArray());

			// Deal with result
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = null;

			while ((line = reader.readLine()) != null) {
				
				// Skip if empty string
				if (line.equals("")) continue;

				if (debug) {
					System.out.println("Reading line...");
					System.out.println(line);
				}

				// Now get number (last string on a space split line).
				line = line.trim();
				String[] columns = line.split(" ");
				String number = columns[columns.length - 1]; // get last column

				if (debug)
					System.out.println("Number: " + number);

				try {
					// Attempt to parse it
					int value = Integer.parseInt(number);
					
					// If successful
					if (value > 0) { 
						// end process
						runProcess.destroy();

						// Debug information
						if (debug) {
							long finish = System.currentTimeMillis();
							long taken = finish - start;
							System.out.println("Time taken: " + taken);
						}
						// Return parsed value to caller
						return value;
					}

					// Tidy up
				} catch (Exception e) {
					System.err.print("Couldn't parse number");
				}
			}

			// If no result
			runProcess.destroy(); // end process
			if (debug)
				print("[DEBUG] Finished processing. Time: "
						+ System.currentTimeMillis());

			// Catch errors & tidy up
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Else signal error
		return -1;
	}

	private static void print(String s) {
		String output = "[EnergyMonitor] " + s;
		System.out.println(output);
	}

	private int snmpCommand(String info) {
		// Set up get active power snmp command
		String cmd = "snmpwalk -v1 -c public -m \"./MIB.txt\" "
				+ this.PDU_IPAddress + " " + info + "." + this.socket;

		// Execute snmp command
		return executeCommand(cmd);
	}

	public float getCurrent() {
		return (float) snmpCommand("outletCurrent") / (float) 1000;
	}

	public int getWattHours() {
		return snmpCommand("outletWattHours");
	}

	public int getVoltage() {
		return snmpCommand("outletVoltage") / 1000;
	}

	public int getActivePower() {
		return snmpCommand("outletActivePower");
	}

	public Data getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSocket() {
		return this.socket;
	}

	// ###########################################################
	/**
	 * TEST METHOD
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Create new monitor for socket and get Active Power
		EnergyMonitor energy = new EnergyMonitor(5, false);
		energy.start();
	}
}