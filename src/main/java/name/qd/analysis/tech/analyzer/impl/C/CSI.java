package name.qd.analysis.tech.analyzer.impl.C;

import java.util.Date;
import java.util.List;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;

public class CSI extends TechAnalyzer {

	public CSI(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return null;
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		return null;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		return null;
	}

	@Override
	public List<String> getCustomDescreption() {
		return null;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
