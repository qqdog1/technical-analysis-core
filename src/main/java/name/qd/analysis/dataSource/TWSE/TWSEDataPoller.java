package name.qd.analysis.dataSource.TWSE;

import java.io.IOException;

public abstract class TWSEDataPoller {
	private TWSEDataPoller nextPoller;
	
	public TWSEDataPoller(TWSEDataPoller nextPoller) {
		this.nextPoller = nextPoller;
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
	
	protected abstract void tryDownloadDailyClosingInfo(String date) throws IOException;
}
