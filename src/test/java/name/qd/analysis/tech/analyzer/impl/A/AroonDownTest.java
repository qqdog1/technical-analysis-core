package name.qd.analysis.tech.analyzer.impl.A;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.FAKE.FakeDataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.FakeAnalyzerManager;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.TimeUtils;

public class AroonDownTest {
	private TechAnalyzerManager manager = new FakeAnalyzerManager();
	private DataSource dataSource = new FakeDataSource();
	private String startDate = "20180505";
	private String endDate = "20180606";
	private String days = "5";
	private Date from;
	private Date to;
	
	@Before
	public void init() {
		try {
			from = TimeUtils.getDateFormat().parse(startDate);
			to = TimeUtils.getDateFormat().parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void customTest() {
		try {
			List<AnalysisResult> lst = manager.getCustomAnalysisResult(dataSource, TechAnalyzers.Aroon_DOWN, "1101", from, to, days);
			List<ProductClosingInfo> lstProducts = dataSource.getProductClosingInfo("1101", from, to);
			
			for (int i = 0; i < lst.size(); i++) {
				int iDays = Integer.parseInt(days);
				Date currentDate = TimeUtils.afterDays(from, iDays+i);
				assertEquals(lst.get(i).getDate(), currentDate);
				
				double min = 99999;
				int index = 0;
				for(int x = 0; x <= iDays; x++) {
					double price = lstProducts.get(i+x).getLowerPrice();
					if(price < min) {
						min = price;
						index = x;
					}
				}
				assertEquals(lst.get(i).getValue().get(0), Double.valueOf((double)index/(double)iDays*100));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
