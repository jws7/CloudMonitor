package DataTypes;

import java.io.Serializable;
import java.util.Date;

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


public abstract class Data implements Serializable {

	private static final long serialVersionUID = -6235540795909992564L;
	public Date time;

	public Data() {
		time = new Date();
	}

	public Date getTime() {
		return time;
	}
	
	
}
