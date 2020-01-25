package name.qd.analysis.chip.analyzer;

import java.util.Date;
import java.util.List;

import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

public abstract class ChipAnalyzer {
	protected ChipAnalyzerManager chipAnalyzerManager;
	
	public ChipAnalyzer(ChipAnalyzerManager chipAnalyzerManager) {
		this.chipAnalyzerManager = chipAnalyzerManager;
	}
	
	public abstract int getInputField();
	public abstract List<String> getHeaderString(String branch, String product);
	public abstract List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl, String ... customInputs);
	public abstract List<String> getCustomDescreption();
}
