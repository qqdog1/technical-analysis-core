package name.qd.analysis.chip;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.utils.TimeUtil;
import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.CoordinateCacheManager;
import name.qd.fileCache.cache.CoordinateObject;

public class ChipAnalyzerManagerTest {
	private ChipAnalyzerManager manager = ChipAnalyzerManager.getInstance();
	private DataSource dataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
//	private FileCacheManager fileCacheManager = manager.getFCM();
	
	public static void main(String[] s) {
		new ChipAnalyzerManagerTest();
	}
	
	private ChipAnalyzerManagerTest() {
		transCache();
		
//		printSpecificX("1101");
//		printSpecificY("700B兆豐板橋");
//		printSpecificObj("1101", "9204凱基台中");
	}
	
	private void transCache() {
		try {
			Date from = TimeUtil.getDateFormat().parse("20190527");
			Date to = TimeUtil.getDateFormat().parse("20190528");
			manager.getAnalysisResult(dataSource, ChipAnalyzers.DAILY_PNL, "", "", from, to, 0, true);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		manager.transToDailyCache(dataSource, date, date);
	}
	
//	private void printSpecificX(String x) {
//		try {
//			CoordinateCacheManager cacheManager = fileCacheManager.getCoordinateCacheInstance("bsr_20190527", DailyOperate.class.getName());
//	
//			List<CoordinateObject> lst = cacheManager.getByX(x);
//			for(CoordinateObject obj : lst) {
//				DailyOperate daily = (DailyOperate) obj;
//				StringBuilder sb = new StringBuilder();
//				sb.append(daily.getBrokerName()).append(":").append(daily.getProduct()).append(":").append(daily.getOpenShare()).append(":").append(daily.getAvgPrice());
//				System.out.println(sb.toString());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	private void printSpecificY(String y) {
//		try {
//			CoordinateCacheManager cacheManager = fileCacheManager.getCoordinateCacheInstance("bsr_20190527", DailyOperate.class.getName());
//	
//			List<CoordinateObject> lst = cacheManager.getByY(y);
//			for(CoordinateObject obj : lst) {
//				DailyOperate daily = (DailyOperate) obj;
//				StringBuilder sb = new StringBuilder();
//				sb.append(daily.getBrokerName()).append(":").append(daily.getProduct()).append(":").append(daily.getOpenShare());
//				System.out.println(sb.toString());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void printSpecificObj(String x, String y) {
//		try {
//			CoordinateCacheManager cacheManager = fileCacheManager.getCoordinateCacheInstance("bsr_20190527", DailyOperate.class.getName());
//			
//			CoordinateObject obj = cacheManager.get(x, y);
//			DailyOperate daily = (DailyOperate) obj;
//			StringBuilder sb = new StringBuilder();
//			sb.append(daily.getBrokerName()).append(":").append(daily.getProduct()).append(":").append(daily.getOpenShare());
//			System.out.println(sb.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
