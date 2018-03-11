package name.qd.techAnalyst.client;

import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import name.qd.techAnalyst.vo.AnalysisResult;

public class TechJFreechart {
	private ChartPanel chartPanel;
	private JFreeChart chart;
	private TimeSeriesCollection dataset;

	public TechJFreechart() {
		dataset = new TimeSeriesCollection();
	}
	
	public void setData(String name, List<AnalysisResult> lst) {
		TimeSeries timeSeries = new TimeSeries(name);
		
	}
	
	public void setData(String name, List<Date> lstX, List<Double> lstY) {
		
	}
	
	public void removeData() {
	}
}
