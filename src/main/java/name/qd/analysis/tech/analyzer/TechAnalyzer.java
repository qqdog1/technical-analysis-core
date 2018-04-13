package name.qd.analysis.tech.analyzer;

import java.util.Date;
import java.util.List;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.vo.AnalysisResult;

public interface TechAnalyzer {
	public String getCacheName(String product);
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception;
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String ... inputs) throws Exception;
	public List<String> getCustomDescreption();
	public AnalyzerType getAnalyzerType();
}
