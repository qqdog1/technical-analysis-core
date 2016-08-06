package name.qd.techAnalyst.dataSource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import name.qd.techAnalyst.util.FileConstUtil;

public class TWSEDataPoller {
	private static final String[] PROD_CLOSING_INFO = 
		{"http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report"
			, "/"
			, "_F3_1_8_"
			, ".php&type=csv"};
	
	private static final String DAILY_CLOSING_INFO = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX.php";
	
	private String sFilePath;
	
	TWSEDataPoller(String sFilePath) {
		this.sFilePath = sFilePath;
	}
	
	public void downloadProdClosingInfo(String sYearMonth, String sProdId) throws IOException {
		URL url = new URL(getProdClosingInfoQueryAPI(sYearMonth, sProdId));
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(FileConstUtil.getProdClosingFilePath(sFilePath, sYearMonth, sProdId));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
	
	public void downloadDailyClosingInfo(String sDate, String sPOSTDate) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(DAILY_CLOSING_INFO).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.getOutputStream().write(FileConstUtil.getDailyClosingPOSTBody(sPOSTDate).getBytes());
		ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(FileConstUtil.getDailyClosingFilePath(sFilePath, sDate));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
	
	private String getProdClosingInfoQueryAPI(String sDate, String sProdId) {
		return PROD_CLOSING_INFO[0] + sDate + PROD_CLOSING_INFO[1] + sDate + PROD_CLOSING_INFO[2] + sProdId + PROD_CLOSING_INFO[3];
	}
}
