package name.qd.analysis.tech.analyzer.impl.A;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.FAKE.FakeDataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.FakeAnalyzerManager;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.TimeUtils;

public class ADRatioTest {
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
			List<AnalysisResult> lst = manager.getAnalysisResult(dataSource, TechAnalyzers.AD_Ratio, "", from, to);
			for (int i = 0; i < lst.size(); i++) {
				Date currentDate = TimeUtils.afterDays(from, i);
				assertEquals(lst.get(i).getDate(), currentDate);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(currentDate);
				int day = calendar.get(Calendar.DATE);
				assertEquals(lst.get(i).getValue().get(0), Double.valueOf((double)(400+day)/(double)(400-day)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void customTest() {
		try {
			List<AnalysisResult> lst = manager.getCustomAnalysisResult(dataSource, TechAnalyzers.AD_Ratio, "", from, to, days);
			int iDays = Integer.parseInt(days);
			for (int i = 0; i < lst.size(); i++) {
				Date currentDate = TimeUtils.afterDays(from, iDays-1+i);
				assertEquals(lst.get(i).getDate(), currentDate);
				
				double sum = 0;
				for(int x = 0; x < iDays; x++) {
					Date date = TimeUtils.afterDays(currentDate, -x);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					sum = sum + (double)(400+calendar.get(Calendar.DATE))/(double)(400-calendar.get(Calendar.DATE));
				}
				assertEquals(lst.get(i).getValue().get(0), Double.valueOf(sum/iDays));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
