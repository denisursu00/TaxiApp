package ro.taxiApp.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.format.ISODateTimeFormat;

/**
 * 
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	public static boolean intervalsIntersect(Date interval1StartDate, Date interval1EndDate,
			Date interval2StartDate, Date interval2EndDate) {
		
		return (
			(interval1StartDate.before(interval2EndDate) || interval1StartDate.equals(interval2EndDate)) &&
			(interval1EndDate.equals(interval2StartDate) || interval1EndDate.after(interval2StartDate))
		);
	}
	
	public static Date nullHourMinutesSeconds(Date date) {
		Calendar calendar = nullHourMinutesSecondsAsCalendar(date);
		if (date != null) {
			return calendar.getTime();
		}
		return null;
	}
	
	public static Calendar nullHourMinutesSecondsAsCalendar(Date date) {		
		if (date != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar;
		} else {
			return null;
		}
	}

	public static Date maximizeHourMinutesSeconds(Date date) {
		Calendar calendar = maximizeHourMinutesSecondsAsCalendar(date);
		if (calendar != null) {
			return calendar.getTime();
		}
		return null;
	}
	
	public static Calendar maximizeHourMinutesSecondsAsCalendar(Date date) {
		if (date != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);

			return calendar;
		} else {
			return null;
		}
	}

	public static Calendar toCalendar(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static Date getPreviousDay(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_WEEK, -1);

		return calendar.getTime();
	}
	
	public static Integer getMonth(Date date) {
		Calendar dateAsCalendar = toCalendar(date);
		return dateAsCalendar.get(Calendar.MONTH);
	}
	
	public static Integer getDayOfMonth(Date date) {
		Calendar dateAsCalendar = toCalendar(date);
		return dateAsCalendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static Date setCurrentYear(Date date) {
		Calendar dateAsCalendar = toCalendar(date);
		Calendar currentDateAsCalendar = toCalendar(new Date());
		dateAsCalendar.set(Calendar.YEAR, currentDateAsCalendar.get(Calendar.YEAR));
		return dateAsCalendar.getTime();
	}
	

	public static boolean isDate(String dateAsString, String format) {
		
		try {
			parseDate(dateAsString, new String[] { format });
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static long pozitiveNumberDaysBetween(Date firstDate, Date secondDate) {
		
		LocalDate firstLocalDate = firstDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate secondLocalDate = secondDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if (firstLocalDate.isAfter(secondLocalDate)) {
			return - ChronoUnit.DAYS.between(firstLocalDate, secondLocalDate);
		}
		
		return ChronoUnit.DAYS.between(secondLocalDate, firstLocalDate);
	}
	
	public static long numberDaysBetween(Date firstDate, Date secondDate) {
		
		LocalDate firstLocalDate = firstDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate secondLocalDate = secondDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return ChronoUnit.DAYS.between(secondLocalDate, firstLocalDate);
	}
	
	public static boolean isBeforeDate(Date firstDate, Date secondDate) {
		LocalDate firstLocalDate = firstDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate secondLocalDate = secondDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return firstLocalDate.isBefore(secondLocalDate);
	}
	
	public static boolean isAfterDate(Date firstDate, Date secondDate) {
		LocalDate firstLocalDate = firstDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate secondLocalDate = secondDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return firstLocalDate.isAfter(secondLocalDate);
	}
	
	public static boolean isSameDate(Date firstDate, Date secondDate) {
		LocalDate firstLocalDate = firstDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate secondLocalDate = secondDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return firstLocalDate.isEqual(secondLocalDate);
	}
	
	public static Date parseDate(String dateAsString, String pattern) {
		try {
			if (StringUtils.isEmpty(dateAsString)) {
				return null;
			}
			return parseDate(dateAsString, new String[] {pattern});
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}
	
	public static Date parse(String dateAsString, String format) throws ParseException {
		String[] formats = { format };
		return DateUtils.parseDate(dateAsString, formats);
	}
	
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static Integer getYear(Date date) {
		Calendar dateAsCalendar = toCalendar(date);
		return dateAsCalendar.get(Calendar.YEAR);
	}
	
	public static boolean isWeekend(Date date) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		DayOfWeek dayOfWeek = DayOfWeek.of(localDate.get(ChronoField.DAY_OF_WEEK));
	    switch (dayOfWeek) {
		    case SATURDAY:
		    case SUNDAY:
		      return true;
		    default:
		      return false;
	    }
	}
	
	public static boolean isSameMonthAndDay(Date date, Long month, Long day) {
		Calendar dateAsCalendar = toCalendar(date);
		int monthExtractedFromDate = dateAsCalendar.get(Calendar.MONTH);
		int dayExtractedFromDate =  dateAsCalendar.get(Calendar.DAY_OF_MONTH);
		
		if (month == (monthExtractedFromDate + 1) && day == dayExtractedFromDate) {
			return true;
		}
		return false;
	}
}