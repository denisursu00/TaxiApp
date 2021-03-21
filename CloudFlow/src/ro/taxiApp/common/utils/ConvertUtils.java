package ro.taxiApp.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Contine metode utilitare pentru conversia diverselor tipuri de date.
 * 
 * 
 */
public class ConvertUtils {

	/**
	 * Converteste un obiect de tip <code>Date</code> intr-un <code>Calendar</code>.
	 * @param date data calendaristica
	 * @return data sub forma de <code>Calendar</code>
	 */
	public static Calendar getCalendarFromDate(Date date) {
		
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar;
	}
}