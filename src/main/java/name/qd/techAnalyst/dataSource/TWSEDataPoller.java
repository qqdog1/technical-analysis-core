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
	
	private static final String PROD_CLOSING_INFO = "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAYMAIN.php";
	private static final String DAILY_CLOSING_INFO = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX.php";
	
	private String filePath;
	
	TWSEDataPoller(String filePath) {
		this.filePath = filePath;
	}
	
	public void downloadProdClosingInfo(String year, String month, String prodId) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(PROD_CLOSING_INFO).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.getOutputStream().write(FileConstUtil.getProdClosingPOSTBody(year, month, prodId).getBytes());
		
		ReadableByteChannel rbc = null;
		try {
			rbc = Channels.newChannel(connection.getInputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return;
		}
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(FileConstUtil.getProdClosingFilePath(filePath, year, month, prodId));
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
			logger.error("可能是非交易日 :[{}]", date);
			return;
		}
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(FileConstUtil.getDailyClosingFilePath(filePath, date));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
}
