package name.qd.techAnalyst.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMdd-HH:mm:ss:SSS");
	
	public static String AD2ROC(String AD) {
		return String.valueOf(AD2ROC(Integer.parseInt(AD)));
	}
	
	public static String ROC2AD(String ROC) {
		return String.valueOf(ROC2AD(Integer.parseInt(ROC)));
	}
	
	public static int AD2ROC(int AD) {
		return AD - 1911;
	}
	
	public static int ROC2AD(int ROC) {
		return ROC + 1911;
	}
	
	public static List<String[]> getYearMonthBetween(Date from, Date to) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		List<String[]> lst = new ArrayList<String[]>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		calendar.set(Calendar.DATE, 1);
		while(calendar.getTimeInMillis() <= to.getTime()) {
			lst.add(new String[]{String.valueOf(calendar.get(Calendar.YEAR)), sdf.format(calendar.getTime())});
			calendar.add(Calendar.MONTH, 1);
		}
		return lst;
	}
	
	public static List<String> getDateBetween(Date from, Date to) {
		List<String> lst = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		while(calendar.getTimeInMillis() <= to.getTime()) {
			lst.add(sdfDate.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}
		return lst;
	}
	
	// 100/01/01 -> 20110101
	public static String getOutputFromROC(String date) {
		String[] dateInfo = date.split("/");
		return StringCombineUtil.combine(ROC2AD(dateInfo[0]), dateInfo[1], dateInfo[2]);
	}
	
	public static Date getToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static SimpleDateFormat getDateFormat() {
		return sdfDate;
	}
	
	public static SimpleDateFormat getDateTimeFormat() {
		return sdfDateTime;
	}
}
