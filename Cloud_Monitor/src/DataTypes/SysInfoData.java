/*
 * Copyright (C) 2009-2010 School of Computer Science, University of St Andrews. All rights reserved.
 * Project Homepage: http://blogs.cs.st-andrews.ac.uk/h2o
 *
 * H2O is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * H2O is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with H2O.  If not, see <http://www.gnu.org/licenses/>.
 */
package DataTypes;


public class SysInfoData extends Data {

	/**
	 * 
	 */
	private static final long serialVersionUID = -381563930935978125L;

	public String machine_id; // MAC address of the primary interface.

	/*
	 * OS Info.
	 */
	public String os_name; // The name of the OS e.g. MacOSX
	public String os_version; // The version of the OS e.g 10.6.4

	/*
	 * Network Info.
	 */

	public String hostname; // Fully qualified hostname of machine e.g. slash.cs.st-andrews.ac.uk
	public String primary_ip; // The primary IP address of the machine
	public String default_gateway; // The default IP gateway used by the machine

	/*
	 * CPU Info.
	 */
	public String cpu_vendor; // The cpu vendor e.g. Intel
	public String cpu_model; // The model of the machine e.g. MacBookPro5,5
	public int num_cores; // The number of processor cores available
	public int num_cpus; // The number of CPUs available
	public int cpu_mhz; // Top CPU speed in MHz
	public long cpu_cache_size; // The CPU cache size (in Kilobytes)

	/*
	 * Memory Info.
	 */
	public long memory_total; // Total available memory (in Kilobytes)
	public long swap_total; // Total available swap (in Kilobytes)

	@Override
	public String toString() {
		return "SysInfoResults [os_name=" + os_name + ", os_version=" + os_version + ", hostname=" + hostname + ", primaryIP=" + primary_ip
				+ ", default_gateway=" + default_gateway + ", cpuVendor=" + cpu_vendor + ", cpu_model=" + cpu_model + ", num_cores="
				+ num_cores + ", num_cpus=" + num_cpus + ", cpu_mhz=" + cpu_mhz + ", cpu_cache_size=" + cpu_cache_size + "]";
	}
}
