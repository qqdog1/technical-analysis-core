package name.qd.analysis.tech.backTest;

import java.util.HashMap;
import java.util.Map;

import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.backTest.impl.ABITesting;

public class BackTestingFactory {
	private Map<TechAnalyzers, BackTesting> map = new HashMap<>();
	
	public BackTestingFactory() {
	}
	
	public BackTesting getVerifier(TechAnalyzers analyzer, TechAnalyzerManager techAnalyzerManager) {
		if(!map.containsKey(analyzer)) {
			createVerifier(analyzer, techAnalyzerManager);
		}
		return map.get(analyzer);
	}
	
	private void createVerifier(TechAnalyzers analyzer, TechAnalyzerManager techAnalyzerManager) {
		switch(analyzer) {
		case ABI:
			map.put(analyzer, new ABITesting(techAnalyzerManager));
			break;
		}
	}
}
