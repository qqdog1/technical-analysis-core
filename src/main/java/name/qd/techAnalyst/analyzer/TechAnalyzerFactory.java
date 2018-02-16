package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.analyzer.impl.ABI;

public class TechAnalyzerFactory {
	private static TechAnalyzerFactory instance = new TechAnalyzerFactory();
	private Map<Analyzer, TechAnalyzer> map = new HashMap<>();
	
	private TechAnalyzerFactory() {
	}
	
	public static TechAnalyzerFactory getInstance() {
		return instance;
	}
	
	public TechAnalyzer getAnalyzer(Analyzer analyzer) {
		if(!map.containsKey(analyzer)) {
			createAnalyzer(analyzer);
		}
		return map.get(analyzer);
	}
	
	private void createAnalyzer(Analyzer analyzer) {
		switch(analyzer) {
		case ABI:
			map.put(analyzer, new ABI());
		}
	}
}
