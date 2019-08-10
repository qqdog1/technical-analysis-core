package name.qd.analysis.chip.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.analysis.chip.ChipAnalyzers;
import name.qd.analysis.chip.analyzer.impl.DailyPnl;

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
		}
	}
}
