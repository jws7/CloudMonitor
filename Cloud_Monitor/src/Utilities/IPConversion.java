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
package Utilities;

import java.net.InetAddress;

public class IPConversion {

	public static String convertLongToIP(long ip) {

		// Conversion code from http://mindprod.com/jgloss/ip.html

		StringBuffer sb = new StringBuffer(15);
		for (int shift = 24; shift > 0; shift -= 8) {
			// process 3 bytes, from high order byte down.
			sb.append(Long.toString((ip >>> shift) & 0xff));
			sb.append('.');
		}
		sb.append(Long.toString(ip & 0xff));

		String address = sb.toString();

		return address;
	}

	public static long convertIPtolong(String id) {

		// Split array longo
		String[] array = id.split("\\.");

		// Check format
		if (array.length != 4) {
			System.err.println("Incorrect format. You supplied " + id
					+ " which gave an array of " + array.length);
			return -1;
		}

		// extract longegers
		long first = Long.parseLong(array[0]);
		long second = Long.parseLong(array[1]);
		long third = Long.parseLong(array[2]);
		long fourth = Long.parseLong(array[3]);

		long octet1 = 256 * 256 * 256;
		long octet2 = 256 * 256;
		long octet3 = 256;
		long octet4 = 1;

		// perform calculation
//		System.out.println("Calculation: (" + first + " * " + octet1 + ") + ("
//				+ second + " * " + octet2 + ") + (" + third + " * " + octet3
//				+ ") + (" + fourth + " * " + octet4 + ")");

		long res1 = first * octet1;
		long res2 = second * octet2;
		long res3 = third * octet3;
		long res4 = fourth * octet4;

		// Compute final result
		long result = (long) res1 + res2 + res3 + res4;

		// Return result
		return result;
	}

	// Test
	public static void main(String args[]) {
		
		// Setup
		String ip = "138.251.198.2";
		System.out.println("IP address as string: " + ip);
		
		// Convert to long
		long l = convertIPtolong(ip);
		System.out.println("IP address as long: " + l);

		// and back...
		String str = convertLongToIP(l);
		System.out.println("BackToString: " + str);
	}
}
