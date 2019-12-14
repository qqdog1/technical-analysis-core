package name.qd.analysis.tech.analyzer.impl.ma;

import java.util.ArrayList;
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

public class SimpleMovingAverage extends TechAnalyzer {

	public SimpleMovingAverage(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(SimpleMovingAverage.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		throw new Exception("Must input parameters.");
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		String priceType = inputs[0].toUpperCase();
		int days = Integer.parseInt(inputs[1]);
		List<AnalysisResult> lstResult = null;
		switch(priceType) {
		case "H":
			lstResult = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.UPPER_PRICE, product, from, to);
			break;
		case "L":
			lstResult = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.LOWER_PRICE, product, from, to);
			break;
		case "O":
			lstResult = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.OPEN_PRICE, product, from, to);
			break;
		case "C":
			lstResult = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.CLOSE_PRICE, product, from, to);
			break;
		case "A":
			lstResult = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.AVERAGE_PRICE, product, from, to);
			break;
		}
		return AnalystUtils.simpleMovingAverageByResult(lstResult, days);
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("H/L/O/C/A:");
		lst.add("MA days:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
