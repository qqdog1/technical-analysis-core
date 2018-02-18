package name.qd.techAnalyst;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.Constants.Exchange;
import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.dataSource.DataSourceFactory;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.VerifyResult;
import name.qd.techAnalyst.vo.VerifyResult.VerifyDetail;
import name.qd.techAnalyst.winPercent.WPVerifier;
import name.qd.techAnalyst.winPercent.WPVerifierFactory;

public class TechAnalyst {
	private Logger log;
	private TechAnalyzerManager analyzerManager;
	private DataSource twseDataManager;
	
	private TechAnalyst() {
		initLogger();
		
		analyzerManager = new TechAnalyzerManager();
		twseDataManager = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
		
		List<AnalysisResult> lst = null;
		try {
			Date from = TimeUtil.getDateTimeFormat().parse("20080101-00:00:00:000");
			Date to = TimeUtil.getDateTimeFormat().parse("20180217-00:00:00:000");
			String product = "0050";
			Analyzer analyzer = Analyzer.ABI;
			
			lst = analyzerManager.getAnalysisResult(twseDataManager, analyzer, product, from, to);
			for(AnalysisResult result : lst) {
				System.out.println(result.getKeyString() + ":" + result.getValue());
			}
			
			WPVerifier verifier = WPVerifierFactory.getInstance().getVerifier(analyzer);
			VerifyResult vf = verifier.verify(twseDataManager, lst, product, from, to, 10, 300, 10);
			for(VerifyDetail detail : vf.getVerifyDetails()) {
				System.out.println(detail.getDate() + ":" + detail.getWinLose().name());
			}
			System.out.println(vf.getWinPercent());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void initLogger() {
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		log = LogManager.getLogger(TechAnalyst.class);
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
