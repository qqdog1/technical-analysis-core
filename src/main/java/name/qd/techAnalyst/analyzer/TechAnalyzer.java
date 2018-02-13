package name.qd.techAnalyst.analyzer;

import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.dataSource.DataManager;
import name.qd.techAnalyst.vo.AnalysisResult;

public interface TechAnalyzer {
	public String getCacheName(String product);
	public List<AnalysisResult> analyze(DataManager dataManager, String product, Date from, Date to);
}
