package name.qd.techAnalyst.dataSource.TWSE;

import java.io.IOException;

import name.qd.techAnalyst.dataSource.TWSE.TWSEDataPoller2016;
import name.qd.techAnalyst.dataSource.TWSE.TWSEDataPoller2018;


public class TWSEDataPollerTest {
	
	public static void main(String[] s) {
		new TWSEDataPollerTest();
	}
	
	private TWSEDataPollerTest() {
			new TWSEDataPoller2018(new TWSEDataPoller2016(null)).downloadProdClosingInfo("2016", "04", "2453");
		
			new TWSEDataPoller2018(null).downloadDailyClosingInfo("20160801");
			
			try {
				new TWSEDataPoller2018(null).tryDownloadDailyClosingInfo("");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
