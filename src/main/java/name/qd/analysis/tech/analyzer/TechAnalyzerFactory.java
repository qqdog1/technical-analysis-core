package name.qd.analysis.tech.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.analysis.tech.Analyzer;
import name.qd.analysis.tech.analyzer.impl.ABI;
import name.qd.analysis.tech.analyzer.impl.ABIAdvance;
import name.qd.analysis.tech.analyzer.impl.ABIDecline;
import name.qd.analysis.tech.analyzer.impl.AD;
import name.qd.analysis.tech.analyzer.impl.ADL;
import name.qd.analysis.tech.analyzer.impl.AD_Issues;
import name.qd.analysis.tech.analyzer.impl.AD_Ratio;
import name.qd.analysis.tech.analyzer.impl.AdvancingVolume;
import name.qd.analysis.tech.analyzer.impl.ArmsIndex;
import name.qd.analysis.tech.analyzer.impl.Aroon_DOWN;
import name.qd.analysis.tech.analyzer.impl.Aroon_UP;
import name.qd.analysis.tech.analyzer.impl.DecliningVolume;
import name.qd.analysis.tech.analyzer.impl.SI;
import name.qd.analysis.tech.analyzer.impl.UnchangedVolume;
import name.qd.analysis.tech.analyzer.impl.price.AveragePrice;
import name.qd.analysis.tech.analyzer.impl.price.ClosePrice;
import name.qd.analysis.tech.analyzer.impl.price.LowerPrice;
import name.qd.analysis.tech.analyzer.impl.price.OpenPrice;
import name.qd.analysis.tech.analyzer.impl.price.UpperPrice;

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
		
		case ArmsIndex:
			map.put(analyzer, new ArmsIndex());
			break;
		case Aroon_UP:
			map.put(analyzer, new Aroon_UP());
			break;
		case Aroon_DOWN:
			map.put(analyzer, new Aroon_DOWN());
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
		case AVERAGE_PRICE:
			map.put(analyzer, new AveragePrice());
			break;
		case CLOSE_PRICE:
			map.put(analyzer, new ClosePrice());
			break;
		case OPEN_PRICE:
			map.put(analyzer, new OpenPrice());
			break;
		case UPPER_PRICE:
			map.put(analyzer, new UpperPrice());
			break;
		case LOWER_PRICE:
			map.put(analyzer, new LowerPrice());
			break;
		}
	}
}
