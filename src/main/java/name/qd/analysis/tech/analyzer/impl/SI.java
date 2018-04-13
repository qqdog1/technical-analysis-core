package name.qd.analysis.tech.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.Analyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.StringCombineUtil;

/**
 * $&*@(#$&(*#&($
 */
public class SI implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(SI.class);

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(SI.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProducts = dataManager.getProductClosingInfo(product, from, to);
			for(int i = 1 ; i < lstProducts.size() ; i++) {
				ProductClosingInfo lastInfo = lstProducts.get(i-1);
				ProductClosingInfo info = lstProducts.get(i);
				double a = Math.abs(info.getUpperPrice() - lastInfo.getClosePrice());
				double b = Math.abs(info.getLowerPrice() - lastInfo.getClosePrice());
				double c = Math.abs(info.getUpperPrice() - lastInfo.getLowerPrice());
				double d = Math.abs(lastInfo.getClosePrice() - lastInfo.getOpenPrice());
				double e = info.getClosePrice() - lastInfo.getClosePrice();
				double f = info.getClosePrice() - info.getOpenPrice();
				double g = lastInfo.getClosePrice() - lastInfo.getOpenPrice();
				double x = e + (f/2) + g;
				double k = Math.max(a, b);
				double r = 0;
				if(a >= b && a >= c) {
					r = a + (b/2) + (d/4);
				} else if(b >= a && b >= c) {
					r = b + (a/2) + (d/4);
				} else if(c >= a && c >= b) {
					r = c + (d/4);
				}
				double l = 3;
				double si = 50 * x / r * k / l;
				if(Double.isNaN(si)) {
					si = 0;
				}
				
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				result.setValue(si);
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("Analyze SI failed.", e);
			throw e;
		}
		return lstResult;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataManager, Analyzer.SI, product, from, to);
		String accu = inputs[0];
		if(!"Y".equalsIgnoreCase(accu)) {
			return lst;
		}
		return AnalystUtils.accu(lst);
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Accumulate(Y/N)=ASI:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
