package DataTypes;
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

public class EnergyMonitorDataSummary {

	// Totals
	public int activePowerTotal;
	public int voltage;
	public int currentWattHours;
	public float currentTotal;
	private int totalMeasurements;

	// Avgs
	public int activePowerAvg;
	public float currentAvg;

	private int startWattHours = 0; // initalise to zero
	public int wattHoursUsed;
		
	public void add(EnergyMonitorData e) {

		// Update totals
		this.totalMeasurements++;
		this.activePowerTotal += e.activePower;
		this.currentTotal += e.current;
		this.voltage = e.voltage;

		// Recalculate averages
		recalculateAvgs();

		// If we have not previously set WattHours
		if(this.startWattHours == 0){
			// Set it
			this.startWattHours = e.wattHours;
		}
		
		// Update current watthours
		this.currentWattHours = e.wattHours;
		recalculateWattHrs(); // workout wattHours used
	}

	private void recalculateAvgs() {
		this.activePowerAvg = this.activePowerTotal / this.totalMeasurements;
		this.currentAvg = this.currentTotal / this.totalMeasurements;
	}
	
	private void recalculateWattHrs() {
		this.wattHoursUsed = this.currentWattHours - this.startWattHours;
	}

	@Override
	public String toString() {
		return "EnergyMonitorDataSummary [activePowerAvg="
				+ this.activePowerAvg + "W, voltage=" + this.voltage
				+ "V, current=" + this.currentAvg + "A, WattHours="
				+ wattHoursUsed + "Wh or " + (float) wattHoursUsed
				/ (float) 1000 + "kWh]";
	}
}
