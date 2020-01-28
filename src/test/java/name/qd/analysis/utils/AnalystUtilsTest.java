package name.qd.analysis.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import name.qd.analysis.dataSource.FAKE.FakeDataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.vo.AnalysisResult;

public class AnalystUtilsTest {
	private FakeDataSource dataSource = new FakeDataSource();

	private SimpleDateFormat sdf = TimeUtils.getDateFormat();
	private int days = 5;
	private String startDate = "20190110";
	private String endDate = "20190215";

	@Test
	public void simpleMovingAverageTest() {
		try {
			List<ProductClosingInfo> lstProdInfo = dataSource.getProductClosingInfo("1101", sdf.parse(startDate), sdf.parse(endDate));
			List<AnalysisResult> lst = AnalystUtils.simpleMovingAverage(lstProdInfo, days);

			for (int i = 0; i < lst.size(); i++) {
				assertEquals(lst.get(i).getDate(), TimeUtils.afterDays(sdf.parse(startDate), days - 1 + i));

				double sum = 0;
				for (int x = 0; x < days; x++) {
					sum += lstProdInfo.get(x + i).getAvgPrice();
				}
				assertEquals(Double.valueOf(sum / days), lst.get(i).getValue().get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void simpleMovingAverageByResultTest() {
		try {
			List<ProductClosingInfo> lstProdInfo = dataSource.getProductClosingInfo("2202", sdf.parse(startDate), sdf.parse(endDate));
			List<AnalysisResult> lstSMA = AnalystUtils.simpleMovingAverage(lstProdInfo, days);
			List<AnalysisResult> lst = AnalystUtils.simpleMovingAverageByResult(lstSMA, days);

			Date smaStartDate = TimeUtils.afterDays(sdf.parse(startDate), days - 1);
			for (int i = 0; i < lst.size(); i++) {
				assertEquals(lst.get(i).getDate(), TimeUtils.afterDays(smaStartDate, days - 1 + i));

				double sum = 0;
				for (int x = 0; x < days; x++) {
					sum += lstSMA.get(x + i).getValue().get(0);
				}
				assertEquals(Double.valueOf(sum / days), lst.get(i).getValue().get(0), 0.001);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void exponentialMovingAverageTest() {
		try {
			List<ProductClosingInfo> lstProdInfo = dataSource.getProductClosingInfo("1101", sdf.parse(startDate), sdf.parse(endDate));
			List<AnalysisResult> lst = AnalystUtils.exponentialMovingAverage(lstProdInfo, days);
			double rate = 2d / (double) (days + 1);
			double inverseRate = 1d - rate;

			double ema = lstProdInfo.get(0).getAvgPrice();
			for (int x = 1; x < days - 1; x++) {
				ema = (ema * inverseRate) + (lstProdInfo.get(x).getAvgPrice() * rate);
			}

			for (int i = 0; i < lst.size(); i++) {
				assertEquals(lst.get(i).getDate(), TimeUtils.afterDays(sdf.parse(startDate), days - 1 + i));
				ema = (ema * inverseRate) + (lstProdInfo.get(days - 1 + i).getAvgPrice() * rate);
				assertEquals(Double.valueOf(ema), lst.get(i).getValue().get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void exponentialMovingAverageByResultTest() {
		try {
			List<ProductClosingInfo> lstProdInfo = dataSource.getProductClosingInfo("1101", sdf.parse(startDate), sdf.parse(endDate));
			List<AnalysisResult> lstResult = AnalystUtils.exponentialMovingAverage(lstProdInfo, days);
			List<AnalysisResult> lst = AnalystUtils.exponentialMovingAverageByResult(lstResult, days);
			double rate = 2d / (double) (days + 1);
			double inverseRate = 1d - rate;

			Date emaStartDate = TimeUtils.afterDays(sdf.parse(startDate), days - 1);

			double ema = lstResult.get(0).getValue().get(0);
			for (int x = 1; x < days - 1; x++) {
				ema = (ema * inverseRate) + (lstResult.get(x).getValue().get(0) * rate);
			}

			for (int i = 0; i < lst.size(); i++) {
				assertEquals(lst.get(i).getDate(), TimeUtils.afterDays(emaStartDate, days - 1 + i));
				ema = (ema * inverseRate) + (lstResult.get(days - 1 + i).getValue().get(0) * rate);
				assertEquals(Double.valueOf(ema), lst.get(i).getValue().get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void DEMAByResultTest() {
		try {
			List<ProductClosingInfo> lstProdInfo = dataSource.getProductClosingInfo("1101", sdf.parse(startDate), sdf.parse(endDate));
			List<AnalysisResult> lstResult = AnalystUtils.simpleMovingAverage(lstProdInfo, days);
			List<AnalysisResult> lst = AnalystUtils.DEMAByResult(lstResult, days);
			
			for (int i = 0; i < lst.size(); i++) {
				assertEquals(lst.get(i).getDate(), TimeUtils.afterDays(sdf.parse(startDate), (days-1)*3+i));
				// 值沒什麼好測的
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void WilderSmoothingTest() {
		try {
			List<ProductClosingInfo> lstProdInfo = dataSource.getProductClosingInfo("1101", sdf.parse(startDate), sdf.parse(endDate));
			List<AnalysisResult> lstResult = AnalystUtils.simpleMovingAverage(lstProdInfo, days);
			List<AnalysisResult> lst = AnalystUtils.WilderSmoothing(lstResult, days);
		
			for (int i = 0; i < lst.size(); i++) {
				assertEquals(lst.get(i).getDate(), TimeUtils.afterDays(sdf.parse(startDate), (days-1)*2+i));
				// 值沒測
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void NDaysAccuTest() {
		try {
			List<ProductClosingInfo> lstProdInfo = dataSource.getProductClosingInfo("1101", sdf.parse(startDate), sdf.parse(endDate));
			List<AnalysisResult> lstResult = AnalystUtils.simpleMovingAverage(lstProdInfo, days);
			List<AnalysisResult> lst = AnalystUtils.NDaysAccu(lstResult, days);
			
			for (int i = 0; i < lst.size(); i++) {
				assertEquals(lst.get(i).getDate(), TimeUtils.afterDays(sdf.parse(startDate), (days-1)*2+i));
				
				double sum = 0;
				for(int x = 0 ; x < days ; x++) {
					sum += lstResult.get(i+x).getValue().get(0);
				}
				assertEquals(Double.valueOf(sum), lst.get(i).getValue().get(0), 0.001);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void standardDeviationTest() {
		List<Double> lst = new ArrayList<>();
		lst.add(new Double(5));
		lst.add(new Double(6));
		lst.add(new Double(8));
		lst.add(new Double(9));
		assertEquals(new Double(1.58114), Double.valueOf(AnalystUtils.standardDeviation(lst)), 0.00001);
	}
	
	@Test
	public void NDayStandardDeviationTest() {
		List<AnalysisResult> lstResult = new ArrayList<>();
		List<Double> lstValue = new ArrayList<>();
		lstValue.add(new Double(5));
		lstValue.add(new Double(6));
		lstValue.add(new Double(8));
		lstValue.add(new Double(9));
		lstValue.add(new Double(5));
		lstValue.add(new Double(6));
		lstValue.add(new Double(8));
		lstValue.add(new Double(9));
		
		Calendar calendar = Calendar.getInstance();
		for(int i = 0; i < lstValue.size(); i++) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(calendar.getTime());
			result.setValue(lstValue.get(i));
			calendar.add(Calendar.DATE, 1);
			lstResult.add(result);
		}
		
		List<AnalysisResult> lst = AnalystUtils.NDayStandardDeviation(lstResult, 4);
		for(int i = 0 ; i < lst.size(); i++) {
			assertEquals(new Double(1.58114), lst.get(i).getValue().get(0), 0.00001);
		}
	}
}
