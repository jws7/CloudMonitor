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
package Utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import DataTypes.EnergyMonitorData;
import DataTypes.FileSystemData;
import DataTypes.MachineUtilisationData;
import DataTypes.NetMonitorData;
import DataTypes.SysInfoData;
import Experiments.Experiment;

import uk.ac.standrews.cs.nds.util.ErrorHandling;

/**
 * 
 * @author james.w.smith@st-andrews.ac.uk
 *
 */
public class DatabaseConnection {

	// Default Connection details
	String databaseURL = "jdbc:mysql://";
	String databaseUserName = "User";
	String databasePassword = "Passwd";
	
	
	private Connection conn;
	private boolean debug;

	/**
	 * The date format used to add dates to the database.
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss"); // 2010-08-04 13:51:14.539

	public DatabaseConnection(boolean debug) {
		this();
		this.debug = debug;
		executeSchema();
	}

	public void executeSchema() {
		try {

			System.out.println("Beginning to execute the schema");

			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(
					"sql" + File.separator
							+ "resource_monitoring.sql");

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// Read File Line By Line
			String strLine, sqlLine = " ";
			while ((strLine = br.readLine()) != null) {

				// Skip empty lines & comments
				if (strLine.length() == 0
						|| (strLine.length() > 0 && strLine.charAt(0) == '-')) {
					continue;
				}

				// Add this line to the sql command
				sqlLine = sqlLine + " " + strLine;

				// If the command is now complete
				if (sqlLine.charAt(sqlLine.length() - 1) == ';') {

					// first, remove the ; to avoid errors
					sqlLine = sqlLine.replace(';', ' ');
					System.out.println(sqlLine);

					// then execute query
					PreparedStatement stmt = this.conn
							.prepareStatement(sqlLine);
					stmt.execute();

					// refresh the line
					sqlLine = "";
				}
			}

			// Close the input stream
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public DatabaseConnection() {

		// Open config file
		try {
			File config = new File("sql" + File.separator
					+ "config.txt");
			System.out.println("Attempting to open config file: "
					+ config.getCanonicalPath());
			Scanner sc = new Scanner(config);

			databaseURL += sc.next();
			databaseUserName = sc.next();
			databasePassword = sc.next();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Creating a DatabaseConnection");


		try {
			Class.forName("com.mysql.jdbc.Driver"); // Load driver
			System.out.println("[Init] JDBC driver loaded succesfully");
		} catch (ClassNotFoundException x) {
			System.out.println("JDBC driver could not be loaded");
		}

		try {

			System.out.println("Attempting to connect to [" + databaseURL
					+ "] with user: [" + databaseUserName + "] and password: ["
					+ databasePassword + "]");

			conn = DriverManager.getConnection(databaseURL, databaseUserName,
					databasePassword);

			System.out.println("Finished attempt");

		} catch (Exception e) {
			System.err.println("Attempt to connect failed...");
			e.printStackTrace();
			ErrorHandling
					.hardError("[DatabaseConnection.constructor] Database not found. Make sure a database instance is running at: "
							+ databaseURL);
		}
		System.out.println("Connection to DB successful");

		// executeSQLScript("sql-schema" + File.separator +
		// "monitorSchema.sql");
	}

	public int getSocketNoForMachineID(String machineID) {

		try {

			// Execute statement
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT socket_num FROM MACHINE_PDU_MAP WHERE machine_id = '"
							+ machineID + "';");

			rs.next();
			// Get the data from the row using the column index
			String s = rs.getString(1);

			// Get the data from the row using the column name
			s = rs.getString("socket_num");

			// Close the connection
			stmt.close();

			// Parse & return the integer
			return Integer.parseInt(s);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Else signal error
		return -1;
	}

	public String getMachineIDforSocket(int i) {
		// System.out.println("Getting address for socket: " + i);
		// i = 1;
		try {

			// Execute statement
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT machine_id FROM MACHINE_PDU_MAP WHERE socket_num = "
							+ i + ";");

			rs.next();
			// Get the data from the row using the column index
			String s = rs.getString(1);

			// Get the data from the row using the column name
			s = rs.getString("machine_id");

			// Close the connection
			stmt.close();

			// Parse & return the integer
			return s;

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Else signal error
		return "error!";
	}

	/*
	 * TODO -- Put MachineUtil Data into DB then pull from PHPx
	 */
	public void insertMachineUtilData(MachineUtilisationData results) {

		try {
			// Build SQL command from data
			String cmd = "INSERT INTO RESOURCE_MONITORING.MACHINE_UTIL VALUES("
					+ "?" + ",  NOW(), " + "?, " + "?, " + "?, " + "?, "
					+ "?, " + "?, " + "?, " + "?, " + "?);";

			PreparedStatement stmt = this.conn.prepareStatement(cmd);

			// Insert variables
			stmt.setString(1, results.machineID);
			stmt.setDouble(2, results.cpu_user_total);
			stmt.setDouble(3, results.cpu_sys_total);
			stmt.setDouble(4, results.cpu_idle_total);
			stmt.setDouble(5, results.cpu_nice_total);
			stmt.setDouble(6, results.cpu_wait_total);
			stmt.setLong(7, results.memory_used);
			stmt.setLong(8, results.memory_free);
			stmt.setLong(9, results.swap_used);
			stmt.setLong(10, results.swap_free);

			System.out.println(cmd);

			// Now execute
			executeUpdate(stmt);

			for (FileSystemData summary : results.fs)
				insertFileSystemData(summary, results.machineID,
						results.getDate());
			for (NetMonitorData summary : results.nets)
				insertNetworkData(summary, results.machineID, results.getDate());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertNetworkData(NetMonitorData summary, String machine_id,
			String date) {

		System.out
				.println("[InsertNetworkData] Updating network information for "
						+ machine_id);

		try {
			// Build SQL command from data
			String cmd = "INSERT INTO RESOURCE_MONITORING.NETWORK VALUES(?,  NOW(), ?, ?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, " + "?, " + "?);";

			PreparedStatement stmt = this.conn.prepareStatement(cmd);
			System.out.println("[InsertNetworkData] Preparing command ");

			// Insert variables
			stmt.setString(1, machine_id);
			stmt.setString(2, summary.device_name);
			stmt.setString(3, summary.ip_address);

			stmt.setLong(4, summary.rx_bytes);
			stmt.setLong(5, summary.rx_packets);
			stmt.setLong(6, summary.rx_errors);
			stmt.setLong(7, summary.rx_dropped);
			stmt.setLong(8, summary.rx_overruns);

			stmt.setLong(9, summary.tx_bytes);
			stmt.setLong(10, summary.tx_packets);
			stmt.setLong(11, summary.tx_errors);
			stmt.setLong(12, summary.tx_dropped);
			stmt.setLong(13, summary.tx_overruns);
			System.out
					.println("[InsertNetworkData] Finished preparing command: "
							+ stmt.toString());

			// and execute
			executeUpdate(stmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertFileSystemData(FileSystemData summary, String machine_id,
			String date) {

		System.out.println("[insertFileSystemData] Preparing SQL Statement");

		try {

			// Build SQL command from data
			String cmd = "INSERT INTO RESOURCE_MONITORING.FILE_SYSTEM VALUES(?,  NOW(),"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			PreparedStatement stmt = this.conn.prepareStatement(cmd);
			System.out.println("[insertFileSystemData] " + stmt.toString());

			// Insert variables
			stmt.setString(1, machine_id);
			stmt.setString(2, summary.fileSystemName);

			String fileSystemLocation = summary.fileSystemLocation;

			if (summary.fileSystemName.contains("\\")) {
				summary.fileSystemName = summary.fileSystemName.substring(0,
						summary.fileSystemName.length() - 1);
			}
			if (summary.fileSystemLocation.contains("\\")) {
				fileSystemLocation = summary.fileSystemLocation.substring(0,
						summary.fileSystemLocation.length() - 1);
			}

			stmt.setString(3, fileSystemLocation);
			stmt.setString(4, summary.fileSystemType);

			stmt.setLong(5, summary.fs_size);
			stmt.setLong(6, summary.fs_free);
			stmt.setLong(7, summary.fs_used);
			stmt.setLong(8, summary.fs_files);

			stmt.setLong(9, summary.fs_disk_reads);
			stmt.setLong(10, summary.fs_disk_read_bytes);
			stmt.setLong(11, summary.fs_disk_writes);
			stmt.setLong(12, summary.fs_disk_write_bytes);

			// and execute for each
			executeUpdate(stmt);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertEnergyInfo(EnergyMonitorData energy) {
		//System.out.println("[insertEnergyInfo] Preparing SQL Statement");
		try {
			// Build SQL command from data
			String cmd = "INSERT IGNORE INTO RESOURCE_MONITORING.ENERGY VALUES(NOW(), "
					+ "?, ?, ?, ?, ?, ?);";

			PreparedStatement stmt = this.conn.prepareStatement(cmd);
			//System.out.println("[insertEnergyInfo] " + stmt.toString());

			stmt.setString(1, getMachineIDforSocket(energy.socket));
			stmt.setInt(2, energy.socket);
			stmt.setInt(3, energy.activePower);
			stmt.setInt(4, energy.voltage);
			stmt.setFloat(5, energy.current);
			stmt.setInt(6, energy.wattHours);

			// Now execute
			executeUpdate(stmt);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertSystemInfo(SysInfoData data) {

		System.out.println("[insertSystemInfo] Preparign SQL Statement");

		try {
			// Build SQL command from data
			String cmd = "INSERT IGNORE INTO RESOURCE_MONITORING.SYS_INFO VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			PreparedStatement stmt = this.conn.prepareStatement(cmd);
			System.out.println("[insertSystemInfo] " + stmt.toString());

			stmt.setString(1, data.machine_id);
			stmt.setString(2, data.hostname);
			stmt.setString(3, data.primary_ip);
			stmt.setString(4, data.cpu_vendor);
			stmt.setString(5, data.cpu_model);

			stmt.setInt(6, data.num_cores);
			stmt.setInt(7, data.num_cpus);
			stmt.setInt(8, data.cpu_mhz);
			stmt.setLong(9, data.cpu_cache_size);

			stmt.setString(10, data.os_name);
			stmt.setString(11, data.os_version);
			stmt.setString(12, data.default_gateway);

			stmt.setLong(13, data.memory_total);
			stmt.setLong(14, data.swap_total);

			// Now execute
			executeUpdate(stmt);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertExperimentInfo(Experiment data) {
		System.out.println("[insertExperimentInfo] Preparign SQL Statement");

		try {
			// Build SQL command from data
			String cmd = "INSERT IGNORE INTO RESOURCE_MONITORING.EXPERIMENTS VALUES('"
					+ null + "', ?, ?, ?, ?, ?);";

			PreparedStatement stmt = this.conn.prepareStatement(cmd);
			System.out.println("[insertSystemInfo] " + stmt.toString());

			stmt.setString(1, data.command);
			stmt.setString(2, data.startTime());
			stmt.setString(3, data.finishTime());
			stmt.setString(4, data.benchmarkURL);
			stmt.setString(5, data.output.toString());

			// Now execute
			executeUpdate(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertPredictionData(double[] predictions, ResultSet rs1) {

		System.out
				.println("Regression Analysis Complete."
						+ "\n[insertPredictionData] Updating predictions in database... Please Wait.");

		try {
			rs1.beforeFirst();

			for (double d : predictions) {
				if (d > 0) {
					rs1.next();
					String time = rs1.getString("time");
					double predicted = d;
					double actual = rs1.getDouble("activePower");
					double error = ((predicted - actual) / actual) * 100;

					// Build SQL command from data
					String cmd = "INSERT IGNORE INTO RESOURCE_MONITORING.POWER_PREDICTS VALUES('"
							+ null + "', ?, ?, ?, ?);";

					PreparedStatement stmt = this.conn.prepareStatement(cmd);
					System.out.println("[insertPredictionData] "
							+ stmt.toString());

					stmt.setString(1, time);
					stmt.setDouble(2, predicted);
					stmt.setDouble(3, actual);
					stmt.setDouble(4, error);

					// Now execute
					executeUpdate(cmd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int executeUpdate(String cmd) {
		// DEBUG: Console output of the SQL string being executed
		if (debug)
			System.out.println("[DEBUG][sendCommand] to " + databaseURL + ": "
					+ cmd);

		try {
			// Create statement object
			Statement stmt = this.conn.createStatement();
			int res = stmt.executeUpdate(cmd);
			if (debug)
				System.out
						.println("[DEBUG][sendCommand] Update completed successfully, "
								+ res + " rows effected");

			// Disconnect statement
			stmt.close();

			// Signal success
			return res;

			// Standard Catch block
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Signal error if necessary
		return -1;
	}

	private int executeUpdate(PreparedStatement stmt) {
		//System.out.println("Executing update:");
		try {
			// Create statement object
			//System.out.println(stmt.toString());
			int res = stmt.executeUpdate();
			//System.out
			//		.println("[DEBUG][executeUpdate] Update completed successfully, "
			//				+ res + " rows effected");

			// Disconnect statement
			stmt.close();

			// Signal success
			return res;

			// Standard Catch block
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Signal error if necessary
		return -1;
	}

	public ArrayList<MachineUtilisationData> getResourceData(String id,
			Date start, Date finish) {

		ArrayList<MachineUtilisationData> data = new ArrayList<MachineUtilisationData>();

		try {
			// Execute statement
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM RESOURCE_MONITORING.MACHINE_UTIL "
					+ "WHERE machine_id = '" + id + "' AND time > '"
					+ sdf.format(start) + "' AND time < '" + sdf.format(finish)
					+ "';";

			System.out.println("[DatabaseConnection] " + query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				MachineUtilisationData results = new MachineUtilisationData(id);

				results.cpu_idle_total = Double.parseDouble(rs
						.getString("cpu_idle"));
				results.cpu_nice_total = Double.parseDouble(rs
						.getString("cpu_nice"));
				results.cpu_sys_total = Double.parseDouble(rs
						.getString("cpu_sys"));
				results.cpu_user_total = Double.parseDouble(rs
						.getString("cpu_user"));
				results.cpu_wait_total = Double.parseDouble(rs
						.getString("cpu_wait"));

				results.memory_free = Integer.parseInt(rs
						.getString("memory_free"));
				results.memory_used = Integer.parseInt(rs
						.getString("memory_used"));
				results.swap_free = Integer.parseInt(rs.getString("swap_free"));
				results.swap_used = Integer.parseInt(rs.getString("swap_used"));

				data.add(results);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	// AND repeat for Energy
	public ArrayList<EnergyMonitorData> getEnergyData(String id, Date start,
			Date finish) {

		ArrayList<EnergyMonitorData> data = new ArrayList<EnergyMonitorData>();

		int socket = this.getSocketNoForMachineID(id);

		try {
			// Execute statement
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM RESOURCE_MONITORING.ENERGY "
					+ "WHERE machine_id = '" + id + "' AND time > '"
					+ sdf.format(start) + "' AND time < '" + sdf.format(finish)
					+ "';";

			System.out.println("[DatabaseConnection] " + query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				EnergyMonitorData results = new EnergyMonitorData();

				results.socket = socket;
				results.activePower = Integer.parseInt(rs
						.getString("activePower"));
				results.voltage = Integer.parseInt(rs.getString("voltage"));
				results.wattHours = Integer.parseInt(rs.getString("wattHours"));
				results.current = Float.parseFloat(rs.getString("current"));

				data.add(results);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	public ResultSet[] getPowerModelData(String start_t, String end_t) {

		// String start_t = "'2011-04-25 20:59:51'";
		// String end_t = "'2011-04-25 21:14:21'";

		String energy_q = "SELECT activePower, time FROM ENERGY WHERE time < "
				+ end_t + " AND time >= " + start_t + ";";
		String machine_util_q = "SELECT cpu_user, cpu_sys, memory_used, memory_free FROM MACHINE_UTIL WHERE time < "
				+ end_t + " AND time >= " + start_t + ";";
		String hdd_q = "SELECT fs_disk_reads, fs_disk_writes FROM FILE_SYSTEM WHERE time < "
				+ end_t + " AND time >= " + start_t + " AND fs_name = '/';";
		String net_q = "SELECT rx_bytes, tx_bytes FROM NETWORK WHERE time < "
				+ end_t + " AND time >= " + start_t
				+ " AND device_name = 'lo' ORDER BY time ASC;";

		Statement stmt1;
		Statement stmt2;
		Statement stmt3;
		Statement stmt4;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;

		try {
			stmt1 = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();
			stmt4 = conn.createStatement();

			// Energy
			System.out.println("[DatabaseConnection] " + energy_q);
			rs1 = stmt1.executeQuery(energy_q);

			// Machine Util
			System.out.println("[DatabaseConnection] " + machine_util_q);
			rs2 = stmt2.executeQuery(machine_util_q);

			// HDD Util
			System.out.println("[DatabaseConnection] " + hdd_q);
			rs3 = stmt3.executeQuery(hdd_q);

			// NET Util
			System.out.println("[DatabaseConnection] " + net_q);
			rs4 = stmt4.executeQuery(net_q);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet[] array = new ResultSet[4];
		array[0] = rs1;
		array[1] = rs2;
		array[2] = rs3;
		array[3] = rs4;

		return array;
	}

	public ResultSet[] getPowerModelData(String start_t, String end_t,
			String machine_id) {

		String energy_q = "SELECT activePower, time FROM ENERGY WHERE time < "
				+ end_t + " AND time >= " + start_t + " AND machine_id = "
				+ machine_id + ";";
		String machine_util_q = "SELECT cpu_user, cpu_sys, memory_used, memory_free FROM MACHINE_UTIL WHERE time < "
				+ end_t
				+ " AND time >= "
				+ start_t
				+ "AND machine_id = "
				+ machine_id + ";";
		String hdd_q = "SELECT fs_disk_reads, fs_disk_writes FROM FILE_SYSTEM WHERE time < "
				+ end_t
				+ " AND time >= "
				+ start_t
				+ " AND fs_name = '/' AND machine_id = " + machine_id + ";";
		String net_q = "SELECT rx_bytes, tx_bytes FROM NETWORK WHERE time < "
				+ end_t + " AND time >= " + start_t
				+ " AND device_name = 'lo' AND machine_id = " + machine_id
				+ " ORDER BY time ASC;";

		Statement stmt1;
		Statement stmt2;
		Statement stmt3;
		Statement stmt4;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;

		try {
			stmt1 = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();
			stmt4 = conn.createStatement();

			// Energy
			System.out.println("[DatabaseConnection] " + energy_q);
			rs1 = stmt1.executeQuery(energy_q);

			// Machine Util
			System.out.println("[DatabaseConnection] " + machine_util_q);
			rs2 = stmt2.executeQuery(machine_util_q);

			// HDD Util
			System.out.println("[DatabaseConnection] " + hdd_q);
			rs3 = stmt3.executeQuery(hdd_q);

			// NET Util
			System.out.println("[DatabaseConnection] " + net_q);
			rs4 = stmt4.executeQuery(net_q);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet[] array = new ResultSet[4];
		array[0] = rs1;
		array[1] = rs2;
		array[2] = rs3;
		array[3] = rs4;

		return array;
	}

	/**
	 * Return the SysInfoData of all currently active servers
	 * 
	 * @return - ArrayList of SysInfoData objects
	 */
	public ArrayList<SysInfoData> getAllActiveServers() {

		// Setup collection to hold data
		ArrayList<SysInfoData> data = new ArrayList<SysInfoData>();

		try {
			// Create & Execute statement
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM `resource_monitoring`.`sys_info`;";
			// String query = "SELECT * FROM SYS_INFO;";

			// System.out.println("[DatabaseConnection] " + query);
			ResultSet rs = stmt.executeQuery(query);

			// Loop through all results
			while (rs.next()) {

				// Create SysInfoData object
				SysInfoData system = new SysInfoData();

				// extract data from SQL Query Result
				system.machine_id = rs.getString("machine_id");
				system.hostname = rs.getString("hostname");
				system.primary_ip = rs.getString("primary_ip");

				system.cpu_vendor = rs.getString("cpu_vendor");
				system.cpu_model = rs.getString("cpu_model");
				system.num_cores = Integer.parseInt(rs.getString("num_cores"));
				system.num_cpus = Integer.parseInt(rs.getString("num_cpus"));
				system.cpu_mhz = Integer.parseInt(rs.getString("cpu_mhz"));
				system.cpu_cache_size = Long.parseLong(rs
						.getString("cpu_cache_size"));

				system.os_name = rs.getString("os_name");
				system.os_version = rs.getString("os_version");

				system.default_gateway = rs.getString("default_gateway");

				system.memory_total = Long.parseLong(rs
						.getString("memory_total"));
				system.swap_total = Long.parseLong(rs.getString("swap_total"));

				// add to collection if active in last 30 seconds
				if (isActive(system.primary_ip))
					data.add(system);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return collection
		return data;
	}

	public double[][] getNetUsage(String ipAddress) {

		try {
			// Create & Execute statement
			Statement stmt = conn.createStatement();
			// String query = "SELECT time, cpu_user, cpu_sys, cpu_idle "
			// + "FROM MACHINE_UTIL AS util, "
			// + "SYS_INFO AS sysInfo "
			// + "WHERE util.machine_id = sysInfo.machine_id "
			// + "AND sysInfo.primary_ip = '" + ipAddress + "' "
			// + "ORDER BY time DESC LIMIT 100;";
			String query = "SELECT time, rx_bytes, tx_bytes "
					+ "FROM NETWORK, SYS_INFO "
					+ "WHERE SYS_INFO.machine_id = NETWORK.machine_id "
					+ "AND SYS_INFO.primary_ip = '" + ipAddress + "' "
					+ "AND time >= DATE_SUB(NOW(), INTERVAL 5 MINUTE) "
					+ "AND time <= NOW() " + "ORDER BY time DESC;";
			// System.out.println("[DatabaseConnection] " + query);

			ResultSet rs = stmt.executeQuery(query);
			// System.out.println("Query executed...");

			// System.out.println("Counting resultset...");
			int count = 0;
			// Count resultset
			while (rs.next()) {
				count++;
			}

			// System.out.println(count + " rows");
			rs.beforeFirst();

			// Create array to return
			double[][] results = new double[count][2];

			Timestamp start = null;
			int i = 0;

			// System.out.println("Begining traversal of results...");
			while (rs.next()) {

				double temp = 0;

				Timestamp t = rs.getTimestamp("time");
				if (start == null)
					start = t;
				else {
					long first = start.getTime();
					long second = t.getTime();

					temp = (double) first - second;
				}
				results[i][0] = temp;

				double netRec = rs.getDouble("rx_bytes");
				double netTrans = rs.getDouble("tx_bytes");
				double netOps = netRec + netTrans;

				results[i++][1] = netOps;
			}
			// outputArray(results);
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public double[][] getHddUsage(String ipAddress) {

		try {
			// Create & Execute statement
			Statement stmt = conn.createStatement();
			// String query = "SELECT time, cpu_user, cpu_sys, cpu_idle "
			// + "FROM MACHINE_UTIL AS util, "
			// + "SYS_INFO AS sysInfo "
			// + "WHERE util.machine_id = sysInfo.machine_id "
			// + "AND sysInfo.primary_ip = '" + ipAddress + "' "
			// + "ORDER BY time DESC LIMIT 100;";
			String query = "SELECT time, fs_disk_reads, fs_disk_writes "
					+ "FROM FILE_SYSTEM, SYS_INFO "
					+ "WHERE SYS_INFO.machine_id = FILE_SYSTEM.machine_id "
					+ "AND SYS_INFO.primary_ip = '" + ipAddress + "' "
					+ "AND time >= DATE_SUB(NOW(), INTERVAL 5 MINUTE) "
					+ "AND time <= NOW() " + "ORDER BY time DESC;";
			// System.out.println("[DatabaseConnection] " + query);

			ResultSet rs = stmt.executeQuery(query);
			// System.out.println("Query executed...");

			// System.out.println("Counting resultset...");
			int count = 0;
			// Count resultset
			while (rs.next()) {
				count++;
			}

			// System.out.println(count + " rows");
			rs.beforeFirst();

			// Create array to return
			double[][] results = new double[count][2];

			Timestamp start = null;
			int i = 0;

			// System.out.println("Begining traversal of results...");
			while (rs.next()) {

				double temp = 0;

				Timestamp t = rs.getTimestamp("time");
				if (start == null)
					start = t;
				else {
					long first = start.getTime();
					long second = t.getTime();

					temp = (double) first - second;
				}
				results[i][0] = temp;

				double diskReads = rs.getDouble("fs_disk_reads");
				double diskWrites = rs.getDouble("fs_disk_writes");
				double diskOps = diskReads + diskWrites;

				results[i++][1] = diskOps;
			}
			// outputArray(results);
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public double[][] getMemUsage(String ipAddress) {

		try {
			// Create & Execute statement
			Statement stmt = conn.createStatement();
			// String query = "SELECT time, cpu_user, cpu_sys, cpu_idle "
			// + "FROM MACHINE_UTIL AS util, "
			// + "SYS_INFO AS sysInfo "
			// + "WHERE util.machine_id = sysInfo.machine_id "
			// + "AND sysInfo.primary_ip = '" + ipAddress + "' "
			// + "ORDER BY time DESC LIMIT 100;";
			String query = "SELECT time, memory_used "
					+ "FROM `resource_monitoring`.`machine_util` AS util, "
					+ "`resource_monitoring`.`sys_info` AS sysInfo "
					+ "WHERE util.machine_id = sysInfo.machine_id "
					+ "AND sysInfo.primary_ip = '" + ipAddress + "' "
					+ "AND time >= DATE_SUB(NOW(), INTERVAL 5 MINUTE) "
					+ "AND time <= NOW() " + "ORDER BY time DESC;";
			// System.out.println("[DatabaseConnection] " + query);

			ResultSet rs = stmt.executeQuery(query);
			// System.out.println("Query executed...");

			// System.out.println("Counting resultset...");
			int count = 0;
			// Count resultset
			while (rs.next()) {
				count++;
			}

			// System.out.println(count + " rows");
			rs.beforeFirst();

			// Create array to return
			double[][] results = new double[count][2];

			Timestamp start = null;
			int i = 0;

			// System.out.println("Begining traversal of results...");
			while (rs.next()) {

				double temp = 0;

				Timestamp t = rs.getTimestamp("time");
				if (start == null)
					start = t;
				else {
					long first = start.getTime();
					long second = t.getTime();

					temp = (double) first - second;
				}
				results[i][0] = temp;

				double mem_used = rs.getDouble("memory_used");

				results[i++][1] = mem_used;
			}
			// outputArray(results);
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public double[][] getCPUusage(String ipAddress) {

		try {
			// Create & Execute statement
			Statement stmt = conn.createStatement();
			// String query = "SELECT time, cpu_user, cpu_sys, cpu_idle "
			// + "FROM MACHINE_UTIL AS util, "
			// + "SYS_INFO AS sysInfo "
			// + "WHERE util.machine_id = sysInfo.machine_id "
			// + "AND sysInfo.primary_ip = '" + ipAddress + "' "
			// + "ORDER BY time DESC LIMIT 100;";
			String query = "SELECT time, cpu_user, cpu_sys, cpu_idle "
					+ "FROM `resource_monitoring`.`machine_util` AS util, "
					+ "`resource_monitoring`.`sys_info` AS sysInfo "
					+ "WHERE util.machine_id = sysInfo.machine_id "
					+ "AND sysInfo.primary_ip = '" + ipAddress + "' "
					+ "AND time >= DATE_SUB(NOW(), INTERVAL 5 MINUTE) "
					+ "AND time <= NOW() " + "ORDER BY time DESC;";
			// System.out.println("[DatabaseConnection] " + query);

			ResultSet rs = stmt.executeQuery(query);
			// System.out.println("Query executed...");

			// System.out.println("Counting resultset...");
			int count = 0;
			// Count resultset
			while (rs.next()) {
				count++;
			}

			// System.out.println(count + " rows");
			rs.beforeFirst();

			// Create array to return
			double[][] results = new double[count][2];

			Timestamp start = null;
			int i = 0;

			// System.out.println("Begining traversal of results...");
			while (rs.next()) {

				double temp = 0;

				Timestamp t = rs.getTimestamp("time");
				if (start == null)
					start = t;
				else {
					long first = start.getTime();
					long second = t.getTime();

					temp = (double) first - second;
				}
				results[i][0] = temp;

				double cpu_user = rs.getDouble("cpu_user");
				double cpu_sys = rs.getDouble("cpu_sys");
				double cpu_idle = rs.getDouble("cpu_idle");
				double totalCPU = cpu_user + cpu_sys;

				results[i++][1] = totalCPU;

			}
			// outputArray(results);
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void outputArray(double[][] array) {
		int rowSize = array.length;
		int columnSize = array[0].length;
		for (int i = 0; i < rowSize; i++) {
			System.out.print("[");
			for (int j = 0; j < columnSize; j++) {
				System.out.print(" " + array[i][j]);
			}
			System.out.println(" ]");
		}
		System.out.println();
	}

	public SysInfoData getServerForIP(String ipAddress) {
		try {
			// Create & Execute statement
			Statement stmt = conn.createStatement();
			// String query = "SELECT * FROM SYS_INFO"
			// + " WHERE primary_ip = '" + ipAddress + "';";
			String query = "SELECT * FROM SYS_INFO"
					+ " WHERE primary_ip = '" + ipAddress + "';";

			// System.out.println("[DatabaseConnection] " + query);
			ResultSet rs = stmt.executeQuery(query);

			rs.next();

			// Create SysInfoData object
			SysInfoData system = new SysInfoData();

			// extract data from SQL Query Result
			system.machine_id = rs.getString("machine_id");
			system.hostname = rs.getString("hostname");
			system.primary_ip = rs.getString("primary_ip");

			system.cpu_vendor = rs.getString("cpu_vendor");
			system.cpu_model = rs.getString("cpu_model");
			system.num_cores = Integer.parseInt(rs.getString("num_cores"));
			system.num_cpus = Integer.parseInt(rs.getString("num_cpus"));
			system.cpu_mhz = Integer.parseInt(rs.getString("cpu_mhz"));
			system.cpu_cache_size = Long.parseLong(rs
					.getString("cpu_cache_size"));

			system.os_name = rs.getString("os_name");
			system.os_version = rs.getString("os_version");

			system.default_gateway = rs.getString("default_gateway");

			system.memory_total = Long.parseLong(rs.getString("memory_total"));
			system.swap_total = Long.parseLong(rs.getString("swap_total"));

			return system;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] getAllSeenServerIPs() {

		// Get all current active servers
		ArrayList<SysInfoData> servers = this.getAllActiveServers();
		String[] names = new String[servers.size()];

		// Loop through all
		for (int i = 0; i < servers.size(); i++) {
			// and extract hostnames;
			names[i] = servers.get(i).primary_ip;
		}
		return names;
	}

	public String[] getAllSeenServerNames() {

		// Get all current active servers
		ArrayList<SysInfoData> servers = this.getAllActiveServers();
		String[] names = new String[servers.size()];

		// Loop through all
		for (int i = 0; i < servers.size(); i++) {
			// and extract hostnames;
			names[i] = servers.get(i).hostname;
		}
		return names;
	}

	public double[] getCPUAvgs() {

		// Get list of active servers
		ArrayList<SysInfoData> servers = this.getAllActiveServers();

		// Create double array
		double[] results = new double[servers.size()];
		// System.out.println("Traversing " + servers.size() +
		// " active servers");

		// For all servers
		int count = 0;
		for (SysInfoData sysInfo : servers) {

			// Extract their cpu usage
			double[][] usage = this.getCPUusage(sysInfo.primary_ip);

			// Average that usage
			double total = 0.0;
			for (int i = 0; i < usage.length; i++) {
				total += usage[i][1];
			}
			double avg = total / usage.length;
			// System.out.println("Avg usage for server " + sysInfo.hostname
			// + " equals: " + avg);

			// add to results array
			results[count++] = avg;
		}
		return results;
	}

	public double getCPUusageAvg(String string) {
		double[][] last5mins = this.getCPUusage(string);
		// this.outputArray(last5mins);

		double count = 0.0;
		for (int i = 0; i < last5mins.length; i++) {
			// System.out.println("Adding... " + last5mins[i][1]);
			count += last5mins[i][1];
		}
		double avg = count / last5mins.length;
		// System.out.println("average: " + avg);

		return avg;
	}

	public double getMemUsageAvg(String string) {
		double[][] last5mins = this.getMemUsage(string);
		// this.outputArray(last5mins);

		double count = 0.0;
		for (int i = 0; i < last5mins.length; i++) {
			// System.out.println("Adding... " + last5mins[i][1]);
			count += last5mins[i][1];
		}
		double avg = count / last5mins.length;

		double normalliser = 16568180;

		// Normalise results (put between 0 & 1)
		avg = avg / normalliser;

		// System.out.println("average: " + avg);
		return avg;
	}

	public double getHDDUsageAvg(String string) {
		double[][] last5mins = this.getHddUsage(string);
		// this.outputArray(last5mins);

		double count = 0.0;
		for (int i = 0; i < last5mins.length; i++) {
			// System.out.println("Adding... " + last5mins[i][1]);
			count += last5mins[i][1];
		}
		double avg = count / last5mins.length;

		double normalliser = 300000000.0;

		// Normalise results (put between 0 & 1)
		avg = avg / normalliser;

		// System.out.println("average: " + avg);
		return avg;
	}

	public double getNetUsageAvg(String string) {
		double[][] last5mins = this.getNetUsage(string);
		// this.outputArray(last5mins);

		double count = 0.0;
		for (int i = 0; i < last5mins.length; i++) {
			// System.out.println("Adding... " + last5mins[i][1]);
			count += last5mins[i][1];
		}
		double avg = count / last5mins.length;

		double normalliser = (1653452462 + 268581923) * 10.0;

		// Normalise results (put between 0 & 1)
		avg = avg / normalliser;

		// System.out.println("average: " + avg);
		return avg;
	}

	public boolean isActive(String server1) {

		String cmd = "SELECT util.time "
				+ "FROM `resource_monitoring`.`sys_info` AS sysInfo, "
				+ "`resource_monitoring`.`machine_util` AS util "
				+ "WHERE primary_ip = ?"
				+ "AND time >= DATE_SUB(NOW(), INTERVAL 10 SECOND) "
				+ "AND time <= NOW() "
				+ "AND sysInfo.machine_id = util.machine_id "
				+ "ORDER BY time DESC;";

		try {
			PreparedStatement stmt = this.conn.prepareStatement(cmd);
			stmt.setString(1, server1);
			// System.out.println(stmt.toString());

			// perform query
			ResultSet rs = stmt.executeQuery();
			// System.out.println("Counting resultset...");
			int count = 0;
			// Count resultset
			while (rs.next()) {
				count++;
			}
			if (count > 0)
				return true;
			else
				return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
