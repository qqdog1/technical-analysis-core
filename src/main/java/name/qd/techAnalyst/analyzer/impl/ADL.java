package name.qd.techAnalyst.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.util.StringCombineUtil;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class ADL implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ADL.class);

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(ADL.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		List<AnalysisResult> lst = new ArrayList<>();
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
		} catch (Exception e) {
			log.error("Analyze ADL failed.", e);
		}
		return lst;
	}
	
	@Override
	public List<AnalysisResult> customResult(List<AnalysisResult> lst, String ... inputs) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		String accu = inputs[0];
		if(!"Y".equals(accu)) {
			return lst;
		}
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
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Accumulate(Y/N):");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
