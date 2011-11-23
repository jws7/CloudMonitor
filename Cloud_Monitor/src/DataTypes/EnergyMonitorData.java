package DataTypes;

import java.text.SimpleDateFormat;
import java.util.Date;


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
