package mobi.monaca.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {
	public static String getCurrentTimeStamp() {
		Date now = new Date();
		String format1 = new SimpleDateFormat("MM-dd HH:mm:ss").format(now);
		return format1;
	}
}
