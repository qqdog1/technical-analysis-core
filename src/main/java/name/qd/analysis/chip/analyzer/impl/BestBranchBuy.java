package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

/**
 * 近X日
 * 最會賺前Y名分公司
 * 當日買進超過Z
 * 列出股票觀察
 */
public class BestBranchBuy extends ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BestBranchBuy.class);
	
	public BestBranchBuy(ChipAnalyzerManager chipAnalyzerManager) {
		super(chipAnalyzerManager);
	}
	
	@Override
	public int getInputField() {
		return InputField.FROM + InputField.TO + InputField.CUSTOM + InputField.TRADE_COST;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Product");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl, String ... customInputs) {
		if(customInputs.length != 1) {
			log.error("Please input how many branch you want.");
			return null;
		}
		
		int branchCount;
		try {
			branchCount = Integer.parseInt(customInputs[0]);
		} catch (NumberFormatException e) {
			log.error("Input how many branch you want. {}", customInputs[0]);
		}
		
		
		
		
		return null;
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Top X branch:");
		return lst;
	}
}
