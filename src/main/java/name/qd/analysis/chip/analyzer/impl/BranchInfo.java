package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.chip.vo.BranchFinalInfo;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.NormalCacheManager;

/**
 * 分公司最新狀態
 */
public class BranchInfo implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BranchInfo.class);
	
	@Override
	public int getInputField() {
		return InputField.BROKER + InputField.FROM + InputField.TO + InputField.PRODUCT;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Broker");
		lst.add("Product");
		lst.add("Position");
		lst.add("AvgPrice");
		lst.add("Pnl");
		lst.add("Last Change");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl) {
		log.debug("Analyze Branch Info ... From {} to {}", from, to);
		
		NormalCacheManager cacheManager = null;
		try {
			cacheManager = fileCacheManager.getNormalCacheInstance(BranchFinalInfo.class.getSimpleName(), BranchFinalInfo.class.getName());
		} catch (Exception e) {
			log.error("Trying to get BranchFinalInfo cache failed.");
			return null;
		}
		
		
		
		return null;
	}
}
