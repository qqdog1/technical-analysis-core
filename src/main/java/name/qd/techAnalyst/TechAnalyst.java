package name.qd.techAnalyst;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
		
		analyzerManager = TechAnalyzerManager.getInstance();
		twseDataManager = new TWSEDataManager("./");
		
		String sFrom = "20160331";
		String sTo = "20160501";
		String sProdId = "2453";
		String sAnalyzer = MovingAvg5Day.class.getSimpleName();
		
		try {
			twseDataManager.checkDateAndDownload(sFrom, sTo, sProdId);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		List<ProdClosingInfo> lstProdClosingInfo = null;
		try {
			lstProdClosingInfo = twseDataManager.getProdClosingInfo(sFrom, sTo, sProdId);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		List<DailyClosingInfo> lstDailyClosingInfo = null;
		try {
			lstDailyClosingInfo = twseDataManager.getDailyClosingInfo(sFrom, sTo);
			
			for(DailyClosingInfo dailyClosingInfo : lstDailyClosingInfo) {
				System.out.println(dailyClosingInfo.getDate() + ":" + dailyClosingInfo.getAdvance() + ":" + dailyClosingInfo.getDecline());
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		List<AnalysisResult> lstResult = analyzerManager.analyze(sAnalyzer, sFrom, sTo, lstProdClosingInfo);
		for(AnalysisResult result : lstResult) {
			System.out.println(result.getDate() + ":" + result.getValue());
		}
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
