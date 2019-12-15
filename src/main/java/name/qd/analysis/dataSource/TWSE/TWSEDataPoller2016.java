package name.qd.analysis.dataSource.TWSE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.utils.StringCombineUtil;
import name.qd.analysis.utils.TimeUtil;

public class TWSEDataPoller2016 extends TWSEDataPoller {
	private static Logger logger = LoggerFactory.getLogger(TWSEDataPoller2016.class);
	
	private static final String DAILY_CLOSING_INFO = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX.php";
	
	TWSEDataPoller2016(TWSEDataPoller nextPoller, String dataPath) {
		super(nextPoller, dataPath);
	}
	
	protected void tryDownloadDailyClosingInfo(String date) throws IOException {
		String POSTDate = null;
		try {
			POSTDate = TimeUtil.AD2ROC(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		HttpURLConnection connection = (HttpURLConnection) new URL(DAILY_CLOSING_INFO).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.getOutputStream().write(getDailyClosingPOSTBody(POSTDate).getBytes());
		ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(TWSEConstants.getDailyClosingFilePath(dataPath, date));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		logger.info("download daily closing info success.");
	}
	
	private String getDailyClosingPOSTBody(String date) {
		return StringCombineUtil.combine("download=csv&qdate=", date, "&selectType=MS");
	}
}
