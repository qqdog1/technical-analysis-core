package name.qd.analysis.tech.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.StringCombineUtil;

/**
 * 累積派發線
 * (收-低) - (高-收) / (高-低) * 成交量  累加
 */
public class ADL implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ADL.class);

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(ADL.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		List<AnalysisResult> lstResult = null;
		try {
			List<ProductClosingInfo> lstProduct = dataManager.getProductClosingInfo(product, from, to);
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
				lst.add(result);
			}
			
			lstResult = accu(lst);
		} catch (Exception e) {
			log.error("Analyze ADL failed.", e);
			throw e;
		}
		return lstResult;
	}
	
	private List<AnalysisResult> accu(List<AnalysisResult> lst) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		AnalysisResult lastResult = new AnalysisResult();
		lastResult.setValue(0);
		for(AnalysisResult result : lst) {
			AnalysisResult analysisResult = new AnalysisResult();
			analysisResult.setDate(result.getDate());
			analysisResult.setValue(lastResult.getValue().get(0)+result.getValue().get(0));
			lstResult.add(analysisResult);
			lastResult = analysisResult;
		}
		return lstResult;
	}
	
	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String ... inputs) throws Exception {
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
