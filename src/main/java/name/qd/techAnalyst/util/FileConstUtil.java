package name.qd.techAnalyst.util;

public class FileConstUtil {
	private static final String PROD_CLOSING_FILE_PREFIX = "PCInfo_";
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static final String ADVANCE = "上漲(漲停)";
	public static final String DECLINE = "下跌(跌停)";
	
	public static String getProdClosingFilePath(String filePath, String yearMonth, String prodId) {
		return filePath + FileConstUtil.PROD_CLOSING_FILE_PREFIX + yearMonth + "_" + prodId + ".csv";
	}
	
	public static String getDailyClosingFilePath(String filePath, String date) {
		return filePath + FileConstUtil.DAILY_CLOSING_FILE_PREFIX + date + ".csv";
	}
	
	public static String getDailyClosingPOSTBody(String date) {
		return "download=csv&qdate=" + date + "&selectType=MS";
	}
}
