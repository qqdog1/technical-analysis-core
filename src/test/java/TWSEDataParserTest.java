import java.io.IOException;
import java.util.List;

import name.qd.techAnalyst.dataSource.TWSEDataParser;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TWSEDataParserTest {
	public static void main(String[] s) {
		new TWSEDataParserTest();
	}
	
	private TWSEDataParserTest() {
		TWSEDataParser parser = new TWSEDataParser("./");
		try {
			List<ProdClosingInfo> lst = parser.readProdDailyClosingInfo("201604", "2453");
			for(ProdClosingInfo p : lst) {
				System.out.println(p.getDate() + p.getFilledShare() + p.getOpenPrice() + p.getUpperPrice()+p.getLowerPrice()+p.getClosePrice());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
