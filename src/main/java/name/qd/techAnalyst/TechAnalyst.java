package name.qd.techAnalyst;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.analyzer.impl.ABI;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.dataSource.TWSE.TWSEDataSource;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.VerifyResult;
import name.qd.techAnalyst.winPercent.impl.ABIVerify;

public class TechAnalyst {
	private TechAnalyzerManager analyzerManager;
	private DataSource twseDataManager;
	
	private TechAnalyst() {
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		Logger logger = LogManager.getLogger(TechAnalyst.class);
		
		analyzerManager = new TechAnalyzerManager();
		twseDataManager = new TWSEDataSource();
		
		List<AnalysisResult> lst = null;
		try {
			Date from = TimeUtil.getDateTimeFormat().parse("20180201-00:00:00:000");
			Date to = TimeUtil.getDateTimeFormat().parse("20180212-00:00:00:000");
			String product = "2453";
			String analyzerName = ABI.class.getSimpleName();
			
			lst = analyzerManager.getAnalysisResult(twseDataManager, analyzerName, product, from, to);
			for(AnalysisResult result : lst) {
				System.out.println(result.getKeyString() + ":" + result.getValue());
			}
			
			ABIVerify v = new ABIVerify();
			VerifyResult vf = v.verify(twseDataManager, lst, "0050", from, to, 1, 600, 1);
			System.out.println(vf.getWinPercent());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
