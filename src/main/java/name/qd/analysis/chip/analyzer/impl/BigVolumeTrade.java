package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;
/**
 * 任何 買-賣 或是 賣-買 超過一定量 都列出來
 */
public class BigVolumeTrade implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BigVolumeTrade.class);
	
	@Override
	public int getInputField() {
		return InputField.BROKER + InputField.PRODUCT + InputField.FROM + InputField.TO + InputField.TRADE_COST;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Date");
		lst.add("Product");
		lst.add("B/S");
		lst.add("Price");
		lst.add("Share");
		lst.add("Volume");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl) {
		
		
		
		
		return null;
	}
}
