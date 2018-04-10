package name.qd.techAnalyst;

public class Constants {
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