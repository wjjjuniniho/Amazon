package amazon.com.interview;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	/*
	 * assumption: 
	 * the log file "access_log" is under the same dir of the application
	 * log format is apache combined log format
	 * the server can create as much logs as possible with 1000 different server ip addresses in "access_log"
	 * 
	 * Solution:
	 * using a combination of hash map and min heap to solve the problem
	 * 
	 */
	// log file name
	private static final String LOG = "access_log";
	// apache combined format
	private static final String LOG_PATTERN = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
	
	public static void main(String[] args) {
		// for record all eligible ips
		ArrayList<String> ips = new ArrayList<String>();
		String line;		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		// get ts an hr ago
		Date anHourAgo = calendar.getTime();
		// for parse purpose
		SimpleDateFormat sd = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
		
		Solution solution = new Solution();
		
		try {
			// read and store the ips in the past hour
			Scanner scanner = new Scanner(new File(LOG));
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				// regex match
				Pattern p = Pattern.compile(LOG_PATTERN);
				Matcher matcher = p.matcher(line);
				if (!matcher.matches() || matcher.groupCount() != 9) {
					System.err.println("Pattern not matched");
					return;
				}
				// get ip and date
				String ip = matcher.group(1);
				Date date = sd.parse(matcher.group(4));
				// check if the date is in the past hour
				if (date.after(anHourAgo)) {
					ips.add(ip);
				}
			}
			// print top 10 common urls
			solution.printTopKFrequentHitIps(ips, 10);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
