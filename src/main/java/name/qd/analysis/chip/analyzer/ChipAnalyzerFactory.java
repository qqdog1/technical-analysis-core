package name.qd.analysis.chip.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.analysis.chip.ChipAnalyzers;
import name.qd.analysis.chip.analyzer.impl.BackTestPattern1;
import name.qd.analysis.chip.analyzer.impl.BigVolumeTrade;
import name.qd.analysis.chip.analyzer.impl.BranchInfoByBS;
import name.qd.analysis.chip.analyzer.impl.DailyOpen;
import name.qd.analysis.chip.analyzer.impl.DailyOpenMClose;
import name.qd.analysis.chip.analyzer.impl.DailyPnl;
import name.qd.analysis.chip.analyzer.impl.DailyTradeRecord;
import name.qd.analysis.chip.analyzer.impl.TotalPnl;

public class ChipAnalyzerFactory {
	private Map<ChipAnalyzers, ChipAnalyzer> map = new HashMap<>();
	
	public ChipAnalyzerFactory() {
	}
	
	public ChipAnalyzer getAnalyzer(ChipAnalyzers analyzer) {
		if(!map.containsKey(analyzer)) {
			createAnalyzer(analyzer);
		}
		return map.get(analyzer);
	}
	
	private void createAnalyzer(ChipAnalyzers analyzer) {
		switch(analyzer) {
		case DAILY_PNL:
			map.put(analyzer, new DailyPnl());
			break;
		case TOTAL_PNL:
			map.put(analyzer, new TotalPnl());
			break;
		case DAILY_TRADE_RECORD:
			map.put(analyzer, new DailyTradeRecord());
			break;
		case DAILY_OPEN:
			map.put(analyzer, new DailyOpen());
			break;
		case DAILY_OPEN_M_CLOSE:
			map.put(analyzer, new DailyOpenMClose());
			break;
		case BACK_TEST_PATTERN_1:
			map.put(analyzer, new BackTestPattern1());
			break;
		case BIG_VOLUME_TRADE:
			map.put(analyzer, new BigVolumeTrade());
			break;
		case BRANCH_INFO:
			map.put(analyzer, new BranchInfoByBS());
			break;
		}
	}
}
