package name.qd.techAnalyst.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.analyzer.TechAnalyzerFactory;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.util.StringCombineUtil;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.DailyClosingInfo;

/**
 * (上漲家數 / 下跌家數) / (上漲成交量 / 下跌成交量)
 */
public class ArmsIndex implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ArmsIndex.class);
	private TechAnalyzerFactory factory = TechAnalyzerFactory.getInstance();
	
	@Override
	public String getCacheName(String product) {
		return ArmsIndex.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		try {
			List<DailyClosingInfo> lstDaily = dataManager.getDailyClosingInfo(from, to);
			List<AnalysisResult> lstAdvanceVolume = factory.getAnalyzer(Analyzer.AdvancingVolume).analyze(dataManager, product, from, to);
			List<AnalysisResult> lstDeclineVolume = factory.getAnalyzer(Analyzer.DecliningVolume).analyze(dataManager, product, from, to);
			List<AnalysisResult> lstUnchangeVolume = factory.getAnalyzer(Analyzer.UnchangedVolume).analyze(dataManager, product, from, to);
			
			for(int i = 0 ; i < lstDaily.size() ; i++) {
				int ad = lstDaily.get(i).getAdvance();
				double addd = lstAdvanceVolume.get(i).getValue().get(1);
				int de = lstDaily.get(i).getDecline();
				double deee = lstDeclineVolume.get(i).getValue().get(1);
				int un = lstDaily.get(i).getUnchanged();
				double unnn = lstUnchangeVolume.get(i).getValue().get(1);
				System.out.println(StringCombineUtil.combine(lstDaily.get(i).getDate().toString(), ",", lstAdvanceVolume.get(i).getDate().toString(), ",", lstDeclineVolume.get(i).getDate().toString()));
				System.out.println("[" + ad + ":" + addd + "],[" + de + ":" + deee + "],[" + un + ":" + unnn + "]");
			}
			
		} catch (Exception e) {
			log.error("Analyze ArmsIndex failed.", e);
		}
		return null;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String ... inputs) {
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
