package name.qd.techAnalyst.dataSource;
import java.io.IOException;

import name.qd.techAnalyst.dataSource.TWSEDataPoller;


public class TWSEDataPollerTest {
	
	public static void main(String[] s) {
		new TWSEDataPollerTest();
	}
	
	private TWSEDataPollerTest() {
		try {
			new TWSEDataPoller("./file/").downloadProdClosingInfo("201604", "2453");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			new TWSEDataPoller("./file/").downloadDailyClosingInfo("20160801", "105/08/01");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
