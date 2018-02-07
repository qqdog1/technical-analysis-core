package name.qd.techAnalyst.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
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
	
	public static List<String[]> getYearMonthBetween(Date dateFrom, Date dateTo) {
		List<String[]> lst = new ArrayList<String[]>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFrom);
		calendar.set(Calendar.DATE, 1);
		while(calendar.getTimeInMillis() <= dateTo.getTime()) {
			lst.add(new String[]{String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)+1)});
			calendar.add(Calendar.MONTH, 1);
		}
		return lst;
	}
	
	public static List<String> getDateBetween(Date dateFrom, Date dateTo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<String> lst = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFrom);
		while(calendar.getTimeInMillis() <= dateTo.getTime()) {
			lst.add(sdf.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}
		return lst;
	}
	
	// 100/01/01 -> 20110101
	public static String getOutputFromROC(String date) {
		String[] dateInfo = date.split("/");
		return ROC2AD(dateInfo[0]) + dateInfo[1] + dateInfo[2];
	}
}
