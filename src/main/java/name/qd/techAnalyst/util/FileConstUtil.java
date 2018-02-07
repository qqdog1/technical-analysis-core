package name.qd.techAnalyst.util;

public class FileConstUtil {
	public static final String FILE_DIR = "./file/";
	
	private static final String PROD_CLOSING_FILE_PREFIX = "PCInfo_";
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static final String ADVANCE = "上漲(漲停)";
	public static final String DECLINE = "下跌(跌停)";
	
	public static String getProdClosingFilePath(String year, String month, String prodId) {
		return StringCombineUtil.combine(FILE_DIR, FileConstUtil.PROD_CLOSING_FILE_PREFIX, year, month, "_", prodId, ".csv");
	}
	
	public static String getDailyClosingFilePath(String date) {
		return StringCombineUtil.combine(FILE_DIR, FileConstUtil.DAILY_CLOSING_FILE_PREFIX, date, ".csv");
	}
}