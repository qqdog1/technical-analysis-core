package name.qd.analysis.dataSource.TWSE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import name.qd.analysis.dataSource.TWSE.utils.TWSEPathUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TWSEDataPoller2020 extends TWSEDataPoller {
	private final OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(5, TimeUnit.SECONDS).build();
	private HttpUrl httpUrl = HttpUrl.parse("http://www.twse.com.tw/exchangeReport");
	
	public TWSEDataPoller2020(TWSEDataPoller nextPoller, String dataPath) {
		super(nextPoller, dataPath);
	}

	@Override
	protected void tryDownloadDailyClosingInfo(String date) throws IOException {
		Path path = TWSEPathUtil.getDailyClosingFilePath(dataPath, date);
		if(Files.exists(path)) return;

		HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
		urlBuilder.addPathSegments("MI_INDEX");
		urlBuilder.addEncodedQueryParameter("response", "csv");
		urlBuilder.addEncodedQueryParameter("date", date);
		urlBuilder.addEncodedQueryParameter("type", "ALL");
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
