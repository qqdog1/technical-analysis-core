package name.qd.analysis.tech.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.impl.DecliningVolume;
import name.qd.analysis.tech.analyzer.impl.SI;
import name.qd.analysis.tech.analyzer.impl.UnchangedVolume;
import name.qd.analysis.tech.analyzer.impl.A.ABI;
import name.qd.analysis.tech.analyzer.impl.A.ABIAdvance;
import name.qd.analysis.tech.analyzer.impl.A.ABIDecline;
import name.qd.analysis.tech.analyzer.impl.A.AD;
import name.qd.analysis.tech.analyzer.impl.A.ADL;
import name.qd.analysis.tech.analyzer.impl.A.AD_Issues;
import name.qd.analysis.tech.analyzer.impl.A.AD_Ratio;
import name.qd.analysis.tech.analyzer.impl.A.ATR;
import name.qd.analysis.tech.analyzer.impl.A.AdvancingVolume;
import name.qd.analysis.tech.analyzer.impl.A.ArmsIndex;
import name.qd.analysis.tech.analyzer.impl.A.Aroon_DOWN;
import name.qd.analysis.tech.analyzer.impl.A.Aroon_UP;
import name.qd.analysis.tech.analyzer.impl.B.BollingerBand_Lower;
import name.qd.analysis.tech.analyzer.impl.B.BollingerBand_Middle;
import name.qd.analysis.tech.analyzer.impl.B.BollingerBand_Upper;
import name.qd.analysis.tech.analyzer.impl.B.BreadthThrust;
import name.qd.analysis.tech.analyzer.impl.C.CCI;
import name.qd.analysis.tech.analyzer.impl.C.CMO;
import name.qd.analysis.tech.analyzer.impl.C.ChaikinMoneyFlow;
import name.qd.analysis.tech.analyzer.impl.C.ChaikinOscillator;
import name.qd.analysis.tech.analyzer.impl.D.DPO;
import name.qd.analysis.tech.analyzer.impl.ma.DEMA;
import name.qd.analysis.tech.analyzer.impl.ma.ExponentialMovingAverage;
import name.qd.analysis.tech.analyzer.impl.ma.SimpleMovingAverage;
import name.qd.analysis.tech.analyzer.impl.ma.WilderSmoothing;
import name.qd.analysis.tech.analyzer.impl.price.AveragePrice;
import name.qd.analysis.tech.analyzer.impl.price.ClosePrice;
import name.qd.analysis.tech.analyzer.impl.price.LowerPrice;
import name.qd.analysis.tech.analyzer.impl.price.OpenPrice;
import name.qd.analysis.tech.analyzer.impl.price.TypicalPrice;
import name.qd.analysis.tech.analyzer.impl.price.UpperPrice;

public class TechAnalyzerFactory {
	private Map<TechAnalyzers, TechAnalyzer> map = new HashMap<>();
	
	public TechAnalyzerFactory() {
	}
	
	public TechAnalyzer getAnalyzer(TechAnalyzers analyzer) {
		if(!map.containsKey(analyzer)) {
			createAnalyzer(analyzer);
		}
		return map.get(analyzer);
	}
	
	private void createAnalyzer(TechAnalyzers analyzer) {
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
		case ATR:
			map.put(analyzer, new ATR());
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
		case TYPICAL_PRICE:
			map.put(analyzer, new TypicalPrice());
			break;
		case BollingerBand_Lower:
			map.put(analyzer, new BollingerBand_Lower());
			break;
		case BollingerBand_Middle:
			map.put(analyzer, new BollingerBand_Middle());
			break;
		case BollingerBand_Upper:
			map.put(analyzer, new BollingerBand_Upper());
			break;
		case BreadthThrust:
			map.put(analyzer, new BreadthThrust());
			break;
		case ChaikinMoneyFlow:
			map.put(analyzer, new ChaikinMoneyFlow());
			break;
		case ChaikinOscillator:
			map.put(analyzer, new ChaikinOscillator());
			break;
		case CMO:
			map.put(analyzer, new CMO());
			break;
		case CCI:
			map.put(analyzer, new CCI());
			break;
		case DPO:
			map.put(analyzer, new DPO());
			break;
		case SimpleMovingAverage:
			map.put(analyzer, new SimpleMovingAverage());
			break;
		case ExponentialMovingAverage:
			map.put(analyzer, new ExponentialMovingAverage());
			break;
		case DEMA:
			map.put(analyzer, new DEMA());
			break;
		case WilderSmoothing:
			map.put(analyzer, new WilderSmoothing());
			break;
		default:
			break;
		}
	}
}
