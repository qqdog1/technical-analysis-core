package name.qd.analysis.chip.analyzer.impl;

import java.util.Date;
import java.util.List;

import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

public class BranchInfoByCache extends ChipAnalyzer {
	public BranchInfoByCache(ChipAnalyzerManager chipAnalyzerManager) {
		super(chipAnalyzerManager);
	}

	@Override
	public int getInputField() {
		return 0;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		return null;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl, String ... customInputs) {
		return null;
	}

	@Override
	public List<String> getCustomDescreption() {
		return null;
	}
}
