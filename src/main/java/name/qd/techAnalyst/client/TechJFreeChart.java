package name.qd.techAnalyst.client;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import name.qd.techAnalyst.vo.AnalysisResult;

public class TechJFreeChart {
	private ChartPanel chartPanel;
	private Map<String, TimeSeries> mapTimeSeries = new HashMap<>();

	public TechJFreeChart() {
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
		
		mapTimeSeries.put(name, timeSeries);
		
		prepareChartPanel();
	}
	
	public void removeData(String name) {
		if(mapTimeSeries.containsKey(name)) {
			mapTimeSeries.remove(name);
		}
		prepareChartPanel();
	}
	
	public void removeAll() {
		mapTimeSeries.clear();
		prepareChartPanel();
	}
	
	private void prepareChartPanel() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataset);
		XYPlot plot = (XYPlot) chart.getPlot();
		NumberAxis axis2 = new NumberAxis();
        axis2.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, axis2);
        plot.mapDatasetToRangeAxis(1, 1);
        StandardXYItemRenderer renderer = new StandardXYItemRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(1, renderer);
		
		if(mapTimeSeries.size() > 1) {
			int i = 0;
			for(TimeSeries timeSeries : mapTimeSeries.values()) {
				if(i%2 == 0) {
					dataset.addSeries(timeSeries);
				} else {
					dataset2.addSeries(timeSeries);
				}
				i++;
			}
			plot.setDataset(1, dataset2);
		} else {
			for(TimeSeries timeSeries : mapTimeSeries.values()) {
				dataset.addSeries(timeSeries);
			}
		}
		
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		chartPanel = new ChartPanel(chart);
	}
	
	public JPanel getChartPanel() {
		return chartPanel; 
	}
}
