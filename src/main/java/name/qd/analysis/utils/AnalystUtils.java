package name.qd.analysis.utils;

import java.util.ArrayList;
import java.util.List;

import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.vo.AnalysisResult;

public class AnalystUtils {
	public static List<AnalysisResult> simpleMovingAverage(List<ProductClosingInfo> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<>();
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
	
	public static List<AnalysisResult> simpleMovingAverageByResult(List<AnalysisResult> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<>();
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
	
	public static List<AnalysisResult> exponentialMovingAverage(List<ProductClosingInfo> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		double rate = 2d / (double)(days+1);
		double lastValueRate = 1d-rate;
		AnalysisResult lastResult = null;
		for(int i = 0 ; i < lst.size() ;i++) {
			AnalysisResult result = new AnalysisResult();
			ProductClosingInfo info = lst.get(i);
			result.setDate(info.getDate());
			double lastValue = 0;
			if(i != 0) {
				lastValue = lastResult.getValue().get(0) * lastValueRate;
				double value = lastValue + (info.getAvgPrice() * rate);
				result.setValue(value);
			} else {
				result.setValue(info.getAvgPrice());
			}
			
			lastResult = result;
			
			if(i >= days) {
				lstResult.add(result);
			}
		}
		return lstResult;
	}
	
	public static List<AnalysisResult> exponentialMovingAverageByResult(List<AnalysisResult> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		double rate = 2d / (double)(days+1);
		double lastValueRate = 1d-rate;
		AnalysisResult lastResult = null;
		for(int i = 0 ; i < lst.size() ;i++) {
			AnalysisResult result = new AnalysisResult();
			AnalysisResult data = lst.get(i);
			result.setDate(data.getDate());
			double lastValue = 0;
			if(i != 0) {
				lastValue = lastResult.getValue().get(0) * lastValueRate;
				double value = lastValue + (data.getValue().get(0) * rate);
				result.setValue(value);
			} else {
				result.setValue(data.getValue().get(0));
			}
			
			lastResult = result;
			
			if(i >= days) {
				lstResult.add(result);
			}
		}
		return lstResult;
	}
	
	public static List<AnalysisResult> WilderSmoothing(List<AnalysisResult> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		
		double sum = 0;
		for(int i = 0 ; i < days ; i++) {
			sum += lst.get(i).getValue().get(0);
		}
		double firstMA = sum/days;
		AnalysisResult firstResult = new AnalysisResult();
		firstResult.setDate(lst.get(days-1).getDate());
		firstResult.setValue(firstMA);
		lstResult.add(firstResult);
		
		AnalysisResult lastResult = firstResult;
		for(int i = days ; i < lst.size() ; i++) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lst.get(i).getDate());
			double value = lastResult.getValue().get(0)+(lst.get(i).getValue().get(0)-lastResult.getValue().get(0))/days;
			result.setValue(value);
			lstResult.add(result);
			lastResult = result;
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
	
	public static List<AnalysisResult> NDaysAccu(List<AnalysisResult> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<>();
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
				lstValueResult.add(sum);
			}
			result.setValue(lstValueResult);
			lstResult.add(result);
		}
		return lstResult;
	}
		
	public static List<AnalysisResult> NDayStandardDeviation(List<AnalysisResult> lst, int days) {
		List<AnalysisResult> lstResult = new ArrayList<>();
		for(int i = lst.size() - 1 ; i >= days - 1 ;  i--) {
			AnalysisResult result = new AnalysisResult();
			result.setDate(lst.get(i).getDate());
			List<Double> lstValue = new ArrayList<>();
			for(int j = 0 ; j < days ; j++) {
				lstValue.add(lst.get(i - j).getValue().get(0));
			}
			result.setValue(standardDeviation(lstValue));
			lstResult.add(result);
		}
		
		return lstResult;
	}
	
	private static double standardDeviation(List<Double> values) {
		double powSum = 0;
		double sum = 0;
		double length = (double) values.size();
		for(double value : values) {
			powSum += Math.pow(value, 2);
			sum += value;
		}
		return Math.sqrt(powSum/length - Math.pow(sum/length, 2));
	}
	
	public static void main(String[] s) {
		List<AnalysisResult> lst = new ArrayList<>();
		for(int i = 0 ; i < 5; i++) {
			AnalysisResult result = new AnalysisResult();
			result.setValue((i*2)+1);
			lst.add(result);
		}
		List<AnalysisResult> lstResult = NDayStandardDeviation(lst, 3);
		for(AnalysisResult result : lstResult) {
			System.out.println(result.getValue().get(0));
		}
	}
}
