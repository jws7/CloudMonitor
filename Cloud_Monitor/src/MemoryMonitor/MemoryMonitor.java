package MemoryMonitor;

import java.io.File;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import uk.ac.standrews.cs.nds.util.SSH2ConnectionWrapper;

public class MemoryMonitor extends Thread {

	// Database connection
	private Connection conn;
	
	// Debug notifier
	private boolean debug = false;

	public MemoryMonitor() {
		try {			
			connectToDatabase();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Threaded method to run on loop - will get all memory data for server every three secs
	 * 
	 */
	public void run() {
		
		while (true) {
			long start = System.currentTimeMillis();

			MemoryPoller poller = new MemoryPoller(this.conn);
			poller.start();

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
	
	private void connectToDatabase() {
		// Default Connection details
		String databaseURL = "jdbc:mysql://";
		String databaseUserName = "User";
		String databasePassword = "Passwd";

		// Open config file
		try {
			File config = new File("sql" + File.separator + "config.txt");
			if(debug) System.out.println("Attempting to open config file: "
					+ config.getCanonicalPath());
			Scanner sc = new Scanner(config);

			databaseURL += sc.next();
			databaseUserName = sc.next();
			databasePassword = sc.next();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(debug) System.out.println("Creating a DatabaseConnection");

		try {

			if(debug) System.out.println("Attempting to connect to " + databaseURL
					+ " with user: " + databaseUserName + " and pass.. "
					+ databasePassword);

			conn = DriverManager.getConnection(databaseURL, databaseUserName,
					databasePassword);

			if(debug) System.out.println("Finished attempt");

		} catch (Exception e) {
			System.err.println("Attempt to connect failed...");
			e.printStackTrace();
		}
		if(debug) System.out.println("Connection to DB successful");
	}

	public static void main(String[] args) {
		// Create new monitor for socket and get Active Power
		MemoryMonitor monitor = new MemoryMonitor();
		monitor.start();
	}
}