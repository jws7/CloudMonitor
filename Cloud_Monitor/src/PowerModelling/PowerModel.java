package PowerModelling;
/*
 * Copyright (C) 2009-2010 School of Computer Science, University of St Andrews. All rights reserved.
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

import java.sql.ResultSet;

import Utilities.DatabaseConnection;

public class PowerModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PowerModel();
	}

	private DatabaseConnection db;

	public PowerModel() {
		this.db = new DatabaseConnection();

		ResultSet[] array = this.db.getPowerModelData("", "");

		ResultSet rs1 = array[0];
		ResultSet rs2 = array[1];

		try {

			// #########################################################
			// Get Data
			int rs1_size = 0;
			if (rs1 != null) {
				rs1.beforeFirst();
				rs1.last();
				rs1_size += rs1.getRow();
			}
			System.out.println("Rs1 has " + rs1_size + " results!");

			int rs2_size = 0;
			if (rs2 != null) {
				rs2.beforeFirst();
				rs2.last();
				rs2_size += rs2.getRow();
			}
			System.out.println("Rs2 has " + rs2_size + " results!");
			if (rs1_size != rs2_size) {
				System.err.println("Different result set sizes");
				System.exit(-1); // quit
			}

			double[][] matrix = new double[rs1_size + 1][8];

			// Reset iterators
			rs1.beforeFirst();
			rs2.beforeFirst();

			int count = 0;
			// Iterate through ResultSets
			while (rs1.next() && rs2.next()) {

				double power = rs1.getDouble("activePower");

				double cpu_user = rs2.getDouble("cpu_user_total");
				double cpu_sys = rs2.getDouble("cpu_sys_total");
				double cpu_usage = (cpu_user + cpu_sys) * 10;
				double memory_used = rs2.getDouble("memory_used");

				System.out.println("Data: [" + power + ", " + cpu_usage + ", " + memory_used + "]");

				double Y = power;

				double X1 = cpu_usage;
				double X2 = memory_used;
				
				// Dependent Variable
				matrix[count][0] = Y;

				// Two independent variables
				matrix[count][1] = X1;
				matrix[count][2] = X2;

				// X1*Y
				matrix[count][3] = X1 * Y;
				
				// X2*Y
				matrix[count][4] = X2 * Y;
				
				// X1*X2
				matrix[count++][5] = X1 * X2;

			}

			
			//Calculate sums
			double sumOfY = 0.0;
			double sumOfX1 = 0.0;
			double sumOfX2 = 0.0;
			double sumOfX1Y = 0.0;
			double sumOfX2Y = 0.0;
			double sumOfX1X2 = 0.0;
			double sumOfX1_2 = 0.0;
			double sumOfX2_2 = 0.0;
			for (int i = 0; i < count; i++) {
				sumOfY += matrix[i][0];
				sumOfX1 += matrix[i][1];
				sumOfX2 += matrix[i][2];
				sumOfX1Y += matrix[i][3];
				sumOfX2Y += matrix[i][4];
				sumOfX1X2 += matrix[i][5];
				sumOfX1_2 += (matrix[i][1] * matrix[i][1]);
				sumOfX2_2 += (matrix[i][2] * matrix[i][2]);
				
			}

			// Calculate weights for independent variables
			double b1 = ((sumOfX2_2 * sumOfX1Y) - (sumOfX1X2 * sumOfX2Y)) / ( (sumOfX1_2 * sumOfX2_2) - (sumOfX1X2 * sumOfX1X2) );
			double b2 = ((sumOfX1_2 * sumOfX2Y) - (sumOfX1X2 * sumOfX1Y)) / ( (sumOfX1_2 * sumOfX2_2) - (sumOfX1X2 * sumOfX1X2) );
			
			// Calculate integral
			double a = matrix[count - 1][0] - (b1 * matrix[count - 1][1]) - (b2 * matrix[count - 1][2]);
			
			// Print out equation deatils
			System.out.println("Regression: [a = " + a + ", b1 = " + b1 + ", b2 = " + b2);

			// Test error rate
			double totalError = 0.0;
			for (int i = 0; i < count; i++) {

				 double Yprime = a + (b1 * matrix[i][1]) + (b2 * matrix[i][2]);
				 
				 double error = Yprime - matrix[i][0];
				 System.out.println("Yprime = " + Yprime + ", Y = " + matrix[i][0] + ", error: " + error);
				 
				 totalError += error;
			}
			double avg_error = totalError / count;
			System.out.println("Avg Error rate: [" + avg_error + "]");
			
			//double newX = 0.08;
			//double predictedY = a + (b1 * newX) + avg_error;
			
			//System.out.println("Given X: " + newX + ", Y would be: " + predictedY);
						
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
