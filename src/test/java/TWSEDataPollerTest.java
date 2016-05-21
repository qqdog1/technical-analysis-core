import java.io.IOException;

import name.qd.techAnalyst.dataSource.TWSEDataPoller;


public class TWSEDataPollerTest {
	
	public static void main(String[] s) {
		new TWSEDataPollerTest();
	}
	
	private TWSEDataPollerTest() {
		try {
			new TWSEDataPoller("./").downloadFile("201604", "2453");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
