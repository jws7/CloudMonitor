package PowerModelling;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import Utilities.DatabaseConnection;

import flanagan.analysis.Regression;

public class GenerateModel {

	public static void main(String[] args) {

		DatabaseConnection db = new DatabaseConnection(false);

		long reg_start = System.currentTimeMillis();

		String machine_id = args[1];
		
		
		// SetTimestamps
		String start_t = args[2];
		String end_t = args[3];

		println("Generating model for start_t: " + start_t + " to: " + end_t + " on machine: " + machine_id);

		// Get ALL DATA
		ResultSet[] array = db.getPowerModelData(start_t, end_t, machine_id);

		// Split up result sets
		ResultSet rs1 = array[0];
		ResultSet rs2 = array[1];
		ResultSet rs3 = array[2];
		ResultSet rs4 = array[3];

		try {

			// #########################################################
			// Calculate RS Sizes
			int rs1_size = 0;
			if (rs1 != null) {
				rs1.beforeFirst();
				rs1.last();
				rs1_size += rs1.getRow();
			}

			int rs2_size = 0;
			if (rs2 != null) {
				rs2.beforeFirst();
				rs2.last();
				rs2_size += rs2.getRow();
			}

			int rs3_size = 0;
			if (rs3 != null) {
				rs3.beforeFirst();
				rs3.last();
				rs3_size += rs3.getRow();
			}

			int rs4_size = 0;
			if (rs4 != null) {
				rs4.beforeFirst();
				rs4.last();
				rs4_size += rs4.getRow();
			}

			println("Rs1 has " + rs1_size + " results!");
			println("Rs2 has " + rs2_size + " results!");
			println("Rs3 has " + rs3_size + " results!");
			println("Rs4 has " + rs4_size + " results!");
			if (rs1_size != rs2_size) {
				System.err.println("Different result set sizes");
				// System.exit(-1); // quit
			}
			// #########################################################

			// Matrix to hold extracted data
			double[][] matrix = new double[5][rs1_size + 1];

			// Reset iterators
			rs1.beforeFirst();
			rs2.beforeFirst();
			rs3.beforeFirst();
			rs4.beforeFirst();

			int count = 0;

			// Prep hdd base values
			rs3.next();
			double read_count = rs3.getDouble("fs_disk_reads");
			double write_count = rs3.getDouble("fs_disk_writes");

			// Prep net base values
			rs4.next();
			double rx_count = rs4.getDouble("rx_bytes");
			double tx_count = rs4.getDouble("tx_bytes");

			// Iterate through ResultSets
			while (rs1.next() && rs2.next() && rs3.next() && rs4.next()) {

				// Power
				double power = rs1.getDouble("activePower");

				// CPU
				double cpu_user = rs2.getDouble("cpu_user");
				double cpu_sys = rs2.getDouble("cpu_sys");
				double cpu_usage = (cpu_user + cpu_sys);

				// MEM
				double memory_used = rs2.getDouble("memory_used");

				// HDD
				double new_read_count = rs3.getDouble("fs_disk_reads");
				double new_write_count = rs3.getDouble("fs_disk_writes");
				double read_ops = new_read_count - read_count;
				double write_ops = new_write_count - write_count;
				read_count = new_read_count;
				write_count = new_write_count;
				double hdd_ops = read_ops + write_ops;

				// NET
				double new_rx_count = rs4.getDouble("rx_bytes");
				double new_tx_count = rs4.getDouble("tx_bytes");
				double rx_ops = new_rx_count - rx_count;
				double tx_ops = new_tx_count - tx_count;
				rx_count = new_rx_count;
				tx_count = new_tx_count;
				double net_ops = rx_ops + tx_ops;

				println("Data: [" + power + ", " + cpu_usage + ", "
						+ memory_used + ", " + hdd_ops + ", " + net_ops + "]");

				// Dependent Variables
				double Y = power;

				// Independent Variables
				double X1 = cpu_usage;
				double X2 = memory_used;
				double X3 = hdd_ops;
				double X4 = net_ops;

				// Dependent Variable
				matrix[0][count] = Y;

				// Four independent variables
				matrix[1][count] = X1;
				matrix[2][count] = X2;
				matrix[3][count] = X3;
				matrix[4][count] = X4;
				count++;
				
			
			}

			// Copy independent variable values to seperate matrix
			double[][] x_data = new double[4][rs1_size + 1];
			System.arraycopy(matrix[1], 0, x_data[0], 0, matrix[1].length);
			System.arraycopy(matrix[2], 0, x_data[1], 0, matrix[2].length);
			System.arraycopy(matrix[3], 0, x_data[2], 0, matrix[3].length);
			System.arraycopy(matrix[4], 0, x_data[3], 0, matrix[4].length);

			// Initialise regression library
			Regression reg = new Regression();
			
			// input data as x data matrix & y data array
			reg.enterData(x_data, matrix[0]);

			// #########################################################
			// Confirm data is in correct format
			print("X1 data: [");
			for (double x : reg.getXdata()[0]) {
				print(x + ", ");
			}
			print("]\n");

			print("X2 data: [");
			for (double x : reg.getXdata()[1]) {
				print(x + ", ");
			}
			print("]\n");

			print("X3 data: [");
			for (double x : reg.getXdata()[2]) {
				print(x + ", ");
			}
			print("]\n");

			print("X4 data: [");
			for (double x : reg.getXdata()[3]) {
				print(x + ", ");
			}
			print("]\n");

			print("Y data: [");
			for (double y : reg.getYdata()) {
				print(y + ", ");
			}
			print("]\n");
			// #########################################################

			// #########################################################
			// PREFROM LINEAR REGRESSION
			reg.linear();
			// #########################################################

			// Print results
			double[] weights = reg.getBestEstimates();
			print("Testing... weights: [");
			for (double d : weights) {
				print(d + ", ");
			}
			int length = weights.length;

			// Print power model...
			print("\nPower model: y = ");
			for (int i = 0; i < length; i++) {
				print("" + weights[i]);
				if (i > 0)
					print("x" + i);
				if (i < length - 1)
					print(" + ");
			}

			// and with CPU instead of X1, etc...
			println("...So Power Model: y = " + weights[0] + " + " + weights[1]
					+ "*CPU + " + weights[2] + "*MEMORY + " + weights[3]
					+ "*HDD + " + weights[4] + "*NET");

			// #########################################################
			// Get alpha weight
			double a = weights[0];

			// Get beta weights
			double[] b = new double[weights.length - 1];
			System.arraycopy(weights, 1, b, 0, weights.length - 1);
			// #########################################################

			println("Using power model....");

			// Prepare to calculate errors
			int bigErrors = 0;
			double totalError = 0.0;

			// Create predictions array
			double[] predictions = new double[matrix[0].length];

			// Loop through actual power values
			for (int i = 0; i < reg.getYdata().length; i++) {

				// Extract actual
				double actual = reg.getYdata()[i];

				// if greater than zero, proceed
				if (actual > 0.0) {

					// Begin to built up predicted value
					double predicted = a; // add alpha weight

					// ...and all independent variables multiplied by their
					// coefficients (beta weights)
					for (int j = 0; j < b.length; j++) {
						double xk = b[j] * matrix[j + 1][i];
						predicted += xk;
					}

					// Calculate error percentage
					double error = ((predicted - actual) / actual) * 100;

					// Load predictions array
					predictions[i] = predicted;

					// Print details
					println("Predicted: " + predicted + ", actual: " + actual
							+ ", error: " + error);

					// Note error of greater than 5%
					if (error > 5.0 || error < -5.0) {
						// System.err.println("Outside 5%");
						bigErrors++;
					}
					if (error < 0)
						error = error * -1;
					totalError += error; // prepare to calculate avg error
				}
			}

			// Calculate actual error
			double avgError = totalError / reg.getYdata().length;

			// Not reg analysis time
			long reg_end = System.currentTimeMillis();
			long diff = reg_end - reg_start;
			println("Regression Analysis took: " + diff + "ms");
			println("Number of measurements: " + reg.getYdata().length);

			System.out.println("Regression Analysis took: " + diff + "ms for "
					+ reg.getYdata().length + " measurements");

			// Put predictions against actual & error into Database
			//db.insertPredictionData(predictions, rs1);

			// Note database update time
			long db_end = System.currentTimeMillis();
			long db_time = db_end - reg_end;
			println("Database Update took: " + db_time + "ms");

			
			// Print errors & model (recap)
			println("Number of bigErrors: " + bigErrors);
			println("AvgError: " + avgError);

			println("...So Power Model: y = " + weights[0] + " + " + weights[1]
					+ "*CPU + " + weights[2] + "*MEMORY + " + weights[3]
					+ "*HDD + " + weights[4] + "*NET");
			
			println("Coefficient of Determination: ");
			double coeff = reg.getCoefficientOfDetermination();
			println(coeff + "\n");
			
			reg.getPvalues();
			
			reg.print("test.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Remember to print to console & file
		output();
	}

	/**
	 * Little helper method to ouput to console & file
	 */
	private static void output() {
		try {
			SimpleDateFormat Fname = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"); // 2010-08-04_13:51:14.539
																					// FileNameFormat
			// current time.
			Date now = new Date();

			// Return formated date
			String time = Fname.format(now);
			// Create file
			FileWriter fstream = new FileWriter("results/regression/" + time
					+ ".txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(outputStr);
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		}

		System.out.print(outputStr);
	}

	// Ouput helper field & functions
	private static String outputStr = "";

	private static void print(String s) {
		outputStr += s;
	}

	private static void println(String s) {
		outputStr += s + "\n";
	}

}
