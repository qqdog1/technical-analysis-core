package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.qd.techAnalyst.analyzer.impl.DayAvg5;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TechAnalyzerManager {
	private Map<String, ITechAnalyzer> map = new HashMap<String, ITechAnalyzer>();

	private static TechAnalyzerManager instance = new TechAnalyzerManager();
	
	public static TechAnalyzerManager getInstance() {
		return instance;
	}
	
	private TechAnalyzerManager() {
		map.put("DayAvg5", new DayAvg5());
	}
	
	public List<AnalysisResult> analyze(String sAnalyzer, String sFrom, String sTo, List<ProdClosingInfo> lst) {
		if(!map.containsKey(sAnalyzer)) {
			// TODO
			return null;
		}
		
		return map.get(sAnalyzer).analyze(sFrom, sTo, lst);
	}
	
}
