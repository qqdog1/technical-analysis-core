package name.qd.techAnalyst.dataSource.TWSE;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import name.qd.techAnalyst.dataSource.TWSE.TWSEDataParser;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class TWSEDataParserTest {
	
	@Test
	public void TWSEDataParserTestT() {
//		TWSEDataParser parser = new TWSEDataParser();
//		SimpleDateFormat sdf = TimeUtil.getDateFormat();
//		try {
//			List<ProductClosingInfo> lst = parser.readProdClosingInfo("2016", "04", "2453");
//			for(ProductClosingInfo p : lst) {
//				
//				System.out.println(sdf.format(p.getDate()) + p.getFilledShare() + p.getOpenPrice() + p.getUpperPrice()+p.getLowerPrice()+p.getClosePrice());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			DailyClosingInfo d = parser.readDailyClosingInfo("20160801");
//			if(d != null) {
//				System.out.println(d.getAdvance());
//				System.out.println(d.getDecline());
//				System.out.println(d.getUnchanged());
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
	
	@Test
	public void testParseDailyProducts() {
		TWSEDataParser parser = new TWSEDataParser();
		try {
			List<ProductClosingInfo> lst = parser.readAllNormalStock("20180305");
			for(ProductClosingInfo info : lst) {
				System.out.println(info.getFilledShare() + ":" + info.getFilledAmount() + ":" + 
						info.getOpenPrice() + ":" + info.getUpperPrice() + ":" + 
						info.getLowerPrice() + ":" + info.getClosePrice() + ":" + info.getADStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
