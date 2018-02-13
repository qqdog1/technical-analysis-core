package name.qd.techAnalyst;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg5Day;
import name.qd.techAnalyst.dataSource.DataManager;
import name.qd.techAnalyst.dataSource.TWSE.TWSEDataManager;

public class TechAnalyst {
	private TechAnalyzerManager analyzerManager;
	private DataManager twseDataManager;
	
	private TechAnalyst() {
		// 要分析哪一檔商品  時間  哪種分析方式
		// 檢查檔案 分析 回傳結果?
		
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		Logger logger = LogManager.getLogger(TechAnalyst.class);
		
		analyzerManager = new TechAnalyzerManager();
		twseDataManager = new TWSEDataManager();
		
		String sFrom = "20160301";
		String sTo = "20160601";
		String sProdId = "2453";
		String sAnalyzer = MovingAvg5Day.class.getSimpleName();
		
//		try {
//			twseDataManager.checkDateAndDownload(sFrom, sTo, sProdId);
//		} catch (ParseException | IOException e) {
//			logger.error(e.getMessage(), e);
//		}
//		
//		List<AnalysisResult> lstResult = analyzerManager.analyze(twseDataManager, sAnalyzer, sFrom, sTo, sProdId);
//		
//		if(lstResult == null) {
//			return;
//		}
//		
//		for(AnalysisResult result : lstResult) {
//			System.out.println(StringCombineUtil.combine(result.getDate(), ":", String.valueOf(result.getValue())));
//		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Date date = new Date();
		System.out.println(sdf.format(date));
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
