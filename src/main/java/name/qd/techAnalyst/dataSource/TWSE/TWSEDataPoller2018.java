package name.qd.techAnalyst.dataSource.TWSE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import name.qd.techAnalyst.Constants;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TWSEDataPoller2018 extends TWSEDataPoller {
	private final OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(5, TimeUnit.SECONDS).build();
	private HttpUrl httpUrl = HttpUrl.parse("http://www.tse.com.tw/exchangeReport");
	
	TWSEDataPoller2018(TWSEDataPoller nextPoller) {
		super(nextPoller);
	}

	@Override
	protected void tryDownloadProdClosingInfo(String year, String month, String prodId) throws IOException {
		String filePathName = Constants.getProdClosingFilePath(year, month, prodId);
		Path path = new File(filePathName).toPath();
		if(Files.exists(path)) return;
		
		HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
		urlBuilder.addPathSegments("STOCK_DAY");
		urlBuilder.addEncodedQueryParameter("response", "csv");
		urlBuilder.addEncodedQueryParameter("date", year+month+"01");
		urlBuilder.addEncodedQueryParameter("stockNo", prodId);
		byte[] result = sendSyncHttpGet(urlBuilder.build().url().toString());
		
		if(!Files.exists(path)) {
			Files.createFile(path);
			Files.write(path, result);
		}
	}

	@Override
	protected void tryDownloadDailyClosingInfo(String date) throws IOException {
		String filePathName = Constants.getDailyClosingFilePath(date);
		Path path = new File(filePathName).toPath();
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
