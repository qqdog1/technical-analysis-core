package name.qd.techAnalyst.util;

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
		List<String> set = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFrom);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		while(calendar.getTimeInMillis() <= dateTo.getTime()) {
			String sYear = String.valueOf(calendar.get(Calendar.YEAR));
			String sMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
			set.add(sYear + sMonth);
			calendar.add(Calendar.MONTH, 1);
		}
		return set;
	}
}
