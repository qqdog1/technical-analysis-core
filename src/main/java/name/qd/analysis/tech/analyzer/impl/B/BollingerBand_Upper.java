package name.qd.analysis.tech.analyzer.impl.B;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class BollingerBand_Upper implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BollingerBand_Upper.class);

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(BollingerBand_Upper.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
		throw new Exception("Must enter days.");
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		int ma = Integer.parseInt(inputs[0]);
		int sd = 1;
		try {
			sd = Integer.parseInt(inputs[1]);
		} catch(NumberFormatException e) {
		}
		List<AnalysisResult> lstMiddle = TechAnalyzerManager.getInstance().getCustomAnalysisResult(dataManager, TechAnalyzers.BollingerBand_Middle, product, from, to, inputs);
		Map<Date, AnalysisResult> mapMiddle = new HashMap<>();
		for(AnalysisResult middle : lstMiddle) {
			mapMiddle.put(middle.getDate(), middle);
		}
		
		List<AnalysisResult> lstProduct = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProductInfo = dataManager.getProductClosingInfo(product, from, to);
			for(ProductClosingInfo prodInfo : lstProductInfo) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(prodInfo.getDate());
				result.setValue(prodInfo.getClosePrice());
				lstProduct.add(result);
			}
		} catch (Exception e) {
			log.error("Get product closing info failed.", e);
			throw e;
		}
		
		List<AnalysisResult> lstSD = AnalystUtils.NDayStandardDeviation(lstProduct, ma);

		for(AnalysisResult result : lstSD) {
			if(mapMiddle.containsKey(result.getDate())) {
				AnalysisResult finalResult = new AnalysisResult();
				double middle = mapMiddle.get(result.getDate()).getValue().get(0);
				double nsd = result.getValue().get(0) * sd;
				finalResult.setValue(middle + nsd);
				finalResult.setDate(result.getDate());
				lstResult.add(finalResult);
			}
		}
		return lstResult;
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("MA:");
		lst.add("SD Multi:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
