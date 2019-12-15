package name.qd.analysis.dataSource.TWSE;

import java.io.IOException;

import name.qd.analysis.dataSource.TWSE.TWSEDataPoller2018;


public class TWSEDataPollerTest {
	private String dataPath = "D:/workspace/file";
	
	public static void main(String[] s) {
		new TWSEDataPollerTest();
	}
	
	private TWSEDataPollerTest() {
			new TWSEDataPoller2018(null, dataPath).downloadDailyClosingInfo("20160801");
			
			try {
				new TWSEDataPoller2018(null, dataPath).tryDownloadDailyClosingInfo("");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
