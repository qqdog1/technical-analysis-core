package name.qd.analysis.chip.analyzer.impl;

import java.util.Date;
import java.util.List;

import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.fileCache.FileCacheManager;

public class TotalPnl implements ChipAnalyzer {

	@Override
	public int getInputField() {
		return 0;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		return null;
	}

	@Override
	public List<List<String>> analyze(FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, boolean isOpenPnl) {
		return null;
	}

}
