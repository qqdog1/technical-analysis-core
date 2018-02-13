package name.qd.techAnalyst.dataSource.TWSE;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import name.qd.techAnalyst.dataSource.TWSE.TWSEDataParser;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class TWSEDataParserTest {
	public static void main(String[] s) {
		new TWSEDataParserTest();
	}
	
	private TWSEDataParserTest() {
		TWSEDataParser parser = new TWSEDataParser();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			List<ProductClosingInfo> lst = parser.readProdClosingInfo("2016", "04", "2453");
			for(ProductClosingInfo p : lst) {
				
				System.out.println(sdf.format(p.getDate()) + p.getFilledShare() + p.getOpenPrice() + p.getUpperPrice()+p.getLowerPrice()+p.getClosePrice());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
