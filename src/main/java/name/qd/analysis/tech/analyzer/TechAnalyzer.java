package name.qd.analysis.tech.analyzer;

import java.util.Date;
import java.util.List;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.vo.AnalysisResult;

public abstract class TechAnalyzer {
	protected TechAnalyzerManager techAnalyzerManager;
	
	public TechAnalyzer(TechAnalyzerManager techAnalyzerManager) {
		this.techAnalyzerManager = techAnalyzerManager;
	}
	
	public abstract String getCacheName(String product);
	public abstract List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception;
	public abstract List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String ... inputs) throws Exception;
	public abstract List<String> getCustomDescreption();
	public abstract AnalyzerType getAnalyzerType();
}
