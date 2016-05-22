package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.analyzer.impl.DayAvg5;

public class TechAnalyzerManager {
	private Map<String, ITechAnalyzer> map = new HashMap<String, ITechAnalyzer>();

	private static TechAnalyzerManager instance = new TechAnalyzerManager();
	
	public static TechAnalyzerManager getInstance() {
		return instance;
	}
	
	private TechAnalyzerManager() {
		map.put("DayAvg5", new DayAvg5());
	}
	
	public void analyze(String sAnalyzer, String sFrom, String sTo, String sProdId) {
		if(!map.containsKey(sAnalyzer)) {
			// TODO
			return;
		}
		
		map.get(sAnalyzer).analyze(sFrom, sTo, sProdId);
	}
	
}
