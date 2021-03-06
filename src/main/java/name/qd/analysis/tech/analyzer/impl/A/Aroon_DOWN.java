package name.qd.analysis.tech.analyzer.impl.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.StringCombineUtil;

public class Aroon_DOWN extends TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(Aroon_DOWN.class);
	
	public Aroon_DOWN(TechAnalyzerManager techAnalyzerManager) {
		super(techAnalyzerManager);
	}
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(Aroon_DOWN.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		throw new Exception("Must enter days.");
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String... inputs) throws Exception {
		int n = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = new ArrayList<>();
		try {
			List<ProductClosingInfo> lstProducts = dataSource.getProductClosingInfo(product, from, to);
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
