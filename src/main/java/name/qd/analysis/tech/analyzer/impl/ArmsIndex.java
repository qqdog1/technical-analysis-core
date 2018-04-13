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
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;

/**
 * (上漲家數 / 下跌家數) / (上漲成交量 / 下跌成交量)
 */
public class ArmsIndex implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ArmsIndex.class);
	private TechAnalyzerManager manager = TechAnalyzerManager.getInstance();
	
	@Override
	public String getCacheName(String product) {
		return ArmsIndex.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<AnalysisResult> lstAdvanceVolume = manager.getAnalysisResult(dataManager, Analyzer.AdvancingVolume, product, from, to);
			List<AnalysisResult> lstDeclineVolume = manager.getAnalysisResult(dataManager, Analyzer.DecliningVolume, product, from, to);
			
			if(lstAdvanceVolume.size() != lstDeclineVolume.size()) {
				throw new Exception("AdvanceVolume data count != DeclineVolume data count");
			}
			
			for(int i = 0 ; i < lstAdvanceVolume.size() ; i++) {
				AnalysisResult advanceResult = lstAdvanceVolume.get(i);
				AnalysisResult declineResult = lstDeclineVolume.get(i);
				
				if(advanceResult.getDate().getTime() != declineResult.getDate().getTime()) {
					throw new Exception("AdvanceVolume data Date != DeclineVolume data Date");
				}
				
				double adCount = advanceResult.getValue().get(1);
				double deCount = declineResult.getValue().get(1);
				double adVolume = advanceResult.getValue().get(0);
				double deVolume = declineResult.getValue().get(0);
				
				AnalysisResult result = new AnalysisResult();
				result.setDate(advanceResult.getDate());
				result.setValue((adCount/deCount)/(adVolume/deVolume));
				lst.add(result);
			}
		} catch (Exception e) {
			log.error("Analyze ArmsIndex failed.", e);
			throw e;
		}
		return lst;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String ... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataManager, Analyzer.ArmsIndex, product, from, to);
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
