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

public class ABI implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ABI.class);

	@Override
	public String getCacheName(String product) {
		return ABI.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<DailyClosingInfo> lstDaily = dataManager.getDailyClosingInfo(from, to);
			for(DailyClosingInfo info : lstDaily) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				int abiValue = Math.abs(info.getAdvance()-info.getDecline());
				int total = info.getAdvance() + info.getDecline() + info.getUnchanged();
				result.setValue(abiValue);
				result.setValue(total);
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("ABI analyze failed.", e);
		}
		return lstResult;
	}
	
	@Override
	public List<AnalysisResult> customResult(List<AnalysisResult> lst, String ... inputs) {
		int ma = Integer.parseInt(inputs[0]);
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
