package name.qd.analysis.tech.analyzer.impl.A;

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
 * 今高 - 今低
 * 絕對值(今高 - 昨收)
 * 絕對值(昨收 - 今低)
 * 上面三者取最大
 */
public class ATR implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ATR.class);
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(ATR.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProducts = dataSource.getProductClosingInfo(product, from, to);
			ProductClosingInfo lastInfo = null;
			for(ProductClosingInfo info : lstProducts) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				double hlRange = info.getUpperPrice() - info.getLowerPrice();
				double chRange = 0;
				double clRange = 0;
				if(lastInfo != null) {
					chRange = Math.abs(info.getUpperPrice() - lastInfo.getClosePrice());
					clRange = Math.abs(info.getLowerPrice() - lastInfo.getClosePrice());
				}
				double max = Math.max(hlRange, chRange);
				max = Math.max(max, clRange);
				result.setValue(max);
				lst.add(result);
				lastInfo = info;
			}
		} catch (Exception e) {
			log.error("ATR analyze failed.", e);
			throw e;
		}
		return lst;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.ATR, product, from, to);
		return AnalystUtils.simpleMovingAverageByResult(lst, ma);
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
