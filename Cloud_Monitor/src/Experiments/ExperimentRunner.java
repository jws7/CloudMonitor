package Experiments;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import DataTypes.EnergyMonitorData;
import DataTypes.EnergyMonitorDataSummary;
import Utilities.DatabaseConnection;

import uk.ac.standrews.cs.nds.util.Processes;
import uk.ac.standrews.cs.nds.util.SSH2ConnectionWrapper;

public class ExperimentRunner {

	private boolean debug;

	private SSH2ConnectionWrapper ssh;

	private Date now;

	private DatabaseConnection db;

	private String machine_id;

	private String fname;

	public double avg_watts;
	
	// Host SSH details
	private String host = "localhost";
	private String user = "snmp-user";
	private String password = "snmp-user-password";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList<Experiment> exps = new ArrayList<Experiment>();

		// Experiment gzip = new
		Experiment seven_zip = new Experiment("phoronix-test-suite batch-benchmark pts/compress-7zip");
		exps.add(seven_zip);
		Experiment gzip = new Experiment("phoronix-test-suite batch-benchmark pts/compress-gzip");
		exps.add(gzip);
		Experiment aio_stress = new Experiment("phoronix-test-suite batch-benchmark pts/aio-stress");
		exps.add(aio_stress);
		Experiment network_loop = new Experiment("phoronix-test-suite batch-benchmark pts/network-loopback");
		exps.add(network_loop);

		
		
		//Experiment ls = new Experiment("ls -la");
		//exps.add(ls);



		for (Experiment exp : exps) {
			//Parameters: experiment, number of runs, debug 
			new ExperimentRunner(exp, 1, false);
		}
		
		System.exit(0);
	}

	public ExperimentRunner(Experiment exp, int NUMBER_OF_RUNS, boolean d) {

		// Set up name of exp
		if(exp.command.contains("/"))
			this.fname = "results/" + exp.command.split("/")[1];
		else
			this.fname = "results/" + exp.command;

		// Set up print
		this.fname += ":" + getFname() + ".txt";

		System.out.println("FNAME: " + fname);

		this.debug = d;
		if (debug) {
			print("Debug on");
		}

		try {

			this.machine_id = "00:22:19:6D:07:B3";

			// Begin by getting a connection to this machine - hack
			InetAddress host = InetAddress.getByName(this.host);
			this.ssh = new SSH2ConnectionWrapper(host, this.user, this.password);

			this.db = new DatabaseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<EnergyMonitorDataSummary> data = new ArrayList<EnergyMonitorDataSummary>();

		// Execute experiment for number of runs
		for (int i = 0; i < NUMBER_OF_RUNS; i++) { // DO 10 TIMES
			executeCommand(exp);
			data.add(displayData(exp));
			
		}

		int count = 0;
		int watts = 0;
		for (EnergyMonitorDataSummary emds : data) {
			System.out.println(d);

			watts += emds.wattHoursUsed;
			count++;
		}

		this.avg_watts = (double) watts / (double) count;
		print("Avg watts used: " + avg_watts);

		
	}

	public int executeCommand(Experiment exp) {

		try {

			// Print command to run
			if (debug)
				print("[DEBUG] \"" + exp.command + "\"");

			// Create output stream to use
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// Note time
			exp.start = new Date();
			print("Start time: " + exp.startTime());

			// Execute command on remote host
			Process runProcess = Processes.runProcess(exp.command, this.ssh,
					out, System.err);

			// Wait for subprocess to finish... then we can read in the buffered
			// stream
			runProcess.waitFor();

			// Convert output stream to an input stream we can read (via
			// ByteArray)
			InputStream in = new ByteArrayInputStream(out.toByteArray());
			
			// Deal with result
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;

			while ((line = reader.readLine()) != null) {

				if (debug) {
					print("[DEBUG] Reading line...");
					System.out.println(line);
				}

				if (line.contains("openbenchmarking.org")) {
					String[] array = line.split(" ");

					String url = array[3].trim();
					exp.benchmarkURL = url;
					// System.out.println("URL: " + url);
				}
				
				if(exp.output == null)
					exp.output.append(line + "~"); //New line character
				else
					exp.output.append(line + "~");
			}

			runProcess.destroy(); // end process

			// Note time
			exp.finish = new Date();
			print("Finish time: " + exp.finishTime());

			// Print output
			print("Output =:" + exp.output);
			System.out.print(":");
			print(exp.benchmarkURL);
			this.db.insertExperimentInfo(exp);

			// Catch errors & tidy up
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Else signal error
		return -1;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss"); // 2010-08-04 13:51:14.539

	public String getDate() {

		// current time.
		now = new Date();

		// Return formated date
		return sdf.format(now);
	}

	private static SimpleDateFormat Fname = new SimpleDateFormat(
			"yyyy-MM-dd_HH-mm-ss"); // 2010-08-04_13:51:14.539 FileNameFormat

	public String getFname() {

		// current time.
		now = new Date();

		// Return formated date
		return Fname.format(now);
	}

	private void print(String s) {
		print(this.fname, s);
	}

	private static void print(String fname, String s) {
		String output = "[Experiment Runner] " + s + "\n";
		System.out.println(output);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fname, true));
			out.write(output);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private EnergyMonitorDataSummary displayData(Experiment exp) {

		EnergyMonitorDataSummary en_summary = new EnergyMonitorDataSummary();

		exp.resource_data = this.db.getResourceData(this.machine_id, exp.start,
				exp.finish);
		exp.energy_data = this.db.getEnergyData(this.machine_id, exp.start,
				exp.finish);

		// Loop through (display in table?)
		//for (MachineUtilisationData mu_data : exp.resource_data) {
			// System.out.println(mu_data);
		//}

		// Loop through (display in table?)
		for (EnergyMonitorData en_data : exp.energy_data) {
			// System.out.println(en_data);
			en_summary.add(en_data);
		}

		print(en_summary.toString());
		return en_summary;
	}

}
