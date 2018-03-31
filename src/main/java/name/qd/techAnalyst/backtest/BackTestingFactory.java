package name.qd.techAnalyst.backtest;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.backtest.impl.ABITesting;

public class BackTestingFactory {
	private Map<Analyzer, BackTesting> map = new HashMap<>();
	
	public BackTestingFactory() {
	}
	
	public BackTesting getVerifier(Analyzer analyzer) {
		if(!map.containsKey(analyzer)) {
			createVerifier(analyzer);
		}
		return map.get(analyzer);
	}
	
	private void createVerifier(Analyzer analyzer) {
		switch(analyzer) {
		case ABI:
			map.put(analyzer, new ABITesting());
			break;
		}
	}
}
