
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
package DataTypes;

public class NetMonitorData extends Data {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3675791503983861139L;
	public String device_name; // Name of the device
	public String ip_address; // IP address of the device
	public long rx_bytes; // Total bytes received by the interface
	public long rx_packets; // Total packets received by the interface
	public long rx_errors; // Total number of errors made whist receiving by the interface
	public long rx_dropped; // Total number of packets dropped whilst receiving by the interface
	public long rx_overruns; // Total number of overruns whilst receiving by the interface
	public long tx_bytes; // Total bytes transmitted by the interface
	public long tx_packets; // Total packets transmitted by the interface
	public long tx_errors; // Total number of errors made whist transmitting by the interface
	public long tx_dropped; // Total number of packets dropped whilst transmitting by the interface
	public long tx_overruns; // Total number of overruns whilst transmitting by the interface

	@Override
	public String toString() {
		return "Network Monitor Results [Device_name=" + device_name + ", " + "ip_address=" + ip_address + ", " + "rx_bytes=" + rx_bytes
				+ ", " + "rx_packets=" + rx_packets + ", " + "rx_errors=" + rx_errors + ", " + "rx_dropped=" + rx_dropped + ", "
				+ "rx_overruns=" + rx_overruns + ", " + "tx_bytes=" + tx_bytes + ", " + "tx_packets=" + tx_packets + ", " + "tx_errors="
				+ tx_errors + ", " + "tx_dropped=" + tx_dropped + ", " + "tx_overruns=" + tx_overruns + "]";
	}

	public NetMonitorData min(NetMonitorData other) {
		NetMonitorData min = new NetMonitorData();

		min.device_name = other.device_name;
		min.ip_address = other.ip_address;

		min.rx_bytes = this.rx_bytes < other.rx_bytes ? this.rx_bytes : other.rx_bytes;
		min.rx_packets = this.rx_packets < other.rx_packets ? this.rx_packets : other.rx_packets;
		min.rx_errors = this.rx_errors < other.rx_errors ? this.rx_errors : other.rx_errors;
		min.rx_dropped = this.rx_dropped < other.rx_dropped ? this.rx_dropped : other.rx_dropped;
		min.rx_overruns = this.rx_overruns < other.rx_overruns ? this.rx_overruns : other.rx_overruns;
		min.tx_bytes = this.tx_bytes < other.tx_bytes ? this.tx_bytes : other.tx_bytes;
		min.tx_packets = this.tx_packets < other.tx_packets ? this.tx_packets : other.tx_packets;
		min.tx_errors = this.tx_errors < other.tx_errors ? this.tx_errors : other.tx_errors;

		return min;
	}

	public NetMonitorData max(NetMonitorData other) {
		NetMonitorData max = new NetMonitorData();

		max.device_name = other.device_name;
		max.ip_address = other.ip_address;

		max.rx_bytes = this.rx_bytes > other.rx_bytes ? this.rx_bytes : other.rx_bytes;
		max.rx_packets = this.rx_packets > other.rx_packets ? this.rx_packets : other.rx_packets;
		max.rx_errors = this.rx_errors > other.rx_errors ? this.rx_errors : other.rx_errors;
		max.rx_dropped = this.rx_dropped > other.rx_dropped ? this.rx_dropped : other.rx_dropped;
		max.rx_overruns = this.rx_overruns > other.rx_overruns ? this.rx_overruns : other.rx_overruns;
		max.tx_bytes = this.tx_bytes > other.tx_bytes ? this.tx_bytes : other.tx_bytes;
		max.tx_packets = this.tx_packets > other.tx_packets ? this.tx_packets : other.tx_packets;
		max.tx_errors = this.tx_errors > other.tx_errors ? this.tx_errors : other.tx_errors;

		return max;
	}

	public NetMonitorData sum(NetMonitorData other) {
		NetMonitorData total = new NetMonitorData();

		total.device_name = other.device_name;
		total.ip_address = other.ip_address;

		total.rx_bytes = this.rx_bytes + other.rx_bytes;
		total.rx_packets = this.rx_packets + other.rx_packets;
		total.rx_errors = this.rx_errors + other.rx_errors;
		total.rx_dropped = this.rx_dropped + other.rx_dropped;
		total.rx_overruns = this.rx_overruns + other.rx_overruns;
		total.tx_bytes = this.tx_bytes + other.tx_bytes;
		total.tx_packets = this.tx_packets + other.tx_packets;
		total.tx_errors = this.tx_errors + other.tx_errors;

		return total;
	}

	public NetMonitorData convertToAverage(int numberOfMeasurements) {
		NetMonitorData average = new NetMonitorData();

		average.device_name = device_name;
		average.ip_address = ip_address;

		average.rx_bytes = this.rx_bytes / numberOfMeasurements;
		average.rx_packets = this.rx_packets / numberOfMeasurements;
		average.rx_errors = this.rx_errors / numberOfMeasurements;
		average.rx_dropped = this.rx_dropped / numberOfMeasurements;
		average.rx_overruns = this.rx_overruns / numberOfMeasurements;
		average.tx_bytes = this.tx_bytes / numberOfMeasurements;
		average.tx_packets = this.tx_packets / numberOfMeasurements;
		average.tx_errors = this.tx_errors / numberOfMeasurements;

		return average;
	}

	public NetMonitorData adjustForBase(NetMonitorData base) {
		NetMonitorData adjusted = new NetMonitorData();

		adjusted.device_name = device_name;
		adjusted.ip_address = ip_address;
		
		adjusted.rx_overruns = this.rx_overruns;
		
		adjusted.rx_bytes = this.rx_bytes - base.rx_bytes;
		adjusted.rx_packets = this.rx_packets - base.rx_packets;
		adjusted.rx_errors = this.rx_errors - base.rx_errors;
		adjusted.rx_dropped = this.rx_dropped - base.rx_dropped;

		adjusted.tx_bytes = this.tx_bytes - base.tx_bytes;
		adjusted.tx_packets = this.tx_packets - base.tx_packets;
		adjusted.tx_errors = this.tx_errors - base.tx_errors;
		
		return adjusted;
	}

	public NetMonitorData getNewInstance() {
		return new NetMonitorData();
	}

	public boolean needsAdjustedForBase() {
		return true;
	}
}

