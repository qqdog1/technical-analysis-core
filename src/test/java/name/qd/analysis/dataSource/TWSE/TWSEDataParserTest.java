package name.qd.analysis.dataSource.TWSE;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import name.qd.analysis.dataSource.TWSE.TWSEDataParser;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.dataSource.vo.DailyClosingInfo;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.utils.TimeUtil;

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
	
	@Test
	public void buySellParserTest() {
		TWSEDataParser parser = new TWSEDataParser();
		
		try {
			List<BuySellInfo> lst = parser.getBuySellInfo("2433", "20180403");
			for(BuySellInfo info : lst) {
				System.out.println(info.getProduct() + ":" + info.getSeqNo() + ":" + info.getBrokerName() + ":" + info.getPrice() + ":" + info.getBuyShare() + ":" + info.getSellShare());
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void bsTest() {
		TWSEDataParser parser = new TWSEDataParser();
		
		try {
			Map<String, List<BuySellInfo>> map = parser.getBuySellInfo("20180411");
			for(String product : map.keySet()) {
				System.out.println(product);
				
				List<BuySellInfo> lst = map.get(product);
				for(BuySellInfo info : lst) {
					System.out.println(info.getProduct() + ":" + info.getSeqNo() + ":" + info.getBrokerName() + ":" + info.getPrice() + ":" + info.getBuyShare() + ":" + info.getSellShare());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
