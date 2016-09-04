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
			logger.info("Created analyzer instance: " + className);
		}
	}
	
	public List<AnalysisResult> analyze(TWSEDataManager dataManager, String analyzer, String from, String to, String prodId, Object ...customizeObjs) {
		if(!map.containsKey(analyzer)) {
			return null;
		}
		
		if(!techAnalystCacheManager.isDateInRange(analyzer, from, to)) {
			updateCache(dataManager, analyzer, from, to, prodId, customizeObjs);
		}
		
		return techAnalystCacheManager.getAnalysisResult(analyzer, from, to);
	}
	
	private void updateCache(TWSEDataManager dataManager, String analyzer, String from, String to, String prodId, Object ...customizeObjs) {
		String first = techAnalystCacheManager.getFirstDateString(analyzer, from);
		String last = techAnalystCacheManager.getLastDateString(analyzer, to);
		List<AnalysisResult> lst = map.get(analyzer).analyze(dataManager, first, last, prodId, customizeObjs);
		techAnalystCacheManager.putAnalysisResult(analyzer, lst);
		techAnalystCacheManager.syncFile(analyzer);
	}
}