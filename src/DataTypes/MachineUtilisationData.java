package DataTypes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import DataTypes.MachineUtilisationData;

/*
 * Copyright (C) 2009-2011 School of Computer Science, University of St Andrews. All rights reserved.
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


public class MachineUtilisationData extends Data {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3195347427544179463L;

	public String machineID;
	
	/*
	 * CPU
	 */
	public double cpu_user_total; // Percentage total CPU utilisation
	public double cpu_sys_total; // Percentage system CPU utilisation
	public double cpu_idle_total; // Percentage CPU idle
	public double cpu_wait_total; // Percentage CPU in I/O wait state
	public double cpu_nice_total; // Percentage CPU runnining nice commands http://en.wikipedia.org/wiki/Nice_%28Unix%29

	/*
	 * Memory
	 */
	public long memory_used; // Memory used in system (in Kilobytes)
	public long memory_free; // Memory free in system (in Kilobytes)
	public long swap_used; // Swap space used in system (in Kilobytes)
	public long swap_free; // Swap space free in system (in Kilobytes)
	public List<FileSystemData> fs;
	public List<NetMonitorData> nets;

	// TODO - How do we find out the amount of swap activity??????

	public MachineUtilisationData(String machineID2) {
		this.machineID = machineID2;
	}

	@Override
	public String toString() {
		String str = "MachineUtilisationData [cpu_user_total=" + cpu_user_total + ", cpu_sys_total=" + cpu_sys_total + ", cpu_idle_total="
				+ cpu_idle_total + ", cpu_wait_total=" + cpu_wait_total + ", cpu_nice_total=" + cpu_nice_total + ", memory_used="
				+ memory_used + ", memory_free=" + memory_free + ", swap_used=" + swap_used + ", swap_free=" + swap_free + "]";
		
		
		for(FileSystemData f : fs)
			str += "\n" + f.toString();
		
		for(NetMonitorData n : nets)
			str += "\n" + n.toString();
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see h2o.resourcemonitor.result.Data#getMin(h2o.resourcemonitor.result.MachineUtilisationData)
	 */
	public MachineUtilisationData min(MachineUtilisationData other) {
		MachineUtilisationData min = new MachineUtilisationData(this.machineID);

		min.cpu_user_total = this.cpu_user_total < other.cpu_user_total ? this.cpu_user_total : other.cpu_user_total;
		min.cpu_sys_total = this.cpu_sys_total < other.cpu_sys_total ? this.cpu_sys_total : other.cpu_sys_total;
		min.cpu_idle_total = this.cpu_idle_total < other.cpu_idle_total ? this.cpu_idle_total : other.cpu_idle_total;
		min.cpu_wait_total = this.cpu_wait_total < other.cpu_wait_total ? this.cpu_wait_total : other.cpu_wait_total;
		min.cpu_nice_total = this.cpu_nice_total < other.cpu_nice_total ? this.cpu_nice_total : other.cpu_nice_total;

		min.memory_used = this.memory_used < other.memory_used ? this.memory_used : other.memory_used;
		min.memory_free = this.memory_free < other.memory_free ? this.memory_free : other.memory_free;
		min.swap_used = this.swap_used < other.swap_used ? this.swap_used : other.swap_used;
		min.swap_free = this.swap_free < other.swap_free ? this.swap_free : other.swap_free;

		return min;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see h2o.resourcemonitor.result.Data#getMax(h2o.resourcemonitor.result.MachineUtilisationData)
	 */
	public MachineUtilisationData max(MachineUtilisationData other) {
		MachineUtilisationData max = new MachineUtilisationData(this.machineID);

		max.cpu_user_total = this.cpu_user_total > other.cpu_user_total ? this.cpu_user_total : other.cpu_user_total;
		max.cpu_sys_total = this.cpu_sys_total > other.cpu_sys_total ? this.cpu_sys_total : other.cpu_sys_total;
		max.cpu_idle_total = this.cpu_idle_total > other.cpu_idle_total ? this.cpu_idle_total : other.cpu_idle_total;
		max.cpu_wait_total = this.cpu_wait_total > other.cpu_wait_total ? this.cpu_wait_total : other.cpu_wait_total;
		max.cpu_nice_total = this.cpu_nice_total > other.cpu_nice_total ? this.cpu_nice_total : other.cpu_nice_total;

		max.memory_used = this.memory_used > other.memory_used ? this.memory_used : other.memory_used;
		max.memory_free = this.memory_free > other.memory_free ? this.memory_free : other.memory_free;
		max.swap_used = this.swap_used > other.swap_used ? this.swap_used : other.swap_used;
		max.swap_free = this.swap_free > other.swap_free ? this.swap_free : other.swap_free;

		return max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see h2o.resourcemonitor.result.Data#getTotal(h2o.resourcemonitor.result.MachineUtilisationData)
	 */
	public MachineUtilisationData sum(MachineUtilisationData other) {
		MachineUtilisationData total = new MachineUtilisationData(this.machineID);

		total.cpu_user_total = this.cpu_user_total + other.cpu_user_total;
		total.cpu_sys_total = this.cpu_sys_total + other.cpu_sys_total;
		total.cpu_idle_total = this.cpu_idle_total + other.cpu_idle_total;
		total.cpu_wait_total = this.cpu_wait_total + other.cpu_wait_total;
		total.cpu_nice_total = this.cpu_nice_total + other.cpu_nice_total;

		total.memory_used = this.memory_used + other.memory_used;
		total.memory_free = this.memory_free + other.memory_free;
		total.swap_used = this.swap_used + other.swap_used;
		total.swap_free = this.swap_free + other.swap_free;

		return total;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see h2o.resourcemonitor.result.Data#convertToAverage(int)
	 */
	public MachineUtilisationData convertToAverage(int numberOfMeasurements) {
		MachineUtilisationData average = new MachineUtilisationData(this.machineID);

		average.cpu_user_total = this.cpu_user_total / numberOfMeasurements;
		average.cpu_sys_total = this.cpu_sys_total / numberOfMeasurements;
		average.cpu_idle_total = this.cpu_idle_total / numberOfMeasurements;
		average.cpu_wait_total = this.cpu_wait_total / numberOfMeasurements;
		average.cpu_nice_total = this.cpu_nice_total / numberOfMeasurements;

		average.memory_used = this.memory_used / numberOfMeasurements;
		average.memory_free = this.memory_free / numberOfMeasurements;
		average.swap_used = this.swap_used / numberOfMeasurements;
		average.swap_free = this.swap_free / numberOfMeasurements;

		return average;
	}

	public MachineUtilisationData adjustForBase(MachineUtilisationData other) {
		// nothing needs to be adjusted in this class.
		return this;
	}

	public boolean needsAdjustedForBase() {
		return false;
	}

	public MachineUtilisationData getNewInstance() {
		return new MachineUtilisationData(this.machineID);
	}

	public void addFileSystems(List<FileSystemData> allFsResults) {
		this.fs = allFsResults;
	}

	public void addNetworks(List<NetMonitorData> allNetResults) {
		this.nets = allNetResults;
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss"); // 2010-08-04 13:51:14.539

	public String getDate() {

			// current time.
	        Date now = new Date();
	        
	        // Return formated date
	        return sdf.format(now);
	}
	
	
}
