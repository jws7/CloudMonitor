package EnergyMonitor;

import DataTypes.Data;
import DataTypes.EnergyMonitorData;

/**
 * Class to read data from a Rairitain Power Distribution Unit
 * 
 *  - these systems use SNMP and can provide data as outlined below.
 *  
 * @author jws7
 *
 */
public class RairitainPDUMonitor implements DeviceMonitor {

	//TODO: Complete by extracting from EnergyMonitorPollerSSH
	
	private SNMPPoller poller;
	private int socket; // socket to query

	public RairitainPDUMonitor(String ip, int s){
		this.socket = s;

		// TODO: In future might want to download MIB if not in expected place
		this.poller = new SNMPPoller(ip, "./MIB.txt");
	}
	
	/**
	 * Main method for collecting data
	 * 
	 * -- Calls each data method inturn
	 * 
	 * @return
	 */
	public EnergyMonitorData getAllData() {
		
		try {
			// Comment out this line to do update only
			EnergyMonitorData data = new EnergyMonitorData();
			
			data.socket = this.socket;

			// Populate data
			int power = getActivePower();
			if (power > 1)
				data.activePower = power;

			int voltage = getVoltage();
			if (voltage > 1)
				data.voltage = voltage;

			float current = getCurrent();
			if (current > 0.1f)
				data.current = current;

			int wattHours = getWattHours();
			if (wattHours > 1)
				data.wattHours = wattHours;
			
			// And return to caller
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// ///////////////////
	// Query Methods
	public float getCurrent() {
		return (float) poller.snmpWalk("outletCurrent." + this.socket) / (float) 1000;
	}

	public int getWattHours() {
		return poller.snmpWalk("outletWattHours." + this.socket);
	}

	public int getVoltage() {
		return poller.snmpWalk("outletVoltage." + this.socket) / 1000;
	}

	public int getActivePower() {
		return poller.snmpWalk("outletActivePower." + this.socket);
	}

	public int getSocket() {
		return this.socket;
	}
}
