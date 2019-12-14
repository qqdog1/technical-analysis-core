package name.qd.analysis.tech.backTest;

import java.util.Date;
import java.util.List;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.ActionResult;

public abstract class BackTesting {
	protected TechAnalyzerManager techAnalyzerManager;
	
	public BackTesting(TechAnalyzerManager techAnalyzerManager) {
		this.techAnalyzerManager = techAnalyzerManager;
	}
	
	public abstract List<ActionResult> getAction(DataSource dataSource, String product, Date from, Date to, String ... custom) throws Exception ;
	public abstract List<String> getCustomDescreption();
}
