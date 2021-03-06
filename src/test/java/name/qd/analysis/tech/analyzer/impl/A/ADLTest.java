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
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.FakeAnalyzerManager;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.TimeUtils;

public class ADLTest {
	private TechAnalyzerManager manager = new FakeAnalyzerManager();
	private DataSource dataSource = new FakeDataSource();
	private String startDate = "20180505";
	private String endDate = "20180606";
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
			List<AnalysisResult> lst = manager.getAnalysisResult(dataSource, TechAnalyzers.ADL, "1101", from, to);
			for (int i = 0; i < lst.size(); i++) {
				Date currentDate = TimeUtils.afterDays(from, i);
				assertEquals(lst.get(i).getDate(), currentDate);
				
				assertEquals(lst.get(i).getValue().get(0), new Double(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void customTest() {
		try {
			List<AnalysisResult> lst = manager.getCustomAnalysisResult(dataSource, TechAnalyzers.ADL, "1101", from, to, "Y");
			double sum = 0;
			for (int i = 0; i < lst.size(); i++) {
				Date currentDate = TimeUtils.afterDays(from, i);
				assertEquals(lst.get(i).getDate(), currentDate);
				
				sum = sum + 0;
				assertEquals(lst.get(i).getValue().get(0), Double.valueOf(sum));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
