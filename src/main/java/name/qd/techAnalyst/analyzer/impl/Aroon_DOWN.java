package name.qd.techAnalyst.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.utils.StringCombineUtil;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class Aroon_DOWN implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(Aroon_DOWN.class);
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(Aroon_DOWN.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) throws Exception {
		throw new Exception("Must enter days.");
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataManager, String product, Date from, Date to, String... inputs) throws Exception {
		int n = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProducts = dataManager.getProductClosingInfo(product, from, to);
			for(int i = n ; i < lstProducts.size() ; i++) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(lstProducts.get(i).getDate());
				double minPrice = 99999;
				int day = n;
				for(int index = i-n ; index <= i ; index++) {
					if(lstProducts.get(index).getLowerPrice() < minPrice) {
						minPrice = lstProducts.get(index).getLowerPrice();
						day = index - i + n;
					}
				}
				result.setValue(((double)day / (double)n) * 100d);
				lst.add(result);
			}
		} catch (Exception e) {
			log.error("Aroon Up analyze failed.", e);
			throw e;
		}
		return lst;
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("N Days:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
