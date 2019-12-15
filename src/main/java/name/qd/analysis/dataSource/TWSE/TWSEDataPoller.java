package name.qd.analysis.dataSource.TWSE;

import java.io.IOException;

public abstract class TWSEDataPoller {
	private TWSEDataPoller nextPoller;
	protected final String dataPath;
	
	public TWSEDataPoller(TWSEDataPoller nextPoller, String dataPath) {
		this.nextPoller = nextPoller;
		this.dataPath = dataPath;
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
