package name.qd.analysis.tech.analyzer.impl.B;

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
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.StringCombineUtil;

public class BollingerBand_Middle implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BollingerBand_Middle.class);
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(BollingerBand_Middle.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		throw new Exception("Must enter days.");
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProductInfo = dataSource.getProductClosingInfo(product, from, to);
			for(ProductClosingInfo prodInfo : lstProductInfo) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(prodInfo.getDate());
				result.setValue(prodInfo.getClosePrice());
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("Get product closing info failed.", e);
			throw e;
		}
		
		int ma = Integer.parseInt(inputs[0]);
		return AnalystUtils.simpleMovingAverageByResult(lstResult, ma);
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Simple-MA:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
