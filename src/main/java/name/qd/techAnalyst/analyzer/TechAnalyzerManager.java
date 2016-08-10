package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.qd.techAnalyst.analyzer.impl.MovingAvg10Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg120Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg20Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg240Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg5Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg60Day;
import name.qd.techAnalyst.dataSource.TWSEDataManager;
import name.qd.techAnalyst.vo.AnalysisResult;

public class TechAnalyzerManager {
	private Map<String, ITechAnalyzer> map = new HashMap<String, ITechAnalyzer>();

	private static TechAnalyzerManager instance = new TechAnalyzerManager();
	
	public static TechAnalyzerManager getInstance() {
		return instance;
	}
	
	private TechAnalyzerManager() {
		map.put(MovingAvg5Day.class.getSimpleName(), new MovingAvg5Day());
		map.put(MovingAvg10Day.class.getSimpleName(), new MovingAvg10Day());
		map.put(MovingAvg20Day.class.getSimpleName(), new MovingAvg20Day());
		map.put(MovingAvg60Day.class.getSimpleName(), new MovingAvg60Day());
		map.put(MovingAvg120Day.class.getSimpleName(), new MovingAvg120Day());
		map.put(MovingAvg240Day.class.getSimpleName(), new MovingAvg240Day());
	}
	
	public List<AnalysisResult> analyze(TWSEDataManager dataManager, String sAnalyzer, String sFrom, String sTo, String sProd) {
		if(!map.containsKey(sAnalyzer)) {
			return null;
		}
		return map.get(sAnalyzer).analyze(dataManager, sFrom, sTo, sProd);
	}
}