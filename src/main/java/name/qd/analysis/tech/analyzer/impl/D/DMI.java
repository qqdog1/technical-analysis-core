package name.qd.analysis.tech.analyzer.impl.D;

import java.util.Date;
import java.util.List;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.StringCombineUtil;

public class DMI extends TechAnalyzer {
	
	public DMI(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(DMI.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstAvgPrice = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.AVERAGE_PRICE, product, from, to);
		List<AnalysisResult> lstSD = AnalystUtils.NDayStandardDeviation(lstAvgPrice, 5);
		List<AnalysisResult> lstSDMA = AnalystUtils.simpleMovingAverageByResult(lstSD, 10);
		
		
		
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
