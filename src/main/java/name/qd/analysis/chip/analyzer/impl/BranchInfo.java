package name.qd.analysis.chip.analyzer.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

/**
 * 分公司最新狀態
 */
public class BranchInfo implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BranchInfo.class);
	
	@Override
	public int getInputField() {
		return 0;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		return null;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl) {
		return null;
	}
}
