package name.qd.techAnalyst.analyzer.impl;

import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.analyzer.ITechAnalyzer;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class DayAvg5 implements ITechAnalyzer {

	@Override
	public List<AnalysisResult> analyze(String sFrom, String sTo, String sProdId, List<ProdClosingInfo> lst) {
		List<AnalysisResult> lstResult = new ArrayList<AnalysisResult>();
		for(int i = lst.size() - 1 ; i >= 4 ;  i--) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lst.get(i).getDate());
			double dSum = 0;
			for(int j = 0 ; j < 5 ; j++) {
				dSum += lst.get(i - j).getClosePrice();
			}
			result.setValue(dSum / 5);
			lstResult.add(result);
		}
		return lstResult;
	}
}
