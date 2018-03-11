package name.qd.techAnalyst.backtest;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.backtest.impl.ABIAdvanceVerify;
import name.qd.techAnalyst.backtest.impl.ABIDeclineVerify;
import name.qd.techAnalyst.backtest.impl.ABIVerify;
import name.qd.techAnalyst.backtest.impl.ADLVerify;

public class WPVerifierFactory {
	private static WPVerifierFactory instance = new WPVerifierFactory();
	private Map<Analyzer, BackTesting> map = new HashMap<>();
	
	private WPVerifierFactory() {
	}
	
	public static WPVerifierFactory getInstance() {
		return instance;
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
			map.put(analyzer, new ABIVerify());
			break;
		case ABIAdvance:
			map.put(analyzer, new ABIAdvanceVerify());
			break;
		case ABIDecline:
			map.put(analyzer, new ABIDeclineVerify());
			break;
		case ADL:
			map.put(analyzer, new ADLVerify());
			break;
		}
	}
}
