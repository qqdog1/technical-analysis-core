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
 * 騰落
 * 上漲家數 - 下跌家數  累加
 */
public class AD extends TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(AD.class);
	
	public AD(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return AD.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<DailyClosingInfo> lstDaily = dataSource.getDailyClosingInfo(from, to);
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
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		List<AnalysisResult> lst = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.AD, product, from, to);
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
