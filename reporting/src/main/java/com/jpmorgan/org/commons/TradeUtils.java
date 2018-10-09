package com.jpmorgan.org.commons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TradeUtils {

	public static Date stringToDateFormatter(String inputStringDate, String format) {
		
		final DateFormat fmt = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = inputStringDate != null ? fmt.parse(inputStringDate) : null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static String dateToStringFormatter(Date inputDate, String format) {
		return inputDate != null ? new SimpleDateFormat(format).format(inputDate) : null;  
	}
	
	public static Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
				
		return cal.getTime();
	}
	
}
