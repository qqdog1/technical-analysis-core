package name.qd.techAnalyst.analyzer;

import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class AnalystUtils {
	public static List<AnalysisResult> NDaysAvg(String sFrom, String sTo, List<ProdClosingInfo> lst, int iDays) {
		List<AnalysisResult> lstResult = new ArrayList<AnalysisResult>();
		for(int i = lst.size() - 1 ; i >= iDays - 1 ;  i--) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lst.get(i).getDate());
			double dSum = 0;
			for(int j = 0 ; j < iDays ; j++) {
				dSum += lst.get(i - j).getAvgPrice();
			}
			result.setValue(dSum / iDays);
			lstResult.add(result);
		}
		return lstResult;
	}
}
