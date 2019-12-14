package name.qd.analysis.tech.analyzer.impl.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;

/**
 * (上漲家數 / 下跌家數) / (上漲成交量 / 下跌成交量)
 */
public class ArmsIndex extends TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ArmsIndex.class);
	
	public ArmsIndex(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return ArmsIndex.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<AnalysisResult> lstAdvanceVolume = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.AdvancingVolume, product, from, to);
			List<AnalysisResult> lstDeclineVolume = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.DecliningVolume, product, from, to);
			
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
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String ... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.ArmsIndex, product, from, to);
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
