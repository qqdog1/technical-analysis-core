package name.qd.techAnalyst.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMdd-HH:mm:ss:SSS");
	
	public static String YearAD2ROC(String AD) {
		return String.valueOf(YearAD2ROC(Integer.parseInt(AD)));
	}
	
	public static String YearROC2AD(String ROC) {
		return String.valueOf(YearROC2AD(Integer.parseInt(ROC)));
	}
	
	public static int YearAD2ROC(int AD) {
		return AD - 1911;
	}
	
	public static int YearROC2AD(int ROC) {
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
		return StringCombineUtil.combine(YearROC2AD(dateInfo[0]), dateInfo[1], dateInfo[2]);
	}
	
	// 20110101 -> 100/01/01
	public static String AD2ROC(String data) throws ParseException {
		Date date = sdfDate.parse(data);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		year = YearAD2ROC(year);
		return StringCombineUtil.combine(String.valueOf(year), "/", 
				String.format("%02d", calendar.get(Calendar.MONTH)+1), "/",
				String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
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
