package name.qd.analysis.tech.analyzer.impl.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.DailyClosingInfo;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;

/**
 * 上漲家數 / 下跌家數
 */
public class AD_Ratio implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(AD_Ratio.class);
	
	@Override
	public String getCacheName(String product) {
		return AD_Ratio.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<DailyClosingInfo> lstDaily = dataSource.getDailyClosingInfo(from, to);
			for(DailyClosingInfo dailyInfo : lstDaily) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(dailyInfo.getDate());
				result.setValue((double)dailyInfo.getAdvance()/(double)dailyInfo.getDecline());
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("Analyze AD_Ratio failed.", e);
			throw e;
		}
		return lstResult;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, TechAnalyzers.AD_Ratio, product, from, to);
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
		return AnalyzerType.MARKET;
	}
}
