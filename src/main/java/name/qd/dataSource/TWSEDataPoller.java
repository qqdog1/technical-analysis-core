package name.qd.dataSource;

public class TWSEDataPoller {
	private static final String[] PROD_DAILY_INFO = 
		{"http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report"
			, "/"
			, "_F3_1_8_"
			, ".php&type=csv"};
	
	private String getQueryAPI(String sDate, String sProdId) {
		return PROD_DAILY_INFO[0] + sDate + PROD_DAILY_INFO[1] + sDate + PROD_DAILY_INFO[2] + sProdId + PROD_DAILY_INFO[3];
	}
}
