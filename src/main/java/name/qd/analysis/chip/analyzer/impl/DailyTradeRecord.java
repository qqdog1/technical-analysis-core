package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.fileCache.FileCacheManager;

/**
 * 單一分公司 單日日交易紀錄
 */
public class DailyTradeRecord implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(DailyTradeRecord.class);
	
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
		lst.add("Price");
		lst.add("Shares");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl) {
		if("".equals(branch)) {
			log.error("Please choose a branch.");
			return null;
		}
		
		List<List<String>> lst = new ArrayList<>();
		
		try {
			Map<Date, Map<String, List<BuySellInfo>>> map = dataSource.getBuySellInfo(from, from);
			Map<String, List<BuySellInfo>> mapProduct = map.get(from);
			if(mapProduct.size() == 0) {
				log.warn("No data on this day. {}", from);
				return null;
			}
			
			for(String keyProduct : mapProduct.keySet()) {
				List<BuySellInfo> lstInfo = mapProduct.get(keyProduct);
				double buyShare = 0;
				double sellShare = 0;
				double buyCost = 0;
				double sellCost = 0;
				for(BuySellInfo info : lstInfo) {
					if(!branch.equals(info.getBrokerName())) {
						continue;
					}
					buyShare += info.getBuyShare();
					sellShare += info.getSellShare();
					buyCost += info.getBuyShare() * info.getPrice();
					sellCost += info.getSellShare() * info.getPrice();
				}
				if(buyShare > 0) {
					lst.add(getList(branch, keyProduct, "B", buyShare, buyCost));
				}
				if(sellShare > 0) {
					lst.add(getList(branch, keyProduct, "S", sellShare, sellCost));
				}
			}
		} catch (Exception e) {
			log.error("Try to get daily trading record failed .", e);
		}
		
		return lst;
	}

	private List<String> getList(String branch, String product, String side, double share, double cost) {
		List<String> lst = new ArrayList<>();
		lst.add(branch);
		lst.add(product);
		lst.add(side);
		lst.add(String.valueOf(cost/share));
		lst.add(String.valueOf(share));
		return lst;
	}
}
