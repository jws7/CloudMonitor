package ResourceMonitor;

import org.hyperic.sigar.Sigar;

abstract class MonitorBase {

	protected Sigar sigar = new Sigar();

	protected static long toKb(long value) {
		return new Long(value / 1024);
	}
}