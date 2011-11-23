/**
 * 
 * Modified from code written by Angus Macdonald for the H20 Project, University of St Andrews.
 * 
 * Edits by James Smith
 * 
 */
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

package ResourceMonitor;

import DataTypes.FileSystemData;
import DataTypes.MachineUtilisationData;
import DataTypes.NetMonitorData;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;


import Utilities.DatabaseConnection;

import java.util.LinkedList;
import java.util.List;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;

public class MachineUtilisationMonitor extends MonitorBase implements Runnable {
	
	private DatabaseConnection db;			 // Database Connection
	MachineUtilisationData mu_data;
	private String machineID;
	
	public MachineUtilisationMonitor(){
		//Set up DB
		this.db = new DatabaseConnection();
		System.out.println(db.toString());
	}

	public MachineUtilisationMonitor(String machineID) {
		this();
		this.machineID = machineID;
		
	}

	@Override
	public void run() {
		System.out.println("[DEBUG]: Starting MACHINE UTIL MONITOR");
		while (true) {
			
			long start = System.currentTimeMillis();
			
			mu_data = getInfo();
			System.out.println(mu_data);
			this.db.insertMachineUtilData(mu_data);
			
			long finish = System.currentTimeMillis();
			
			long diff = finish - start;
			
			long sleep_t = 3000 - diff; // new record every 3 secs
			
			if(sleep_t < 0)
				sleep_t = 0;
			
			// Now sleep for 3 seconds
			try {
				Thread.sleep(sleep_t);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * Get the current state of the physical machine at this moment
	 * 
	 * @return A MachineUtilisationData object containing the current state of
	 *         the machine
	 * 
	 */
	public MachineUtilisationData getInfo() {

		MachineUtilisationData results = new MachineUtilisationData(machineID);

		try {
			/*
			 * CPU
			 */
			CpuPerc cpu = this.sigar.getCpuPerc();

			results.cpu_user_total = cpu.getUser();
			results.cpu_sys_total = cpu.getSys();
			results.cpu_idle_total = cpu.getIdle();
			results.cpu_wait_total = cpu.getWait();
			results.cpu_nice_total = cpu.getNice();

			/*
			 * Memory
			 */
			Mem mem = this.sigar.getMem();
			Swap swap = this.sigar.getSwap();

			results.memory_used = toKb(mem.getUsed());
			results.memory_free = toKb(mem.getFree());
			results.swap_used = toKb(swap.getUsed());
			results.swap_free = toKb(swap.getFree());

			// Hard Disk and Network are currently handled by seperate objects

			/*
			 * Hard Disks
			 */
		
			List<FileSystemData> allFsResults = new LinkedList<FileSystemData>();

			try {
				FileSystem[] fslist = sigar.getFileSystemList();
				
				System.out.println("Got file system list...");

				for (FileSystem element : fslist) {
					System.out.println("Reporting:" + element.getDevName());
					FileSystemData singleFsResult = reportFileSystem(element);
					System.out.println(singleFsResult);
					if (singleFsResult != null)
						allFsResults.add(singleFsResult);
				}
			} catch (SigarException e) {
			}
			System.out.println("FileSystemSize:" + allFsResults.size());
			results.addFileSystems(allFsResults);

			/*
			 * Network
			 */
			List<NetMonitorData> allNetResults = new LinkedList<NetMonitorData>();

			try {
				String[] names = sigar.getNetInterfaceList();

				for (int i = 0; i < names.length; i++) {
					NetMonitorData singleNetResult = reportNetworkInterface(names[i]);
					if (!singleNetResult.ip_address.equals("0.0.0.0"))
						allNetResults.add(singleNetResult);
				}
			} catch (SigarException e) {
			}
			results.addNetworks(allNetResults);

		} catch (Exception e) {
			e.printStackTrace(); // Better than ignoring the exception?
		}

		return results;
	}

	public FileSystemData reportFileSystem(FileSystem fs) {
		FileSystemData result = new FileSystemData();

		try {
			if (fs.getType() == org.hyperic.sigar.FileSystem.TYPE_LOCAL_DISK) {

				result.fileSystemLocation = fs.getDevName();
				result.fileSystemType = fs.getSysTypeName();
				result.fileSystemName = fs.getDirName();

				FileSystemUsage usage = this.sigar
						.getFileSystemUsage(result.fileSystemName);

				result.fs_used = usage.getTotal() - usage.getFree();
				result.fs_free = usage.getAvail();
				result.fs_size = usage.getTotal();
				result.fs_files = usage.getFiles();
				result.fs_disk_reads = usage.getDiskReads();
				result.fs_disk_read_bytes = usage.getDiskReadBytes();
				result.fs_disk_writes = usage.getDiskWrites();
				result.fs_disk_write_bytes = usage.getDiskWriteBytes();

				usage = sigar.getFileSystemUsage(result.fileSystemLocation);

			} else {
				result = null;
			}
		} catch (SigarException e) {
		}

		return result;
	}

	/**
	 * @param device_name
	 *            if passed null will access the primary network device
	 * @return the NetMonitorData for the specified device
	 */
	public NetMonitorData reportNetworkInterface(String device_name) {

		NetMonitorData results = new NetMonitorData();

		try {
			sigar = new Sigar();
			NetInterfaceConfig ifconfig = sigar
					.getNetInterfaceConfig(device_name);
			NetInterfaceStat ifstat = this.sigar.getNetInterfaceStat(ifconfig
					.getName());
			results.device_name = ifconfig.getName();
			results.ip_address = ifconfig.getAddress();

			results.rx_bytes = ifstat.getRxBytes();
			results.rx_packets = ifstat.getRxPackets();
			results.rx_errors = ifstat.getRxErrors();
			results.rx_dropped = ifstat.getRxDropped();
			results.rx_overruns = ifstat.getRxOverruns();

			results.tx_bytes = ifstat.getTxBytes();
			results.tx_packets = ifstat.getTxPackets();
			results.tx_errors = ifstat.getTxErrors();
			results.tx_dropped = ifstat.getTxDropped();
			results.tx_overruns = ifstat.getTxOverruns();
		} catch (SigarException e) {
		}

		return results;
	}

	public static void main(String[] args) throws Exception {
		MachineUtilisationMonitor mu = new MachineUtilisationMonitor();
		new Thread(mu).start();
	}
}
