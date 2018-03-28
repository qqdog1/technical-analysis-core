package name.qd.techAnalyst.analyzer;

import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;

public interface TechAnalyzer {
	public String getCacheName(String product);
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception;
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String ... inputs) throws Exception;
	public List<String> getCustomDescreption();
	public AnalyzerType getAnalyzerType();
}
