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
 * 上漲家數 - 下跌家數
 */
public class AD_Issues implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(AD_Issues.class);
	
	@Override
	public String getCacheName(String product) {
		return AD_Issues.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<DailyClosingInfo> lst = dataSource.getDailyClosingInfo(from, to);
			for(DailyClosingInfo info : lst) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				result.setValue(info.getAdvance()-info.getDecline());
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("AD_Issues analyze failed.", e);
			throw e;
		}
		return lstResult;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, TechAnalyzers.AD_Issues, product, from, to);
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
