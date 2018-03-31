package name.qd.techAnalyst.backtest.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.Constants.Action;
import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.backtest.BackTesting;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.ActionResult;
import name.qd.techAnalyst.vo.AnalysisResult;

public class ABITesting implements BackTesting {
	@Override
	public List<ActionResult> getAction(DataSource dataSource, String product, Date from, Date to, String... custom) throws Exception {
		// 1.要用幾日均線驗證?
		// 2.均線值超過多少% 買點
		// 3.均線值低於多少% 賣點
		int ma = Integer.parseInt(custom[0]);
		double buyPercent = Double.parseDouble(custom[1]);
		double sellPercent = Double.parseDouble(custom[2]);
		
		List<ActionResult> lst = new ArrayList<>();
		List<AnalysisResult> lstResult = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, Analyzer.ABI, product, from, to);
		List<AnalysisResult> lstMAResult = AnalystUtils.NDaysAvgByAnalysisResult(lstResult, ma);
		for(AnalysisResult result : lstMAResult) {
			ActionResult action = new ActionResult();
			action.setDate(result.getDate());
			double percent = result.getValue().get(0) / result.getValue().get(1);
			if(percent >= buyPercent) {
				action.setAction(Action.BUY);
			} else if(percent <= sellPercent) {
				action.setAction(Action.SELL);
			}
			lst.add(action);
		}
		return lst;
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("MA:");
		lst.add("Buy percent:");
		lst.add("Sell percent:");
		return lst;
	}
}
