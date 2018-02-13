package name.qd.techAnalyst.analyzer;

import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.dataSource.DataManager;
import name.qd.techAnalyst.vo.AnalysisResult;

public class ABI implements TechAnalyzer {

	@Override
	public String getCacheName(String product) {
		return ABI.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataManager dataManager, String product, Date from, Date to) {
		return null;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.MARKET;
	}
}
