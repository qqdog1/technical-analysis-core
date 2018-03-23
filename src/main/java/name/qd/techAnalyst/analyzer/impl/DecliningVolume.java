package name.qd.techAnalyst.analyzer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class DecliningVolume implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(DecliningVolume.class);

	@Override
	public String getCacheName(String product) {
		return DecliningVolume.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			Map<Date, List<ProductClosingInfo>> map = dataManager.getAllProductClosingInfo(from, to);
			for(Date date : map.keySet()) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(date);
				double volume = 0;
				int count = 0;
				for(ProductClosingInfo info : map.get(date)) {
					if(info.getADStatus() == ProductClosingInfo.DECLINE) {
						volume += info.getFilledShare();
						count++;
					}
				}
				if(volume != 0) {
					result.setValue(volume);
					result.setValue(count);
					lstResult.add(result);
				}
			}
			
			Collections.sort(lstResult, new Comparator<AnalysisResult>() {
				@Override
				public int compare(AnalysisResult r1, AnalysisResult r2) {
					return r1.getDate().compareTo(r2.getDate());
				}
			});
		} catch (Exception e) {
			log.error("DecliningVolume analyze failed.", e);
		}
		return lstResult;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String... inputs) {
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
