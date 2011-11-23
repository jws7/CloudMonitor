package ResourceMonitor;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import DataTypes.SysInfoData;


public class SysInfo extends MonitorBase {

	public SysInfoData stats() {
		System.out.println("Getting SysInfo stats");

		SysInfoData results = new SysInfoData();

		try {
			/*
			 * OS Info.
			 */
			OperatingSystem sys = OperatingSystem.getInstance();

			results.os_name = sys.getName();
			results.os_version = sys.getVersion();

			/*
			 * Network Info.
			 */
			NetInterfaceConfig config;

			config = this.sigar.getNetInterfaceConfig(null);
			org.hyperic.sigar.NetInfo netinfo = this.sigar.getNetInfo();
			results.machine_id = config.getHwaddr(); //gets Mac Address of primary.
			results.hostname = netinfo.getHostName();
			results.primary_ip = config.getAddress();
			results.default_gateway = netinfo.getDefaultGateway();

			/*
			 * CPU Info.
			 */
			org.hyperic.sigar.CpuInfo[] cpus = this.sigar.getCpuInfoList();
			org.hyperic.sigar.CpuInfo info = cpus[0];

			results.cpu_vendor = info.getVendor();
			results.cpu_model = info.getModel();
			results.num_cores = info.getTotalCores();
			results.num_cpus = info.getTotalSockets();
			results.cpu_mhz = info.getMhz();
			results.cpu_cache_size = info.getCacheSize();

			/*
			 * Mem Info.
			 */
			Mem mem = this.sigar.getMem();
			Swap swap = this.sigar.getSwap();

			results.memory_total = toKb(mem.getTotal());
			results.swap_total = toKb(swap.getTotal());

		} catch (SigarException e) {
		}
		System.out.println("Complete");
		return results;
	}

	public static void main(String[] args) throws Exception {
		SysInfo si = new SysInfo();
		System.out.println(si.stats());

	}
}
