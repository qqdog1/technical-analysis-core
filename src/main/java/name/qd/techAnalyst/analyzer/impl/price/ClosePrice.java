package name.qd.techAnalyst.analyzer.impl.price;

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

public class ClosePrice implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ClosePrice.class);
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(ClosePrice.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProductInfo = dataManager.getProductClosingInfo(product, from, to);
			for(ProductClosingInfo prodInfo : lstProductInfo) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(prodInfo.getDate());
				result.setValue(prodInfo.getClosePrice());
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("Get product closing info failed.", e);
		}
		return lstResult;
	}
	
	@Override
	public List<AnalysisResult> customResult(List<AnalysisResult> lst, String ... inputs) {
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
