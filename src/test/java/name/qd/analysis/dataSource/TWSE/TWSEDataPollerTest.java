package name.qd.analysis.dataSource.TWSE;

import java.io.IOException;

import name.qd.analysis.dataSource.TWSE.TWSEDataPoller2018;


public class TWSEDataPollerTest {
	
	public static void main(String[] s) {
		new TWSEDataPollerTest();
	}
	
	private TWSEDataPollerTest() {
			new TWSEDataPoller2018(null).downloadDailyClosingInfo("20160801");
			
			try {
				new TWSEDataPoller2018(null).tryDownloadDailyClosingInfo("");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
