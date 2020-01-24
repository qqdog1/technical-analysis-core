package name.qd.analysis;

public class Constants {
	public static final String CHINESE_ENCODE = "Windows-950";
	public static final String BSR_PREFIX = "bsr_";
	
	public static String getBSRCacheName(String date) {
		StringBuilder sb = new StringBuilder();
		sb.append(BSR_PREFIX).append(date);
		return sb.toString();
	}
	
	public enum AnalyzerType {
		PRODUCT, MARKET;
	}
	
	public enum Exchange {
		TWSE,
		TPEX,
		;
	}
	
	public enum Action {
		BUY, SELL, NONE;
	}
	
	public enum WinLose {
		WIN, LOSE, NONE;
	}
	
	public enum OrderPriceType {
		OPEN, CLOSE, UPPER, LOWER, AVERAGE;
	}
	
	public enum OrderTriggerType {
		/**
		 * 所有買賣都作
		 */
		EVERY,
		/**
		 * 第一個買開始，且有倉才能賣
		 */
		FIRST_BUY,
		/**
		 * 第一個是買賣都可以 但倉位不會大於1
		 */
		FIRST_EVERY_SIDE,
		;
	}
}