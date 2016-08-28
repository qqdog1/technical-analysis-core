package name.qd.techAnalyst.dataSource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.util.FileConstUtil;

public class TWSEDataPoller {
	private static Logger logger = LogManager.getLogger(TWSEDataPoller.class);
	// 20160828 : 交易所改檔案名稱了 20166 表示6月 之後再調媽的爛死
	private static final String[] PROD_CLOSING_INFO = 
		{"http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report"
			, "/"
			, "_F3_1_8_"
			, ".php&type=csv"};
	
	private static final String DAILY_CLOSING_INFO = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX.php";
	
	private String filePath;
	
	TWSEDataPoller(String filePath) {
		this.filePath = filePath;
	}
	
	public void downloadProdClosingInfo(String yearMonth, String prodId) throws IOException {
		URL url = new URL(getProdClosingInfoQueryAPI(yearMonth, prodId));
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(FileConstUtil.getProdClosingFilePath(filePath, yearMonth, prodId));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
	
	public void downloadDailyClosingInfo(String date, String POSTDate) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(DAILY_CLOSING_INFO).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.getOutputStream().write(FileConstUtil.getDailyClosingPOSTBody(POSTDate).getBytes());
		ReadableByteChannel rbc = null;
		try {
			rbc = Channels.newChannel(connection.getInputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			logger.error("可能是非交易日 :[" + date + "]");
			return;
		}
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(FileConstUtil.getDailyClosingFilePath(filePath, date));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
	
	private String getProdClosingInfoQueryAPI(String date, String prodId) {
		return PROD_CLOSING_INFO[0] + date + PROD_CLOSING_INFO[1] + date + PROD_CLOSING_INFO[2] + prodId + PROD_CLOSING_INFO[3];
	}
}
