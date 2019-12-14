package name.qd.analysis.tech.analyzer.impl.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;

public class AdvancingVolume extends TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(AdvancingVolume.class);

	public AdvancingVolume(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return AdvancingVolume.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			Map<Date, List<ProductClosingInfo>> map = dataSource.getAllProductClosingInfo(from, to);
			for(Date date : map.keySet()) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(date);
				double volume = 0;
				int count = 0;
				for(ProductClosingInfo info : map.get(date)) {
					if(info.getADStatus() == ProductClosingInfo.ADVANCE) {
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
			log.error("AdvancingVolume analyze failed.", e);
			throw e;
		}
		return lstResult;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = techAnalyzerManager.getAnalysisResult(dataSource, TechAnalyzers.AdvancingVolume, product, from, to);
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
