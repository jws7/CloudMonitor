package EnergyMonitor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Scanner;

import uk.ac.standrews.cs.nds.util.Processes;
import uk.ac.standrews.cs.nds.util.SSH2ConnectionWrapper;

/**
 * SNMPPoller classes execute SNMP on a host as defined in the
 * 
 * ../config/snmphost.txt configuration file
 * 
 * @author jws7
 * 
 */
public class SNMPPoller {

	private String deviceIPAddress;

	private String mibPath;

	private SSH2ConnectionWrapper ssh;

	public SNMPPoller() {

		String host = "";
		String user = "";
		String passwd = "";

		// Open config file
		try {
			File config = new File("config" + File.separator + "snmphost.txt");
			Scanner sc = new Scanner(config);

			host += sc.next();
			user = sc.next();
			passwd = sc.next();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		// Begin by getting a connection to snmp poller machine
		try {
			InetAddress address = InetAddress.getByName(host);
			this.ssh = new SSH2ConnectionWrapper(address, user, passwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int snmpWalk(String dataRequested) {

		// Set up get active power snmp command
		String cmd = "snmpwalk -v1 -c public -m " + this.mibPath + " "
				+ this.deviceIPAddress + " " + dataRequested;

		// append "." + socket number to query to snmp data outlet requested

		// Execute snmp command
		return executeCommand(cmd);
	}

	/**
	 * Method setup to execute a snmp command, parse the integer response and
	 * return it.
	 * 
	 * @param cmd
	 * @return
	 */
	private int executeCommand(String cmd) {

		try {

			// Create output stream to use
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// Execute command on remote host
			long start = System.currentTimeMillis();
			Process runProcess = Processes.runProcess(cmd, this.ssh, out,
					System.err);

			// Wait for subprocess to finish... then we can read in the buffered
			// stream
			runProcess.waitFor();

			// Convert output stream to an input stream we can read (via
			// ByteArray)
			InputStream in = new ByteArrayInputStream(out.toByteArray());

			// Deal with result
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = null;

			while ((line = reader.readLine()) != null) {

				// Skip if empty string
				if (line.equals(""))
					continue;

				// Now get number (last string on a space split line).
				line = line.trim();
				String[] columns = line.split(" ");
				String number = columns[columns.length - 1]; // get last column

				try {
					// Attempt to parse it
					int value = Integer.parseInt(number);

					// If successful
					if (value > 0) {
						// end process
						runProcess.destroy();

						// Return parsed value to caller
						return value;
					}

					// Tidy up
				} catch (Exception e) {
					System.err.print("Couldn't parse number");
				}
			}

			// If no result
			runProcess.destroy(); // end process
			// Catch errors & tidy up
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Else signal error
		return -1;
	}

}
