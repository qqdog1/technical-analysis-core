package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.analyzer.impl.ABI;
import name.qd.techAnalyst.analyzer.impl.ABIAdvance;
import name.qd.techAnalyst.analyzer.impl.ABIDecline;
import name.qd.techAnalyst.analyzer.impl.AD;
import name.qd.techAnalyst.analyzer.impl.ADL;
import name.qd.techAnalyst.analyzer.impl.AD_Issues;
import name.qd.techAnalyst.analyzer.impl.AD_Ratio;
import name.qd.techAnalyst.analyzer.impl.AdvancingVolume;
import name.qd.techAnalyst.analyzer.impl.ArmsIndex;
import name.qd.techAnalyst.analyzer.impl.DecliningVolume;
import name.qd.techAnalyst.analyzer.impl.SI;
import name.qd.techAnalyst.analyzer.impl.UnchangedVolume;
import name.qd.techAnalyst.analyzer.impl.price.AveragePrice;
import name.qd.techAnalyst.analyzer.impl.price.ClosePrice;

public class TechAnalyzerFactory {
	private Map<Analyzer, TechAnalyzer> map = new HashMap<>();
	
	public TechAnalyzerFactory() {
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
			break;
		case AD:
			map.put(analyzer, new AD());
			break;
		case AD_Ratio:
			map.put(analyzer, new AD_Ratio());
			break;
		case AD_Issues:
			map.put(analyzer, new AD_Issues());
			break;
		case SI:
			map.put(analyzer, new SI());
			break;
		case AdvancingVolume:
			map.put(analyzer, new AdvancingVolume());
			break;
		case DecliningVolume:
			map.put(analyzer, new DecliningVolume());
			break;
		case UnchangedVolume:
			map.put(analyzer, new UnchangedVolume());
			break;
		case ArmsIndex:
			map.put(analyzer, new ArmsIndex());
			break;
		case AVERAGE_PRICE:
			map.put(analyzer, new AveragePrice());
			break;
		case CLOSE_PRICE:
			map.put(analyzer, new ClosePrice());
			break;
		}
	}
}
