package name.qd.analysis.tech.analyzer.impl.C;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.StringCombineUtil;

/**
 * Chaikin Money Flow
 *
 */
public class ChaikinMoneyFlow implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ChaikinMoneyFlow.class);

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(ChaikinMoneyFlow.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProduct = dataSource.getProductClosingInfo(product, from, to);
			for(ProductClosingInfo info : lstProduct) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				double maxRange = (info.getUpperPrice()-info.getLowerPrice());
				double d;
				if(maxRange == 0) {
					d = 0;
				} else {
					d = (info.getClosePrice()-info.getLowerPrice())-(info.getUpperPrice()-info.getClosePrice())/(maxRange);
				}
				d = d * info.getFilledShare();
				result.setValue(d);
				result.setValue(info.getFilledShare());
				lst.add(result);
			}
		} catch (Exception e) {
			log.error("Analyze ChaikinMoneyFlow failed.", e);
			throw e;
		}
		return lst;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, TechAnalyzers.ChaikinMoneyFlow, product, from, to);
		int n = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lstResult = AnalystUtils.NDaysAccu(lst, n);
		for(AnalysisResult analysisResult : lstResult) {
			List<Double> lstValue = new ArrayList<>();
			double value = analysisResult.getValue().get(0)/analysisResult.getValue().get(1);
			lstValue.add(value);
			analysisResult.setValue(lstValue);
		}
		return lstResult;
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("N Days:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
