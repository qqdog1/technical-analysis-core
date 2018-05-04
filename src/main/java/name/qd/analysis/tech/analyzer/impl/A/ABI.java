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
 * 絕對值(上漲家數-下跌家數)
 */
public class ABI implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ABI.class);

	@Override
	public String getCacheName(String product) {
		return ABI.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
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
			throw e;
		}
		return lstResult;
	}
	
	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String ... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataManager, TechAnalyzers.ABI, product, from, to);
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
