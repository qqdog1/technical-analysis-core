package name.qd.techAnalyst;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.analyzer.impl.MovingAvg5Day;
import name.qd.techAnalyst.dataSource.TWSEDataManager;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TechAnalyst {
	private TechAnalyzerManager analyzerManager;
	private TWSEDataManager twseDataManager;
	
	
	private TechAnalyst() {
		// 要分析哪一檔商品  時間  哪種分析方式
		// 檢查檔案 分析 回傳結果?
		
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		Logger logger = LogManager.getLogger(TechAnalyst.class);
		
		analyzerManager = new TechAnalyzerManager();
		twseDataManager = new TWSEDataManager("./file/");
		
		String sFrom = "20160301";
		String sTo = "20160601";
		String sProdId = "2453";
		String sAnalyzer = MovingAvg5Day.class.getSimpleName();
		
		try {
			twseDataManager.checkDateAndDownload(sFrom, sTo, sProdId);
		} catch (ParseException | IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		List<AnalysisResult> lstResult = analyzerManager.analyze(twseDataManager, sAnalyzer, sFrom, sTo, sProdId);
		for(AnalysisResult result : lstResult) {
			System.out.println(result.getDate() + ":" + result.getValue());
		}
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
