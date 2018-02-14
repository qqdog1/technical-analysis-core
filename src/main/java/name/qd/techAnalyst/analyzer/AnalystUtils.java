package name.qd.techAnalyst.analyzer;

import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class AnalystUtils {
	public static List<AnalysisResult> NDaysAvg(List<ProductClosingInfo> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<AnalysisResult>();
		for(int i = lst.size() - 1 ; i >= days - 1 ;  i--) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lst.get(i).getDate());
			double sum = 0;
			for(int j = 0 ; j < days ; j++) {
				sum += lst.get(i - j).getAvgPrice();
			}
			result.setValue(sum / days);
			lstResult.add(result);
		}
		return lstResult;
	}
	
	public static List<AnalysisResult> NDaysAvgByAnalysisResult(List<AnalysisResult> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<AnalysisResult>();
		for(int i = lst.size() - 1 ; i >= days - 1 ;  i--) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lst.get(i).getDate());
			double sum = 0;
			for(int j = 0 ; j < days ; j++) {
				sum += lst.get(i - j).getValue();
			}
			result.setValue(sum / days);
			lstResult.add(result);
		}
		return lstResult;
	}
}
