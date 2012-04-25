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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import DataTypes.EnergyMonitorData;
import DataTypes.MachineUtilisationData;

/**
 * Class to contain all info needed to run a single experiment
 * @author jws7
 *
 */
public class Experiment {

	public Experiment(String string) {
		this.command = string;
	}

	// Command to run
	public String command;
	
	// CPU Guv
	public String cpu_guv;
	
	//Only populted when display is needed then discarded (save memory)
	public ArrayList<MachineUtilisationData> resource_data;
	public ArrayList<EnergyMonitorData> energy_data;
	
	// Start & Finish times
	public Date start;
	public Date finish;
	
	public String benchmarkURL;
	
	// Output
	public StringBuffer output;

	// Machine executed on
	public String machine_id;
	
	public Experiment(String string) {
		this.command = string;
		output = new StringBuffer();
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss"); // 2010-08-04 13:51:14.539 //AKA SQL Format

	public String startTime() {
		// Return formated date
        return sdf.format(start);
	}

	public String finishTime() {
		// Return formated date
        return sdf.format(finish);
	}
}
