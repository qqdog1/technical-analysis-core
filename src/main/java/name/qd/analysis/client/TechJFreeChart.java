package name.qd.analysis.client;

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

import name.qd.analysis.tech.vo.AnalysisResult;

public class TechJFreeChart {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	private ChartPanel chartPanel;
	private Map<String, TimeSeries> mapLeft = new HashMap<>();
	private Map<String, TimeSeries> mapRight = new HashMap<>();

	public TechJFreeChart() {
	}
	
	public void setData(String name, List<AnalysisResult> lst, int side) {
		List<Date> lstDate = new ArrayList<>();
		List<Double> lstValue = new ArrayList<>();
		for(AnalysisResult result : lst) {
			lstDate.add(result.getDate());
			lstValue.add(result.getValue().get(0));
		}
		
		setData(name, lstDate, lstValue, side);
	}
	
	public void setData(String name, List<Date> lstX, List<Double> lstY, int side) {
		TimeSeries timeSeries = new TimeSeries(name);
		for(int i = 0 ; i < lstX.size() ; i++) {
			timeSeries.add(new TimeSeriesDataItem(new Day(lstX.get(i)), lstY.get(i)));
		}
		
		if(side == LEFT) {
			mapLeft.put(name, timeSeries);
		} else if(side == RIGHT) {
			mapRight.put(name, timeSeries);
		}
		
		prepareChartPanel();
	}
	
	public void removeData(String name) {
		if(mapLeft.containsKey(name)) {
			mapLeft.remove(name);
		} else if(mapRight.containsKey(name)) {
			mapRight.remove(name);
		}
		prepareChartPanel();
	}
	
	public void removeAll() {
		mapLeft.clear();
		mapRight.clear();
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
		
		for(TimeSeries timeSeries : mapLeft.values()) {
			dataset.addSeries(timeSeries);
		}
		for(TimeSeries timeSeries : mapRight.values()) {
			dataset2.addSeries(timeSeries);
		}
		
		plot.setDataset(1, dataset2);
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		chartPanel = new ChartPanel(chart);
	}
	
	public JPanel getChartPanel() {
		return chartPanel; 
	}
}
