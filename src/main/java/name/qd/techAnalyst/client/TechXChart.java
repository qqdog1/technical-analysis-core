package name.qd.techAnalyst.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.YAxisPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

import name.qd.techAnalyst.vo.AnalysisResult;

public class TechXChart {
	private XYChart chart;
	private Set<String> setAnalyzer = new HashSet<>();

	public TechXChart(String title) {
		chart = new XYChartBuilder().width(800).height(600).title(title).build();
		chart.getStyler().setDatePattern("yyyy-MM-dd");
	}
	
	public void setData(String name, List<AnalysisResult> lst, int group, YAxisPosition yAxisPosition) {
		List<Date> lstDate = new ArrayList<>();
		List<Double> lstValue = new ArrayList<>();
		for(AnalysisResult result : lst) {
			lstDate.add(result.getDate());
			lstValue.add(result.getValue().get(0));
		}
		
		setData(name, lstDate, lstValue, group, yAxisPosition);
	}
	
	public void setData(String name, List<Date> lstX, List<Double> lstY, int group, YAxisPosition yAxisPosition) {
		if(setAnalyzer.contains(name)) {
			removeData(name);
		}
		chart.getStyler().setYAxisGroupPosition(group, yAxisPosition);
		
		XYSeries series = chart.addSeries(name, lstX, lstY);
		series.setMarker(SeriesMarkers.NONE);
		series.setYAxisGroup(group);
		
		setAnalyzer.add(name);
	}
	
	public void removeData(String name) {
		if(setAnalyzer.contains(name)) {
			chart.removeSeries(name);
			setAnalyzer.remove(name);
		}
	}
	
	public JPanel getChartPanel() {
		return new XChartPanel<XYChart>(chart);
	}
}
