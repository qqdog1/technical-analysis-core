package name.qd.techAnalyst.dataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import name.qd.techAnalyst.dataSource.TWSE.TWSEDataParser;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TWSEDataParserTest {
	public static void main(String[] s) {
		new TWSEDataParserTest();
	}
	
	private TWSEDataParserTest() {
		TWSEDataParser parser = new TWSEDataParser();
		try {
			List<ProdClosingInfo> lst = parser.readProdClosingInfo("2016", "04", "2453");
			for(ProdClosingInfo p : lst) {
				System.out.println(p.getDate() + p.getFilledShare() + p.getOpenPrice() + p.getUpperPrice()+p.getLowerPrice()+p.getClosePrice());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			DailyClosingInfo d = parser.readDailyClosingInfo("20160801");
			if(d != null) {
				System.out.println(d.getAdvance());
				System.out.println(d.getDecline());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
