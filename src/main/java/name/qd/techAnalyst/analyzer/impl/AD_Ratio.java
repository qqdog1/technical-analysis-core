package name.qd.techAnalyst.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.DailyClosingInfo;

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
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<DailyClosingInfo> lstDaily = dataManager.getDailyClosingInfo(from, to);
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
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = analyze(dataManager, product, from, to);
		return AnalystUtils.NDaysAvgByAnalysisResult(lst, ma);
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("MA:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.MARKET;
	}
}
