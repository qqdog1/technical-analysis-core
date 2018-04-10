package name.qd.analysis.tech.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.Analyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.vo.AnalysisResult;
import name.qd.analysis.vo.DailyClosingInfo;

/**
 * 騰落
 * 上漲家數 - 下跌家數  累加
 */
public class AD implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(AD.class);
	
	@Override
	public String getCacheName(String product) {
		return AD.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<DailyClosingInfo> lstDaily = dataManager.getDailyClosingInfo(from, to);
			for(DailyClosingInfo info : lstDaily) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				int value = info.getAdvance()-info.getDecline();
				result.setValue(value);
				lst.add(result);
			}
		} catch (Exception e) {
			log.error("AD analyze failed.", e);
			throw e;
		}
		return lst;
	}
	
	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataManager, Analyzer.AD, product, from, to);
		String accu = inputs[0];
		if(!"Y".equalsIgnoreCase(accu)) {
			return lst;
		}
		return AnalystUtils.accu(lst);
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Accumulate(Y/N):");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.MARKET;
	}
}
