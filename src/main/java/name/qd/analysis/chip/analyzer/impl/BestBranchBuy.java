package name.qd.analysis.chip.analyzer.impl;

import java.util.Date;
import java.util.List;

import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

/**
 * 近X日
 * 最會賺前Y名分公司
 * 當日買進超過Z
 * 列出股票觀察
 */
public class BestBranchBuy implements ChipAnalyzer {

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
