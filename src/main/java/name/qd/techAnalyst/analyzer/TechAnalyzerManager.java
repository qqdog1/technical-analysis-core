package name.qd.techAnalyst.analyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.qd.techAnalyst.analyzer.impl.DayAvg5;
import name.qd.techAnalyst.util.TimeUtil;

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
		
		// check is data exist
		try {
			checkData(sFrom, sTo, sProdId);
		} catch (ParseException e) {
			// TODO
		}
		
		map.get(sAnalyzer).analyze(sFrom, sTo, sProdId);
	}
	
	private void checkData(String sFrom, String sTo, String sProdId) throws ParseException {
		
	}
}
