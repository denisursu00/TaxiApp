package ro.taxiApp.docs.utils.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.StringTokenizer;

/**
 * Allows obtaining the MAC address attached to the network adapter.
 * For each OS, a derived class should be created. Currently, Linux and Windows
 * are supported.
 *
 */
public abstract class NetworkInfo {

	/**
	 * This function must be implemented in all derived classes in order
	 * to parse the result.
	 * @return The MAC address as string
	 * @throws ParseException
	 */
	public abstract String parseMacAddress() throws ParseException;

	/**
	 * Universal entry for retrieving MAC Address
	 * @return An array containing the address
	 * @throws IOException
	 */
	public final static byte[] getMacAddress() throws IOException {
		try {
			NetworkInfo info = getNetworkInfo();
			String macString = info.parseMacAddress();
			//put mac address in xx:xx:xx:xx:xx:xx format
			String delims=".-:";
			StringTokenizer tokens=new StringTokenizer(macString,delims);
			switch(tokens.countTokens()){
			//xx:xx:xx:xx:xx:xx or xx-xx-xx-xx-xx-xx format
			case 6:{
				macString=macString.replace('-', ':');
				break;
			}
			//xxxx.xxxx.xxxx format
			case 3:{
				String temp="";
				while(tokens.hasMoreTokens()){
					String token=tokens.nextToken();
					if (temp!="")
						temp+=':';
					temp+=token.substring(0, 2)+':'+token.substring(2,4);
				}
				macString=temp;
				break;
			}
			}
			return parseMacString(macString);
		} catch (ParseException ex) {
			throw new IOException();
		}
	}

	/**
	 * Runs the command for getting the MAC address
	 * @param command The command to be run
	 * @return The resulting string
	 * @throws IOException
	 */
	protected String runConsoleCommand(String command) throws IOException {
		Process p = Runtime.getRuntime().exec(command);
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1)
				break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();

		stdoutStream.close();

		return outputText;
	}

	/** Sort of like a factory... */
	private static NetworkInfo getNetworkInfo() throws IOException {
		String os = System.getProperty("os.name");

		if (os.startsWith("Windows")) {
			return new WindowsNetworkInfo();
		} else if (os.startsWith("Linux")) {
			return new LinuxNetworkInfo();
		} else {
			throw new IOException();
		}
	}

	/**
	 * Get the address of the local host
	 * @return the local host
	 * @throws ParseException
	 */
	protected String getLocalHost() throws ParseException {
		try {
			return java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	/**
	 * Parses the MAC address String in order to obtain an array of bytes
	 * @param addrStr the String address to be parsed
	 * @return the array of bytes containing the MAC address
	 */
	private static byte[] parseMacString(String addrStr) {
		byte[] addr = new byte[6];

		int len = addrStr.length();

		/*
		 * Ugh. Although the most logical format would be the 17-char one (12
		 * hex digits separated by colons), apparently leading zeroes can be
		 * omitted. Thus... Can't just check string length. :-/
		 */
		for (int i = 0, j = 0; j < 6; ++j) {
			if (i >= len) {
				// Is valid if this would have been the last byte:
				if (j == 5) {
					addr[5] = (byte) 0;
					break;
				}
				throw new NumberFormatException("Incomplete ethernet address (missing one or more digits");
			}

			char c = addrStr.charAt(i);
			++i;
			int value;

			// The whole number may be omitted (if it was zero):
			if (c == ':') {
				value = 0;
			} else {
				// No, got at least one digit?
				if (c >= '0' && c <= '9') {
					value = (c - '0');
				} else if (c >= 'a' && c <= 'f') {
					value = (c - 'a' + 10);
				} else if (c >= 'A' && c <= 'F') {
					value = (c - 'A' + 10);
				} else {
					throw new NumberFormatException("Non-hex character '" + c + "'");
				}

				// How about another one?
				if (i < len) {
					c = addrStr.charAt(i);
					++i;
					if (c != ':') {
						value = (value << 4);
						if (c >= '0' && c <= '9') {
							value |= (c - '0');
						} else if (c >= 'a' && c <= 'f') {
							value |= (c - 'a' + 10);
						} else if (c >= 'A' && c <= 'F') {
							value |= (c - 'A' + 10);
						} else {
							throw new NumberFormatException("Non-hex character '" + c + "'");
						}
					}
				}
			}

			addr[j] = (byte) value;

			if (c != ':') {
				if (i < len) {
					if (addrStr.charAt(i) != ':') {
						throw new NumberFormatException("Expected ':', got ('" + addrStr.charAt(i) + "')");
					}
					++i;
				} else if (j < 5) {
					throw new NumberFormatException("Incomplete ethernet address (missing one or more digits");
				}
			}
		}
		return addr;
	}
}