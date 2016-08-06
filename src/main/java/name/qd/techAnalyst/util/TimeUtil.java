package name.qd.techAnalyst.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
	public static String AD2ROC(String sAD) {
		return String.valueOf(AD2ROC(Integer.parseInt(sAD)));
	}
	
	public static String ROC2AD(String sROC) {
		return String.valueOf(ROC2AD(Integer.parseInt(sROC)));
	}
	
	public static int AD2ROC(int iAD) {
		return iAD - 1911;
	}
	
	public static int ROC2AD(int iROC) {
		return iROC + 1911;
	}
	
	public static List<String> getYearMonthBetween(Date dateFrom, Date dateTo) {
		List<String> lst = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFrom);
		calendar.set(Calendar.DATE, 1);
		while(calendar.getTimeInMillis() <= dateTo.getTime()) {
			lst.add(sdf.format(calendar.getTime()));
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
	
	public static List<String> getPOSTDateBetween(Date dateFrom, Date dateTo) {
		SimpleDateFormat sdf = new SimpleDateFormat("/MM/dd");
		List<String> lst = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFrom);
		while(calendar.getTimeInMillis() <= dateTo.getTime()) {
			int iYear = AD2ROC(calendar.get(Calendar.YEAR));
			lst.add(String.valueOf(iYear) + sdf.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}
		return lst;
	}
}
