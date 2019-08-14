package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

/**
 * 單一分公司 單日日交易紀錄
 */
public class DailyTradeRecord implements ChipAnalyzer {

	@Override
	public int getInputField() {
		return InputField.BROKER + InputField.FROM;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Branch");
		lst.add("Product");
		lst.add("B/S");
		lst.add("Shares");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, boolean isOpenPnl) {
		return null;
	}

}
