package name.qd.techAnalyst.util;

public class FileConstUtil {
	private static final String PROD_CLOSING_FILE_PREFIX = "PCInfo_";
	private static final String DAILY_CLOSING_FILE_PREFIX = "DCInfo_";
	
	public static final String ADVANCE = "上漲(漲停)";
	public static final String DECLINE = "下跌(跌停)";
	
	public static String getProdClosingFilePath(String filePath, String year, String month, String prodId) {
		StringBuilder sb = new StringBuilder();
		sb.append(filePath).append(FileConstUtil.PROD_CLOSING_FILE_PREFIX).append(year).append(month).append("_").append(prodId).append(".csv");
		return sb.toString();
	}
	
	public static String getDailyClosingFilePath(String filePath, String date) {
		return filePath + FileConstUtil.DAILY_CLOSING_FILE_PREFIX + date + ".csv";
	}
	
	public static String getProdClosingPOSTBody(String year, String month, String prodId) {
		StringBuilder sb = new StringBuilder();
		sb.append("query_year=").append(year).append("&query_month=").append(month).append("&CO_ID=").append(prodId);
		return sb.toString(); 
	}
	
	public static String getDailyClosingPOSTBody(String date) {
		StringBuilder sb = new StringBuilder();
		sb.append("download=csv&qdate=").append(date).append("&selectType=MS");
		return sb.toString();
	}
}
