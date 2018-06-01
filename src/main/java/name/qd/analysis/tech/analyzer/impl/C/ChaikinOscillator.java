package name.qd.analysis.tech.analyzer.impl.C;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.StringCombineUtil;

public class ChaikinOscillator implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ChaikinOscillator.class);

	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(ChaikinOscillator.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<AnalysisResult> lstADL = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, TechAnalyzers.ADL, product, from, to);
			List<AnalysisResult> lst3 = AnalystUtils.exponentialMovingAverageByResult(lstADL, 3);
			List<AnalysisResult> lst10 = AnalystUtils.exponentialMovingAverageByResult(lstADL, 10);
			Map<Date, AnalysisResult> map3 = new HashMap<>();
			for (AnalysisResult result : lst3) {
				map3.put(result.getDate(), result);
			}
			for (AnalysisResult result10 : lst10) {
				AnalysisResult result3 = map3.get(result10.getDate());
				AnalysisResult result = new AnalysisResult();
				result.setDate(result10.getDate());
				result.setValue(result3.getValue().get(0) - result10.getValue().get(0));
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("Analyze ChaikinOscillator failed.", e);
			throw e;
		}
		return lstResult;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String... inputs) throws Exception {
		return null;
	}

	@Override
	public List<String> getCustomDescreption() {
		return null;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
