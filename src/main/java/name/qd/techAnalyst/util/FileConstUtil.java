package name.qd.techAnalyst.util;

public class FileConstUtil {
	private static final String PROD_CLOSING_FILE_PREFIX = "PCInfo_";
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static String getProdClosingFilePath(String sFilePath, String sYearMonth, String sProdId) {
		return sFilePath + FileConstUtil.PROD_CLOSING_FILE_PREFIX + sYearMonth + "_" + sProdId + ".csv";
	}
	
	public static String getDailyClosingFilePath(String sFilePath, String sDate) {
		return sFilePath + FileConstUtil.DAILY_CLOSING_FILE_PREFIX + sDate + ".csv";
	}
	
	public static String getDailyClosingPOSTBody(String sDate) {
		return "download=csv&qdate=" + sDate + "&selectType=MS";
	}
}
