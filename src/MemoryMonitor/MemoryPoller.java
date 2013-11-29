package MemoryMonitor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Scanner;

import uk.ac.standrews.cs.nds.util.ErrorHandling;
import uk.ac.standrews.cs.nds.util.Processes;
import uk.ac.standrews.cs.nds.util.SSH2ConnectionWrapper;

public class MemoryPoller extends Thread {

	// Database connection
	private Connection conn;

	// Should we debug?
	boolean debug = false;

	public MemoryPoller(Connection conn) {
		super();
		this.conn = conn;
	}

	public void run() {
		try {
			String cmd = "free";

			// Print command to run
			if (debug)
				print("Command: [" + cmd + "]");

			// Create output stream to use
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// Execute command on remote host
			long start = System.currentTimeMillis();
			// Process runProcess = Processes.runProcess(cmd, this.ssh, out,
			// System.err);

			Process process = Runtime.getRuntime().exec(cmd);

			if (debug) {
				print("[DEBUG] Process running. Time: " + start);
			}

			// Wait for subprocess to finish... then we can read in the buffered
			// stream
			// runProcess.waitFor();

			// Convert output stream to an input stream we can read (via
			// ByteArray)
			// InputStream in = new ByteArrayInputStream(out.toByteArray());

			// Deal with result
			// BufferedReader reader = new BufferedReader(
			// new InputStreamReader(in));

			DataInputStream input = new DataInputStream(
					process.getInputStream());

			String line = null;
			String specialLine = "";

			while ((line = input.readLine()) != null) {

				// Skip if empty string
				if (line.equals(""))
					continue;

				// Print if debuging..
				if (debug)
					System.out.println(line);

				// Look for special line
				if (line.contains("-/+"))
					specialLine = line;
			}
			// end process
			// runProcess.destroy();

			// Calculate length of operation
			long finish = System.currentTimeMillis();
			long taken = finish - start;
			if (debug)
				System.out.println("Time taken: " + taken);

			// Extract answer
			if (debug)
				print("Relevant Line:  [" + specialLine + "]");
			String[] array = specialLine.split(" ");
			if (debug){
				print("Array contains: " + array.length + " items");
				for(String s: array){
					print("Array item: [" + s + "]" );
				}
			}
			String freeStr = array[array.length - 1];
			String usedStr = array[array.length - 4];
			if(usedStr.equals(""))
				usedStr = array[array.length - 5]; // Fix a bug with some versions of free
			if (debug)
				print("Free: [" + freeStr + "]");
			if (debug) print("used: [" + usedStr + "]");

			// Parse
			long free = Long.parseLong(freeStr);
			long used = Long.parseLong(usedStr);
			long total = used + free;
			if (debug)
				print("total: [" + total + "]");

			// Insert into database
			insert(used);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insert(long used) {
		// Insert Memory used value into database

		// First of all... get machine ID for this IP address
		if (debug)
			print("Retrieving machineID for " + getFirstNonLoopbackAddress());

		try {
			// Do a get statement
			Statement stmt = conn.createStatement();

			// Prepare query
			String query = "SELECT machine_id FROM SYS_INFO WHERE primary_ip = '"
					+ getFirstNonLoopbackAddress() + "'";

			// Execute
			ResultSet rs = stmt.executeQuery(query);
			rs.next();

			// Extract
			String machineID = rs.getString(1);
			if (debug)
				print("MachineID: " + machineID);

			// Now insert this value into the DB for this machine at this time.
			String cmd = "INSERT INTO RESOURCE_MONITORING.MEMORY_UTIL VALUES(?, NOW(), ?)";
			PreparedStatement prepared = conn.prepareStatement(cmd);
			prepared.setString(1, machineID);
			prepared.setLong(2, used);
			if (debug)
				print(prepared.toString());

			// Execute update
			prepared.executeUpdate();
			prepared.close();
			if (debug)
				print("Inserted");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getFirstNonLoopbackAddress() {
		try {
			Enumeration en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface i = (NetworkInterface) en.nextElement();
				for (Enumeration en2 = i.getInetAddresses(); en2
						.hasMoreElements();) {
					InetAddress addr = (InetAddress) en2.nextElement();
					if (!addr.isLoopbackAddress()) {
						if (addr instanceof Inet4Address) {
							return addr.getHostAddress().toString();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void print(String s) {
		String output = "[LocalMemoryPoller] " + s;
		System.out.println(output);
	}
}