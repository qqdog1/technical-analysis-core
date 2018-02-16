package name.qd.techAnalyst.winPercent;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.winPercent.impl.ABIVerify;

public class WPVerifierFactory {
	private static WPVerifierFactory instance = new WPVerifierFactory();
	private Map<Analyzer, WPVerifier> map = new HashMap<>();
	
	private WPVerifierFactory() {
	}
	
	public static WPVerifierFactory getInstance() {
		return instance;
	}
	
	public WPVerifier getVerifier(Analyzer analyzer) {
		if(!map.containsKey(analyzer)) {
			createVerifier(analyzer);
		}
		return map.get(analyzer);
	}
	
	private void createVerifier(Analyzer analyzer) {
		switch(analyzer) {
		case ABI:
			map.put(analyzer, new ABIVerify());
		}
	}
}
