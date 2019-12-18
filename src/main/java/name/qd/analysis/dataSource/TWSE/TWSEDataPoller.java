package name.qd.analysis.dataSource.TWSE;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TWSEDataPoller {
	private Logger log = LoggerFactory.getLogger(TWSEDataPoller.class);
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
			log.error("Download daily closing info failed. {}", date, e);
			if(nextPoller != null) {
				nextPoller.downloadDailyClosingInfo(date);
			}
		}
	}
	
	protected abstract void tryDownloadDailyClosingInfo(String date) throws IOException;
}
