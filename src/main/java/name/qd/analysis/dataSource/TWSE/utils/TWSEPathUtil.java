package name.qd.analysis.dataSource.TWSE.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.utils.StringCombineUtil;

public class TWSEPathUtil {
//	public static final String FILE_DIR = "./file/TWSE/";
	public static final String DAILY_CLOSING_INFO_DIR = "DCInfo";
	public static final String BUY_SELL_INFO_DIR = "bsr";
	
	private static final String SLASH = "/";
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static final String ADVANCE = "上漲(漲停)";
	public static final String DECLINE = "下跌(跌停)";
	public static final String UNCHANGED = "持平";
	
	public static Path getDailyClosingFolder(String baseFolder, String year) {
		return Paths.get(baseFolder, Exchange.TWSE.toString(), DAILY_CLOSING_INFO_DIR, year);
	}
	
	public static Path getDailyClosingFilePath(String baseFolder, String date) {
		return Paths.get(StringCombineUtil.combine(getDailyClosingFolder(baseFolder, date.substring(0, 4)).toString(), SLASH, DAILY_CLOSING_FILE_PREFIX, date, ".csv"));
	}
	
	public static Path getBuySellInfoFolder(String baseFolder, String date) {
		return Paths.get(baseFolder, Exchange.TWSE.toString(), BUY_SELL_INFO_DIR, date.substring(0, 4), date);
	}
	
	public static Path getBuySellInfoFilePath(String baseFolder, String date, String product) {
		return Paths.get(StringCombineUtil.combine(getBuySellInfoFolder(baseFolder, date).toString(), SLASH, product, ".csv"));
	}
}
