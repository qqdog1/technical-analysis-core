package name.qd.techAnalyst.analyzer.impl.ma;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.util.StringCombineUtil;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class MovingAvg240Day implements TechAnalyzer {
	private static Logger logger = LogManager.getLogger(MovingAvg240Day.class);
	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		List<ProductClosingInfo> lst = null;
		try {
			lst = dataManager.getProductClosingInfo(product, from, to);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return AnalystUtils.NDaysAvg(lst, 240);
	}
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(MovingAvg240Day.class.getSimpleName(), product);
	}
	
	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
