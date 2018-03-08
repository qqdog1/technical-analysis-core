package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.analyzer.impl.ABI;
import name.qd.techAnalyst.analyzer.impl.ABIAdvance;
import name.qd.techAnalyst.analyzer.impl.ABIDecline;
import name.qd.techAnalyst.analyzer.impl.ADL;
import name.qd.techAnalyst.analyzer.impl.price.AveragePrice;
import name.qd.techAnalyst.analyzer.impl.price.ClosePrice;

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
			break;
		case ABIAdvance:
			map.put(analyzer, new ABIAdvance());
			break;
		case ABIDecline:
			map.put(analyzer, new ABIDecline());
			break;
		case ADL:
			map.put(analyzer, new ADL());
		case AVERAGE_PRICE:
			map.put(analyzer, new AveragePrice());
			break;
		case CLOSE_PRICE:
			map.put(analyzer, new ClosePrice());
			break;
		}
	}
}
