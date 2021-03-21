package ro.taxiApp.docs.utils.net;

import java.io.IOException;
import java.text.ParseException;

public class LinuxNetworkInfo extends NetworkInfo {
	public static final String IPCONFIG_COMMAND = "ifconfig";

	public String parseMacAddress() throws ParseException {
		String ipConfigResponse = null;
		try {
			ipConfigResponse = runConsoleCommand(IPCONFIG_COMMAND);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage(), 0);
		}
		String localHost = getLocalHost();

		java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			boolean containsLocalHost = line.indexOf(localHost) >= 0;

			// see if line contains IP address
			if (containsLocalHost && lastMacAddress != null) {
				return lastMacAddress;
			}

			// see if line contains MAC address
			int macAddressPosition = line.indexOf("HWaddr");
			if (macAddressPosition <= 0) {
				continue;
			}

			String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
			if (isMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				continue;
			}
		}

		ParseException ex = new ParseException("cannot read MAC address for " + localHost + " from [" + ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}

	private final boolean isMacAddress(String macAddressCandidate) {
		if (macAddressCandidate.length() != 17)
			return false;
		return true;
	}
}
