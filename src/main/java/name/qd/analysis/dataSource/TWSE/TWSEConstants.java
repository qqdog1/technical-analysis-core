package name.qd.analysis.dataSource.TWSE;

import name.qd.analysis.utils.StringCombineUtil;

public class TWSEConstants {
//	public static final String FILE_DIR = "./file/TWSE/";
	public static final String DAILY_CLOSING_INFO_DIR = "DCInfo/";
	public static final String BUY_SELL_INFO_DIR = "bsr/";
	
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static final String ADVANCE = "上漲(漲停)";
	public static final String DECLINE = "下跌(跌停)";
	public static final String UNCHANGED = "持平";
	
	public static String getDailyClosingFolder(String baseFolder, String year) {
		return StringCombineUtil.combine(baseFolder, DAILY_CLOSING_INFO_DIR, year, "/");
	}
	
	public static String getDailyClosingFilePath(String baseFolder, String date) {
		return StringCombineUtil.combine(getDailyClosingFolder(baseFolder, date.substring(0, 4)), DAILY_CLOSING_FILE_PREFIX, date, ".csv");
	}
	
	public static String getBuySellInfoFolder(String baseFolder, String date) {
		return StringCombineUtil.combine(baseFolder, BUY_SELL_INFO_DIR, date, "/");
	}
	
	public static String getBuySellInfoFilePath(String baseFolder, String date, String product) {
		return StringCombineUtil.combine(getBuySellInfoFolder(baseFolder, date), product, ".csv");
	}
}
