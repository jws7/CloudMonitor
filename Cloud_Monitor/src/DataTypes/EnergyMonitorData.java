package DataTypes;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class EnergyMonitorData extends Data{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3738455160029911858L;

	public int activePower;

	public int voltage;

	public int wattHours;

	public float current;

	public int socket;
	
	public Date date;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss"); // 2010-08-04 13:51:14.539

	
	public EnergyMonitorData(){
		this.date = new Date();
	}
	
	@Override
	public String toString() {
		return "EnergyMonitorData [time=" + getDate() + 
				", socket=" + socket
				+ ", activePower=" + activePower + "W, voltage=" + voltage
				+ "V, current=" + current + "A, WattHours=" + wattHours
				+ "Wh or " + (float) wattHours / (float) 1000 + "kWh]";
	}

	public String getDate() {
		
        // Return formated date
        return sdf.format(this.date);
	}

}
