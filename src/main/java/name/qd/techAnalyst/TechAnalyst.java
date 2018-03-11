package name.qd.techAnalyst;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchart.style.Styler.YAxisPosition;

import name.qd.techAnalyst.Constants.Exchange;
import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.backtest.BackTesting;
import name.qd.techAnalyst.backtest.WPVerifierFactory;
import name.qd.techAnalyst.client.TechClient;
import name.qd.techAnalyst.client.TechXChart;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.dataSource.DataSourceFactory;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;
import name.qd.techAnalyst.vo.VerifyResult;
import name.qd.techAnalyst.vo.VerifyResult.VerifyDetail;

public class TechAnalyst {
	private Logger log;
	private TechAnalyzerManager analyzerManager;
	private DataSource twseDataManager;
	private TechXChart chartUI;
	
	private TechAnalyst() {
		initLogger();
		
		analyzerManager = new TechAnalyzerManager();
		twseDataManager = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
		chartUI = new TechXChart("QQ");
		new TechClient();
		
		List<AnalysisResult> lst = null;
		VerifyResult vf = null;
		try {
			Date from = TimeUtil.getDateTimeFormat().parse("20110101-00:00:00:000");
			Date to = TimeUtil.getDateTimeFormat().parse("20180307-00:00:00:000");
			String product = "0050";
			
			Analyzer analyzer = Analyzer.ABI;
			outputResult(analyzer, product, from, to, 10, 40, 10);
			
			//
			analyzer = Analyzer.ABIAdvance;
//			outputResult(analyzer, product, from, to, 1, 600, 10);
			
			//
			analyzer = Analyzer.ABIDecline;
//			outputResult(analyzer, product, from, to, 1, 600, 10);
			//
			analyzer = Analyzer.ADL;
//			outputResult(analyzer, product, from, to);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void outputResult(Analyzer analyzer, String product, Date from, Date to, Object ... objs) {
		List<AnalysisResult> lst = getAnalysisResult(analyzer, product, from, to);
		VerifyResult vf = getVerifyResult(analyzer, lst, product, from, to, objs);
		System.out.println(vf.getWinPercent());
		
		List<AnalysisResult> lstMA = AnalystUtils.NDaysAvgByAnalysisResult(lst, 10);
		
		try {
//			chartUI.setData(analyzer.name(), lstMA, YAxisPosition.Left);
			
			List<ProductClosingInfo> lstProdInfo = twseDataManager.getProductClosingInfo(product, from, to);
			List<Date> lstDate = new ArrayList<>();
			List<Double> lstPrice = new ArrayList<>();
			
			for(ProductClosingInfo prodInfo : lstProdInfo) {
				lstDate.add(prodInfo.getDate());
				lstPrice.add(prodInfo.getClosePrice());
			}
//			chartUI.setData(product, lstDate, lstPrice, YAxisPosition.Left);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<AnalysisResult> getAnalysisResult(Analyzer analyzer, String product, Date from, Date to) {
		List<AnalysisResult> lst = analyzerManager.getAnalysisResult(twseDataManager, analyzer, product, from, to);
		for(AnalysisResult result : lst) {
			System.out.println(result.getKeyString() + ":" + result.getValue());
		}
		return lst;
	}
	
	private VerifyResult getVerifyResult(Analyzer analyzer, List<AnalysisResult> lst, String product, Date from, Date to, Object ... objs) {
		BackTesting verifier = WPVerifierFactory.getInstance().getVerifier(analyzer);
		VerifyResult vf = verifier.verify(twseDataManager, lst, product, from, to, objs);
		for(VerifyDetail detail : vf.getVerifyDetails()) {
			System.out.println(detail.getDate() + ":" + detail.getWinLose().name());
		}
		return vf;
	}
	
	private void initLogger() {
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		log = LogManager.getLogger(TechAnalyst.class);
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
