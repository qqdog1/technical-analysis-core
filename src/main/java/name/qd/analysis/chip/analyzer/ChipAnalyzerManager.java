package name.qd.analysis.chip.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChipAnalyzerManager {
	private static Logger log = LoggerFactory.getLogger(ChipAnalyzerManager.class);
	
	private MostEffective mostEffective;

	public ChipAnalyzerManager() {
		mostEffective = new MostEffective();
	}
}
