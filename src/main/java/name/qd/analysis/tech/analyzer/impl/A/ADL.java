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
 * 累積派發線
 * (收-低) - (高-收) / (高-低) * 成交量  累加
 */
public class ADL extends TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ADL.class);

	public ADL(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(ADL.class.getSimpleName(), product);
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
				lst.add(result);
			}
		} catch (Exception e) {
			log.error("Analyze ADL failed.", e);
			throw e;
		}
		return lst;
	}
	
	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String ... inputs) throws Exception {
		List<AnalysisResult> lst = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.ADL, product, from, to);
		String accu = inputs[0];
		if(!"Y".equalsIgnoreCase(accu)) {
			return lst;
		}
		return AnalystUtils.accu(lst);
	}
	
	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Accumulate(Y/N)=ADL:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
