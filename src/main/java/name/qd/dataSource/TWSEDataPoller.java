package name.qd.dataSource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class TWSEDataPoller {
	private static final String[] PROD_DAILY_INFO = 
		{"http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report"
			, "/"
			, "_F3_1_8_"
			, ".php&type=csv"};
	
	private String sFilePath;
	
	public TWSEDataPoller(String sFilePath) {
		this.sFilePath = sFilePath;
	}
	
	public void downloadFile(String sDate, String sProdId) throws IOException {
		URL url = new URL(getQueryAPI(sDate, sProdId));
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(sFilePath + sDate + "_" + sProdId + ".csv");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
	
	private String getQueryAPI(String sDate, String sProdId) {
		return PROD_DAILY_INFO[0] + sDate + PROD_DAILY_INFO[1] + sDate + PROD_DAILY_INFO[2] + sProdId + PROD_DAILY_INFO[3];
	}
}
