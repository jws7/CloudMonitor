package DataTypes;

import java.io.Serializable;
import java.util.Date;

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
