package name.qd.techAnalyst.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import name.qd.techAnalyst.vo.AnalysisResult;

public class TechJFreeChart {
	private ChartPanel chartPanel;
//	private JFreeChart chart;
//	private XYPlot plot;
//	private TimeSeriesCollection dataset;

	public TechJFreeChart() {
//		dataset = new TimeSeriesCollection();
//		plot = new XYPlot();
//		plot.setDataset(dataset);
//		chart = new JFreeChart(plot);
//		chartPanel = new ChartPanel(chart);
//		chartPanel.setMouseWheelEnabled(true);
	}
	
	public void setData(String name, List<AnalysisResult> lst) {
		List<Date> lstDate = new ArrayList<>();
		List<Double> lstValue = new ArrayList<>();
		for(AnalysisResult result : lst) {
			lstDate.add(result.getDate());
			lstValue.add(result.getValue().get(0));
		}
		
		setData(name, lstDate, lstValue);
	}
	
	public void setData(String name, List<Date> lstX, List<Double> lstY) {
		TimeSeries timeSeries = new TimeSeries(name);
		for(int i = 0 ; i < lstX.size() ; i++) {
			timeSeries.add(new TimeSeriesDataItem(new Day(lstX.get(i)), lstY.get(i)));
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(timeSeries);
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataset);
		chartPanel = new ChartPanel(chart);
	}
	
	public void removeData(String name) {
	}
	
	public JPanel getChartPanel() {
		return chartPanel; 
	}
}
