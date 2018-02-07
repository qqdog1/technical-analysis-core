package name.qd.techAnalyst.dataSource;

import java.io.IOException;

public abstract class DataPoller {
	private DataPoller nextPoller;
	
	public DataPoller(DataPoller nextPoller) {
		this.nextPoller = nextPoller;
	}
	
	public void downloadProdClosingInfo(String year, String month, String prodId) {
		try {
			tryDownloadProdClosingInfo(year, month, prodId);
		} catch (IOException e) {
			if(nextPoller != null) {
				nextPoller.downloadProdClosingInfo(year, month, prodId);
			}
		}
	}
	
	public void downloadDailyClosingInfo(String date) {
		try {
			tryDownloadDailyClosingInfo(date);
		} catch (IOException e) {
			if(nextPoller != null) {
				nextPoller.downloadDailyClosingInfo(date);
			}
		}
	}
	
	public abstract void tryDownloadProdClosingInfo(String year, String month, String prodId) throws IOException;
	public abstract void tryDownloadDailyClosingInfo(String date) throws IOException;
}
