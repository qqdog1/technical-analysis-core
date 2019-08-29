package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class BranchInfoByBS implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BranchInfoByBS.class);
	
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
					log.debug("Processing ... {}", currentDate);
					List<BuySellInfo> lst = map.get(currentDate);
					if(lst != null) {
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
				log.error("Trying to get BuySellInfo failed.", e);
				return null;
			}
		} else {
			try {
				Map<Date, Map<String, List<BuySellInfo>>> map = dataSource.getBuySellInfo(from, to);
				while(!to.before(currentDate)) {
					log.debug("Processing ... {}", currentDate);
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
				log.error("Trying to get BuySellInfo failed.", e);
				return null;
			}
		}
		
		List<List<String>> lst = new ArrayList<>();
		if(isSpecificProduct) {
			List<String> lstData = new ArrayList<>();
			lstData.add(branch);
			lstData.add(product);
			lstData.add(String.valueOf(branchFinalInfo.getPosition(branch, product)));
			lstData.add(String.valueOf(branchFinalInfo.getAvgPrice(branch, product)));
			lstData.add(String.valueOf(branchFinalInfo.getPnl(branch, product)));
			lstData.add(String.valueOf(branchFinalInfo.getPositionDiff(branch, product)));		
			lst.add(lstData);
		} else {
			Set<String> setProducts = branchFinalInfo.getAllProduct(branch);
			for(String p : setProducts) {
				List<String> lstData = new ArrayList<>();
				lstData.add(branch);
				lstData.add(p);
				lstData.add(String.valueOf(branchFinalInfo.getPosition(branch, p)));
				lstData.add(String.valueOf(branchFinalInfo.getAvgPrice(branch, p)));
				lstData.add(String.valueOf(branchFinalInfo.getPnl(branch, p)));
				lstData.add(String.valueOf(branchFinalInfo.getPositionDiff(branch, p)));		
				lst.add(lstData);
			}
		}
		
		return lst;
	}
	
	private void updateBranchFinalInfo(BuySellInfo bsInfo, BranchFinalInfo finalInfo) {
		String branch = bsInfo.getBrokerName();
		String product = bsInfo.getProduct();
		double pos = finalInfo.getPosition(branch, product);
		double cost = pos * finalInfo.getAvgPrice(branch, product);
		if(bsInfo.getBuyShare() > 0) {
			if(pos >= 0) {
				pos += bsInfo.getBuyShare();
				cost += bsInfo.getBuyShare() * bsInfo.getPrice();
				
				finalInfo.setAvgPrice(branch, product, cost/pos);
				finalInfo.setPosition(branch, product, pos);
				finalInfo.setPositionDiff(branch, product, bsInfo.getBuyShare());
			} else {
				double newPnl;
				double newPos = bsInfo.getBuyShare() + pos;
				if(bsInfo.getBuyShare() >= Math.abs(pos)) {
					newPnl = (finalInfo.getAvgPrice(branch, product) - bsInfo.getPrice()) * Math.abs(pos);
					finalInfo.setAvgPrice(branch, product, bsInfo.getPrice());
				} else {
					newPnl = (finalInfo.getAvgPrice(branch, product) - bsInfo.getPrice()) * bsInfo.getBuyShare();
				}
				finalInfo.setPnl(branch, product, finalInfo.getPnl(branch, product) + newPnl);
				finalInfo.setPosition(branch, product, newPos);
				finalInfo.setPositionDiff(branch, product, bsInfo.getBuyShare());
			}
		} 
		if(bsInfo.getSellShare() > 0) {
			if(pos <= 0) {
				pos -= bsInfo.getSellShare();
				cost -= bsInfo.getSellShare() * bsInfo.getPrice();
				
				finalInfo.setAvgPrice(branch, product, cost/pos);
				finalInfo.setPosition(branch, product, pos);
				finalInfo.setPositionDiff(branch, product, -bsInfo.getSellShare());
			} else {
				double newPnl;
				double newPos = pos - bsInfo.getSellShare();
				if(bsInfo.getSellShare() >= pos) {
					newPnl = (bsInfo.getPrice() - finalInfo.getAvgPrice(branch, product)) * pos;
					finalInfo.setAvgPrice(branch, product, bsInfo.getPrice());
				} else {
					newPnl = (bsInfo.getPrice() - finalInfo.getAvgPrice(branch, product)) * bsInfo.getSellShare();
				}
				finalInfo.setPnl(branch, product, finalInfo.getPnl(branch, product) + newPnl);
				finalInfo.setPosition(branch, product, newPos);
				finalInfo.setPositionDiff(branch, product, -bsInfo.getSellShare());
			}
		}
	}
}
