package name.qd.analysis.utils;

import java.util.ArrayList;
import java.util.List;

import name.qd.analysis.vo.AnalysisResult;
import name.qd.analysis.vo.ProductClosingInfo;

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
			List<Double> lstValues = new ArrayList<>();
			lstValues.add(sum / days);
			result.setValue(lstValues);
			lstResult.add(result);
		}
		return lstResult;
	}
	
	public static List<AnalysisResult> NDaysAvgByAnalysisResult(List<AnalysisResult> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<AnalysisResult>();
		for(int i = lst.size() - 1 ; i >= days - 1 ;  i--) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lst.get(i).getDate());
			List<Double> lstValues = lst.get(i).getValue();
			List<Double> lstValueResult = new ArrayList<>();
			for(int index = 0 ; index < lstValues.size() ; index++ ) {
				double sum = 0;
				for(int j = 0 ; j < days ; j++) {
					sum += lst.get(i - j).getValue().get(index);
				}
				lstValueResult.add(sum / days);
			}
			result.setValue(lstValueResult);
			lstResult.add(result);
		}
		return lstResult;
	}
	
	public static List<AnalysisResult> accu(List<AnalysisResult> lst) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		AnalysisResult lastResult = new AnalysisResult();
		lastResult.setValue(0);
		for(AnalysisResult result : lst) {
			AnalysisResult analysisResult = new AnalysisResult();
			analysisResult.setDate(result.getDate());
			analysisResult.setValue(lastResult.getValue().get(0)+result.getValue().get(0));
			lstResult.add(analysisResult);
			lastResult = analysisResult;
		}
		
		return lstResult;
	}
}
