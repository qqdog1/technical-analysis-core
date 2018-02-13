package name.qd.techAnalyst.dataSource.TWSE;

import java.io.IOException;

public abstract class TWSEDataPoller {
	private TWSEDataPoller nextPoller;
	
	public TWSEDataPoller(TWSEDataPoller nextPoller) {
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
	
	protected abstract void tryDownloadProdClosingInfo(String year, String month, String prodId) throws IOException;
	protected abstract void tryDownloadDailyClosingInfo(String date) throws IOException;
}
