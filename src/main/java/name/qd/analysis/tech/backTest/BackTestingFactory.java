package name.qd.analysis.tech.backTest;

import java.util.HashMap;
import java.util.Map;

import name.qd.analysis.tech.Analyzer;
import name.qd.analysis.tech.backTest.impl.ABITesting;

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
