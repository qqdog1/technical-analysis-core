package name.qd.techAnalyst.dataSource.TWSE;

import name.qd.techAnalyst.util.StringCombineUtil;

public class TWSEConstants {
	public static final String FILE_DIR = "./file/TWSE/";
	public static final String PRODUCT_CLOSING_INFO_DIR = "PCInfo/";
	public static final String DAILY_CLOSING_INFO_DIR = "DCInfo/";
	
	private static final String PROD_CLOSING_FILE_PREFIX = "PCInfo_";
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static final String ADVANCE = "上漲(漲停)";
	public static final String DECLINE = "下跌(跌停)";
	
	public static String getProdClosingFolder(String product) {
		return StringCombineUtil.combine(FILE_DIR, PRODUCT_CLOSING_INFO_DIR, product, "/");
	}
	
	public static String getDailyClosingFolder(String year) {
		return StringCombineUtil.combine(FILE_DIR, DAILY_CLOSING_INFO_DIR, year, "/");
	}
	
	public static String getProdClosingFilePath(String year, String month, String product) {
		return StringCombineUtil.combine(getProdClosingFolder(product),
				PROD_CLOSING_FILE_PREFIX, year, month, "_", product, ".csv");
	}
	
	public static String getDailyClosingFilePath(String date) {
		return StringCombineUtil.combine(getDailyClosingFolder(date.substring(0, 4)),
				DAILY_CLOSING_FILE_PREFIX, date, ".csv");
	}
}
