package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.analyzer.impl.MovingAvg10Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg120Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg20Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg240Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg5Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg60Day;
import name.qd.techAnalyst.dataSource.TWSEDataManager;
import name.qd.techAnalyst.fileCache.TechAnalystCacheManager;
import name.qd.techAnalyst.vo.AnalysisResult;

public class TechAnalyzerManager {
	private Map<String, ITechAnalyzer> map = new HashMap<String, ITechAnalyzer>();
	private TechAnalystCacheManager techAnalystCacheManager = new TechAnalystCacheManager();
	private Logger logger = LogManager.getLogger(TechAnalyzerManager.class);

	public TechAnalyzerManager() {
		map.put(MovingAvg5Day.class.getSimpleName(), new MovingAvg5Day());
		map.put(MovingAvg10Day.class.getSimpleName(), new MovingAvg10Day());
		map.put(MovingAvg20Day.class.getSimpleName(), new MovingAvg20Day());
		map.put(MovingAvg60Day.class.getSimpleName(), new MovingAvg60Day());
		map.put(MovingAvg120Day.class.getSimpleName(), new MovingAvg120Day());
		map.put(MovingAvg240Day.class.getSimpleName(), new MovingAvg240Day());
		
		for(String className : map.keySet()) {
			logger.info("Init Analyzer: " + className);
		}
	}
	
	public List<AnalysisResult> analyze(TWSEDataManager dataManager, String sAnalyzer, String sFrom, String sTo, String sProd) {
		if(!map.containsKey(sAnalyzer)) {
			return null;
		}
		
		if(!techAnalystCacheManager.isDateInRange(sAnalyzer, sFrom, sTo)) {
			updateCache(dataManager, sAnalyzer, sFrom, sTo, sProd);
		}
		
		return techAnalystCacheManager.getAnalysisResult(sAnalyzer, sFrom, sTo);
	}
	
	private void updateCache(TWSEDataManager dataManager, String sAnalyzer, String sFrom, String sTo, String sProd) {
		String sFirst = techAnalystCacheManager.getFirstDateString(sAnalyzer, sFrom);
		String sLast = techAnalystCacheManager.getLastDateString(sAnalyzer, sTo);
		List<AnalysisResult> lst = map.get(sAnalyzer).analyze(dataManager, sFirst, sLast, sProd);
		techAnalystCacheManager.putAnalysisResult(sAnalyzer, lst);
		techAnalystCacheManager.syncFile(sAnalyzer);
	}
}