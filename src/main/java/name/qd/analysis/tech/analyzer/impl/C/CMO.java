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
import name.qd.analysis.utils.StringCombineUtil;

public class CMO extends TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(CMO.class);

	public CMO(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(CMO.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProductInfo = dataSource.getProductClosingInfo(product, from, to);
			ProductClosingInfo lastInfo = null;
			for(ProductClosingInfo info : lstProductInfo) {
				if(lastInfo != null) {
					AnalysisResult result = new AnalysisResult();
					result.setDate(info.getDate());
					double lastClose = lastInfo.getClosePrice();
					double thisClose = info.getClosePrice();
					if(lastClose < thisClose) {
						result.setValue(thisClose-lastClose);
						result.setValue(0);
					} else if(lastClose > thisClose) {
						result.setValue(0);
						result.setValue(lastClose-thisClose);
					} else {
						result.setValue(0);
						result.setValue(0);
					}
					lstResult.add(result);
				}
				lastInfo = info;
			}
		} catch(Exception e) {
			log.error("Analyze CMO failed.", e);
			throw e;
		}
		return lstResult;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		int days = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lstResult = new ArrayList<>();
		List<AnalysisResult> lstDaily = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.CMO, product, from, to);
		for(int i = lstDaily.size() - 1 ; i >= days - 1 ;  i--) {
			AnalysisResult result = new AnalysisResult();
			AnalysisResult dailyResult = lstDaily.get(i);
			result.setDate(dailyResult.getDate());
			double upperSum = 0;
			double lowerSum = 0;
			for(int j = 0 ; j < days ; j++) {
				upperSum += lstDaily.get(i-j).getValue().get(0);
				lowerSum += lstDaily.get(i-j).getValue().get(1);
			}
			double value = ((upperSum - lowerSum)/(upperSum + lowerSum))*100d;
			result.setValue(value);
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
