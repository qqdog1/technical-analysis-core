package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.chip.vo.BranchFinalInfo;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.fileCache.FileCacheManager;

/**
 * 分公司最新狀態
 * 倉位 PNL
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
		if("".equals(branch)) {
			log.warn("Please choose a branch.");
			return null;
		}
		
		boolean isSpecificProduct = !"".equals(product);
		
		log.debug("Analyze Branch Info ... From {} to {}", from, to);
		
		// TODO 要判斷CACHE的時間是否是輸入的範圍
//		NormalCacheManager cacheManager = null;
//		try {
//			cacheManager = fileCacheManager.getNormalCacheInstance(BranchFinalInfo.class.getSimpleName(), BranchFinalInfo.class.getName());
//		} catch (Exception e) {
//			log.error("Trying to get BranchFinalInfo cache failed.");
//			return null;
//		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		BranchFinalInfo branchFinalInfo = new BranchFinalInfo();
		branchFinalInfo.setFrom(from);
		branchFinalInfo.setTo(to);
		
		if(isSpecificProduct) {
			try {
				Map<Date, List<BuySellInfo>> map = dataSource.getBuySellInfo(product, from, to);
				
				while(!to.before(currentDate)) {
					List<BuySellInfo> lst = map.get(currentDate);
					
					for(BuySellInfo info : lst) {
						if(branch.equals(info.getBrokerName())) {
							updateBranchFinalInfo(info, branchFinalInfo);
						}
					}
					calendar.add(Calendar.DATE, 1);
					currentDate = calendar.getTime();
				}
			} catch (Exception e) {
				log.error("Trying to get BuySellInfo failed.");
				return null;
			}
		} else {
			try {
				Map<Date, Map<String, List<BuySellInfo>>> map = dataSource.getBuySellInfo(from, to);
				while(!to.before(currentDate)) {
					Map<String, List<BuySellInfo>> mapData = map.get(currentDate);
					for(String keyProduct : mapData.keySet()) {
						List<BuySellInfo> lst = mapData.get(keyProduct);
						for(BuySellInfo info : lst) {
							if(branch.equals(info.getBrokerName())) {
								updateBranchFinalInfo(info, branchFinalInfo);
							}
						}
					}
					calendar.add(Calendar.DATE, 1);
					currentDate = calendar.getTime();
				}
			} catch (Exception e) {
				log.error("Trying to get BuySellInfo failed.");
				return null;
			}
		}
		
		return null;
	}
	
	private void updateBranchFinalInfo(BuySellInfo bsInfo, BranchFinalInfo finalInfo) {
		String branch = bsInfo.getBrokerName();
		String product = bsInfo.getProduct();
		double pos = finalInfo.getPosition(branch, product);
		if(bsInfo.getBuyShare() > 0) {
			if(pos >= 0) {
				pos += bsInfo.getBuyShare();
			} else {
				
			}
		} 
		if(bsInfo.getSellShare() > 0) {
			if(pos <= 0) {
				pos -= bsInfo.getSellShare();
			} else {
				
			}
		}
	}
}
