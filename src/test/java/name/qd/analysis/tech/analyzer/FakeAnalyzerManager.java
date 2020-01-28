package name.qd.analysis.tech.analyzer;

import java.util.Date;
import java.util.List;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.vo.AnalysisResult;

public class FakeAnalyzerManager extends TechAnalyzerManager {

	public FakeAnalyzerManager() {
		super("");
	}

	@Override
	public List<AnalysisResult> getAnalysisResult(DataSource dataSource, TechAnalyzers analyzer, String product, Date from, Date to) throws Exception {
		TechAnalyzer techAnalyzer = super.techAnalyzerFactory.getAnalyzer(analyzer, this);
		if(techAnalyzer == null) {
			return null;
		}
		return techAnalyzer.analyze(dataSource, product, from, to);
	}
}
