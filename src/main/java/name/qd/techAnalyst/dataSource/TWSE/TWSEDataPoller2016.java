package name.qd.techAnalyst.dataSource.TWSE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.Constants;
import name.qd.techAnalyst.util.StringCombineUtil;
import name.qd.techAnalyst.util.TimeUtil;

public class TWSEDataPoller2016 extends TWSEDataPoller {
	private static Logger logger = LogManager.getLogger(TWSEDataPoller2016.class);
	
	private static final String PROD_CLOSING_INFO = "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAYMAIN.php";
	private static final String DAILY_CLOSING_INFO = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX.php";
	
	TWSEDataPoller2016(TWSEDataPoller nextPoller) {
		super(nextPoller);
	}
	
	protected void tryDownloadProdClosingInfo(String year, String month, String prodId) throws IOException {
		if(month.startsWith("0")) {
			month = month.replace("0", "");
		}
		HttpURLConnection connection = (HttpURLConnection) new URL(PROD_CLOSING_INFO).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.getOutputStream().write(getProdClosingPOSTBody(year, month, prodId).getBytes());
		ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(Constants.getProdClosingFilePath(year, month, prodId));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		logger.info("download product closing info success.");
	}
	
	protected void tryDownloadDailyClosingInfo(String date) throws IOException {
		String POSTDate = TimeUtil.AD2ROC(date);
		HttpURLConnection connection = (HttpURLConnection) new URL(DAILY_CLOSING_INFO).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.getOutputStream().write(getDailyClosingPOSTBody(POSTDate).getBytes());
		ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(Constants.getDailyClosingFilePath(date));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		logger.info("download daily closing info success.");
	}
	
	private String getProdClosingPOSTBody(String year, String month, String prodId) {
		return StringCombineUtil.combine("download=csv&query_year=", year, "&query_month=", month, "&CO_ID=", prodId);
	}
	
	private String getDailyClosingPOSTBody(String date) {
		return StringCombineUtil.combine("download=csv&qdate=", date, "&selectType=MS");
	}
}
