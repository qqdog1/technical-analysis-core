package name.qd.analysis.tech.analyzer.impl.A;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

public class ArmsIndexTest {
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
	public void analyzeTest() {
		try {
			List<AnalysisResult> lst = manager.getAnalysisResult(dataSource, TechAnalyzers.ArmsIndex, "", from, to);
			Map<Date, List<ProductClosingInfo>> map = dataSource.getAllProductClosingInfo(from, to);
			for (int i = 0; i < lst.size(); i++) {
				Date currentDate = TimeUtils.afterDays(from, i);
				assertEquals(lst.get(i).getDate(), currentDate);
				
				List<ProductClosingInfo> lstProductInfo = map.get(currentDate);
				double advanceVolume = 0;
				double declineVolume = 0;
				double advanceCount = 0;
				double declineCount = 0;
				for(ProductClosingInfo info : lstProductInfo) {
					if(info.getADStatus() == ProductClosingInfo.ADVANCE) {
						advanceCount++;
						advanceVolume += info.getFilledShare();
					} else if(info.getADStatus() == ProductClosingInfo.DECLINE) {
						declineCount++;
						declineVolume += info.getFilledShare();
					}
				}
				double a = advanceCount / declineCount;
				double b = advanceVolume / declineVolume;
				assertEquals(lst.get(i).getValue().get(0), Double.valueOf(a/b));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void customTest() {
		try {
			List<AnalysisResult> lst = manager.getCustomAnalysisResult(dataSource, TechAnalyzers.ArmsIndex, "", from, to, days);
			Map<Date, List<ProductClosingInfo>> map = dataSource.getAllProductClosingInfo(from, to);
			for (int i = 0; i < lst.size(); i++) {
				int iDays = Integer.parseInt(days);
				Date currentDate = TimeUtils.afterDays(from, iDays-1+i);
				assertEquals(lst.get(i).getDate(), currentDate);
				
				double sum = 0;
				for(int x = 0; x < iDays ; x++) {
					Date date = TimeUtils.afterDays(currentDate, -x);
					List<ProductClosingInfo> lstProductInfo = map.get(date);
					double advanceVolume = 0;
					double declineVolume = 0;
					double advanceCount = 0;
					double declineCount = 0;
					for(ProductClosingInfo info : lstProductInfo) {
						if(info.getADStatus() == ProductClosingInfo.ADVANCE) {
							advanceCount++;
							advanceVolume += info.getFilledShare();
						} else if(info.getADStatus() == ProductClosingInfo.DECLINE) {
							declineCount++;
							declineVolume += info.getFilledShare();
						}
					}
					double a = advanceCount / declineCount;
					double b = advanceVolume / declineVolume;
					sum += a/b;
				}
				
				assertEquals(lst.get(i).getValue().get(0), Double.valueOf(sum/iDays));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
