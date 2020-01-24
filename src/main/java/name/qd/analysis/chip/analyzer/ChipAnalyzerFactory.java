package name.qd.analysis.chip.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.analysis.chip.ChipAnalyzers;
import name.qd.analysis.chip.analyzer.impl.BackTestPattern1;
import name.qd.analysis.chip.analyzer.impl.BestBranchBuy;
import name.qd.analysis.chip.analyzer.impl.BigVolumeTrade;
import name.qd.analysis.chip.analyzer.impl.BranchInfoByBS;
import name.qd.analysis.chip.analyzer.impl.BranchInfoByCache;
import name.qd.analysis.chip.analyzer.impl.DailyOpen;
import name.qd.analysis.chip.analyzer.impl.DailyOpenMClose;
import name.qd.analysis.chip.analyzer.impl.DailyPnl;
import name.qd.analysis.chip.analyzer.impl.DailyTradeRecord;
import name.qd.analysis.chip.analyzer.impl.TotalPnl;

public class ChipAnalyzerFactory {
	private Map<ChipAnalyzers, ChipAnalyzer> map = new HashMap<>();
	
	public ChipAnalyzerFactory() {
	}
	
	public ChipAnalyzer getAnalyzer(ChipAnalyzers analyzer, ChipAnalyzerManager chipAnalyzerManager) {
		if(!map.containsKey(analyzer)) {
			createAnalyzer(analyzer, chipAnalyzerManager);
		}
		return map.get(analyzer);
	}
	
	private void createAnalyzer(ChipAnalyzers analyzer, ChipAnalyzerManager chipAnalyzerManager) {
		switch(analyzer) {
		case DAILY_PNL:
			map.put(analyzer, new DailyPnl(chipAnalyzerManager));
			break;
		case TOTAL_PNL:
			map.put(analyzer, new TotalPnl(chipAnalyzerManager));
			break;
		case DAILY_TRADE_RECORD:
			map.put(analyzer, new DailyTradeRecord(chipAnalyzerManager));
			break;
		case DAILY_OPEN:
			map.put(analyzer, new DailyOpen(chipAnalyzerManager));
			break;
		case DAILY_OPEN_M_CLOSE:
			map.put(analyzer, new DailyOpenMClose(chipAnalyzerManager));
			break;
		case BACK_TEST_PATTERN_1:
			map.put(analyzer, new BackTestPattern1(chipAnalyzerManager));
			break;
		case BIG_VOLUME_TRADE:
			map.put(analyzer, new BigVolumeTrade(chipAnalyzerManager));
			break;
		case BRANCH_INFO_BY_BS:
			map.put(analyzer, new BranchInfoByBS(chipAnalyzerManager));
			break;
		case BRANCH_INFO_BY_CACHE:
			map.put(analyzer, new BranchInfoByCache(chipAnalyzerManager));
			break;
		case BEST_BRANCH_BUY:
			map.put(analyzer, new BestBranchBuy(chipAnalyzerManager));
			break;
		default:
			break;
		}
	}
}
