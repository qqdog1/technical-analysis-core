package name.qd.techAnalyst.analyzer;

import java.util.HashMap;
import java.util.Map;

import name.qd.techAnalyst.analyzer.impl.ABI;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg10Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg120Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg20Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg240Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg5Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg60Day;

public class TechAnalyzerFactory {
	private static TechAnalyzerFactory instance = new TechAnalyzerFactory();
	private Map<String, TechAnalyzer> map = new HashMap<>();
	
	private TechAnalyzerFactory() {
	}
	
	public static TechAnalyzerFactory getInstance() {
		return instance;
	}
	
	public TechAnalyzer getAnalyzer(String name) {
		if(!map.containsKey(name)) {
			createAnalyzer(name);
		}
		return map.get(name);
	}
	
	private void createAnalyzer(String name) {
		if(name.equals(MovingAvg5Day.class.getSimpleName())) {
			map.put(name, new MovingAvg5Day());
		} else if(name.equals(MovingAvg10Day.class.getSimpleName())) {
			map.put(name, new MovingAvg10Day());
		} else if(name.equals(MovingAvg20Day.class.getSimpleName())) {
			map.put(name, new MovingAvg20Day());
		} else if(name.equals(MovingAvg60Day.class.getSimpleName())) {
			map.put(name, new MovingAvg60Day());
		} else if(name.equals(MovingAvg120Day.class.getSimpleName())) {
			map.put(name, new MovingAvg120Day());
		} else if(name.equals(MovingAvg240Day.class.getSimpleName())) {
			map.put(name, new MovingAvg240Day());
		} else if(name.equals(ABI.class.getSimpleName())) {
			map.put(name, new ABI());
		}
	}
}
