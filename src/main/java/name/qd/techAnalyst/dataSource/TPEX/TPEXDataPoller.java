package name.qd.techAnalyst.dataSource.TPEX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import name.qd.techAnalyst.util.TimeUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TPEXDataPoller {
	private final OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(5, TimeUnit.SECONDS).build();
	private HttpUrl httpUrl = HttpUrl.parse("http://www.tpex.org.tw/web/stock/aftertrading/daily_close_quotes/stk_quote_result.php");
	
	public TPEXDataPoller() {
	}
	
	public void downloadDailyClosingInfo(String date) throws IOException, ParseException {
		String filePathName = TPEXConstants.getDailyClosingFilePath(date);
		String roc = TimeUtil.AD2ROC(date);
		Path path = new File(filePathName).toPath();
		if(Files.exists(path)) return;

		HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
		urlBuilder.addEncodedQueryParameter("l", "zh-tw");
		urlBuilder.addEncodedQueryParameter("o", "csv");
		urlBuilder.addEncodedQueryParameter("d", roc);
		urlBuilder.addEncodedQueryParameter("s", "0");
		byte[] result = sendSyncHttpGet(urlBuilder.build().url().toString());
		
		if(!Files.exists(path)) {
			Files.createFile(path);
			Files.write(path, result);
		}
	}
	
	private byte[] sendSyncHttpGet(String url) throws IOException {
		return okHttpClient.newCall(new Request.Builder().url(url).build()).execute().body().bytes();
	}
}
