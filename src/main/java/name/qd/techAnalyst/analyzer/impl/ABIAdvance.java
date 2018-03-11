package name.qd.techAnalyst.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.DailyClosingInfo;

public class ABIAdvance implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ABIAdvance.class);

	@Override
	public String getCacheName(String product) {
		return ABIAdvance.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<DailyClosingInfo> lstDaily = dataManager.getDailyClosingInfo(from, to);
			for(DailyClosingInfo info : lstDaily) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				int value = info.getAdvance()-info.getDecline();
				int total = info.getAdvance()+info.getDecline()+info.getUnchanged();
				result.setValue(value);
				result.setValue(total);
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("ABIAdvance analyze failed.", e);
		}
		return lstResult;
	}
	
	@Override
	public List<AnalysisResult> customResult(List<AnalysisResult> lst, Object... objs) {
		return null;
	}
	
	@Override
	public List<String> customDescreption() {
		return null;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.MARKET;
	}
}
