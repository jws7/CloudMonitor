package DataTypes;

public class EnergyMonitorDataSummary {

	// Totals
	public int activePowerTotal;
	public int voltage;
	public int wattHoursTotal;
	public float currentTotal;
	private int totalMeasurements;

	// Avgs
	public int activePowerAvg;
	public float currentAvg;

	private int startWattHours;
	private boolean flag = false;
	public int wattHoursUsed;
	
	public EnergyMonitorDataSummary(){
		this.flag = true;
	}
	
	
	public void add(EnergyMonitorData e) {

		// Update totals
		this.totalMeasurements++;
		this.activePowerTotal += e.activePower;
		this.currentTotal += e.current;
		this.voltage = e.voltage;

		// Recalculate averages
		recalculateAvgs();

		// DEAL with watthours
		if (flag){
			this.startWattHours = e.wattHours;
			this.flag = false;
		}
		
		this.wattHoursTotal = e.wattHours;
		recalculateWattHrs();
	}

	private void recalculateAvgs() {
		this.activePowerAvg = this.activePowerTotal / this.totalMeasurements;
		this.currentAvg = this.currentTotal / this.totalMeasurements;
	}
	
	private void recalculateWattHrs() {
		this.wattHoursUsed = this.wattHoursTotal - this.startWattHours;
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
