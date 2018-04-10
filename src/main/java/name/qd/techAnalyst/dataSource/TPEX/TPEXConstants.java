package name.qd.techAnalyst.dataSource.TPEX;

import name.qd.techAnalyst.utils.StringCombineUtil;

public class TPEXConstants {
	public static final String FILE_DIR = "./file/TPEX/";
	
	public static final String DAILY_CLOSING_INFO_DIR = "DCInfo/";
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static String getDailyClosingFolder(String year) {
		return StringCombineUtil.combine(FILE_DIR, DAILY_CLOSING_INFO_DIR, year, "/");
	}
	
	public static String getDailyClosingFilePath(String date) {
		return StringCombineUtil.combine(getDailyClosingFolder(date.substring(0, 4)), DAILY_CLOSING_FILE_PREFIX, date, ".csv");
	}
}
