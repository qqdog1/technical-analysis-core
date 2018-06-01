package name.qd.analysis.tech.analyzer.impl.C;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.StringCombineUtil;

public class CCI implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(CCI.class);

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(CCI.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		throw new Exception("Must enter days.");
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		List<AnalysisResult> lstTypicalPrice = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, TechAnalyzers.TYPICAL_PRICE, product, from, to);
		int days = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lstTypicalMA = AnalystUtils.simpleMovingAverageByResult(lstTypicalPrice, days);
		
		for(int i = 0 ; i < lstTypicalMA.size(); i++) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lstTypicalMA.get(i).getDate());
			double typicalMA = lstTypicalMA.get(i).getValue().get(0);
			double sum = 0;
			for(int j = 0 ; j < days ; j++) {
				double typical = lstTypicalPrice.get(i+j).getValue().get(0);
				sum += Math.abs(typicalMA - typical);
			}
			double d = sum * 0.003;
			double m = typicalMA - lstTypicalPrice.get(i+days-1).getValue().get(0);
			result.setValue(m/d);
			lstResult.add(result);
		}
		return lstResult;
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("N days:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
