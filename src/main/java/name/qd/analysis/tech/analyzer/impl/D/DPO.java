package name.qd.analysis.tech.analyzer.impl.D;

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

// The Detrended Price Oscillator
public class DPO implements TechAnalyzer {

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(DPO.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		throw new Exception("Must enter days.");
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		List<AnalysisResult> lstClosePrice = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, TechAnalyzers.CLOSE_PRICE, product, from, to);
		int days = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lstSMA = AnalystUtils.simpleMovingAverageByResult(lstClosePrice, days);
		int index = days - (days/2 + 1) - 1;
		System.out.println("Index:" + index);
		for(int i = 0 ; i < lstSMA.size() ; i++) {
			AnalysisResult result = new AnalysisResult();
			AnalysisResult closePrice = lstClosePrice.get(i+index);
			AnalysisResult sma = lstSMA.get(i);
			System.out.println("ClosePrice:" + closePrice.getDate() + ":" + closePrice.getValue().get(0));
			System.out.println("SMA:" + sma.getDate() + ":" + sma.getValue().get(0));
			result.setDate(closePrice.getDate());
			result.setValue(closePrice.getValue().get(0) - sma.getValue().get(0));
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
