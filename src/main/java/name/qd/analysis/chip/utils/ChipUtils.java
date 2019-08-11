package name.qd.analysis.chip.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;

public class ChipUtils {
	private static Logger log = LoggerFactory.getLogger(ChipUtils.class);
	
	// lstInfo must be same product, same day (single file)
	public static void bsInfoToOperate(DataSource dataSource, String product, Date date, List<BuySellInfo> lstInfo, Map<String, DailyOperate> map) throws Exception {
		for(BuySellInfo info : lstInfo) {
			String brokerName = info.getBrokerName();
			if(!map.containsKey(brokerName)) {
				map.put(brokerName, createDailyOperate(info));
			}
			
			DailyOperate operate = map.get(brokerName);
			if(info.getBuyShare() > 0) {
				if(operate.getOpenShare() >= 0) {
					// open
					buyOpen(info, operate);
				} else {
					if(Math.abs(operate.getOpenShare()) < info.getBuyShare()) {
						// close and open
						buyClose(info, operate, Math.abs(operate.getOpenShare()));
						buyOpen(info, operate);
					} else {
						// close
						buyClose(info, operate, info.getBuyShare());
					}
				}
			} else if(info.getSellShare() > 0) {
				if(operate.getOpenShare() <= 0) {
					// open
					sellOpen(info, operate);
				} else {
					if(operate.getOpenShare() < info.getSellShare()) {
						// close and open
						sellClose(info, operate, operate.getOpenShare());
						sellOpen(info, operate);
					} else {
						// close
						sellClose(info, operate, info.getSellShare());
					}
				}
			}
		}
		
		calcOpenPnl(dataSource, product, date, map);
	}
	
	public static DailyOperate updateDailyOperate(DailyOperate first, DailyOperate second) {
		DailyOperate dailyOperate = new DailyOperate();
		dailyOperate.setBrokerName(first.getBrokerName());
		dailyOperate.setProduct(first.getProduct());
		dailyOperate.setOpenShare(first.getOpenShare() + second.getOpenShare());
		dailyOperate.setClosePnl(first.getClosePnl() + second.getClosePnl());
		dailyOperate.setTradeCost(first.getTradeCost() + second.getTradeCost());
		double closePrice = (second.getOpenPnl() / second.getOpenShare()) + second.getAvgPrice();
		
		if((first.getOpenShare() > 0 && second.getOpenShare() > 0) || (first.getOpenShare() < 0 && second.getOpenShare() < 0)) {
			double firstCost = first.getOpenShare() * first.getAvgPrice();
			double secondCost = second.getOpenShare() * second.getAvgPrice();
			double totalCost = firstCost + secondCost;
			dailyOperate.setAvgPrice(totalCost / dailyOperate.getOpenShare());
		} else {
			long firstOpenShare = Math.abs(first.getOpenShare());
			long secondOpenShare = Math.abs(second.getOpenShare());
			if(firstOpenShare > secondOpenShare) {
				dailyOperate.setAvgPrice(first.getAvgPrice());
			} else {
				dailyOperate.setAvgPrice(second.getAvgPrice());
			}
			long closeShare = Math.min(firstOpenShare, secondOpenShare);
			double tradeCost = closeShare * first.getAvgPrice();
			dailyOperate.setTradeCost(dailyOperate.getTradeCost() + tradeCost);
			double closePnl = (second.getAvgPrice() - first.getAvgPrice()) * closeShare;
			dailyOperate.setClosePnl(dailyOperate.getClosePnl() + closePnl);
		}
		double newOpenPnl = (closePrice - dailyOperate.getAvgPrice()) * dailyOperate.getOpenShare();
		dailyOperate.setOpenPnl(newOpenPnl);
		dailyOperate.setPnlRate();
		
		return dailyOperate;
	}
	
	private static void buyOpen(BuySellInfo info, DailyOperate operate) {
		double cost = operate.getOpenShare() * operate.getAvgPrice();
		operate.setOpenShare(operate.getOpenShare() + info.getBuyShare());
		cost += info.getPrice() * info.getBuyShare();
		double avgPrice = cost / operate.getOpenShare();
		operate.setAvgPrice(avgPrice);
	}
	
	private static void sellOpen(BuySellInfo info, DailyOperate operate) {
		double cost = Math.abs(operate.getOpenShare()) * operate.getAvgPrice();
		operate.setOpenShare(operate.getOpenShare() - info.getSellShare());
		cost += info.getPrice() * info.getSellShare();
		double avgPrice = cost / Math.abs(operate.getOpenShare());
		operate.setAvgPrice(avgPrice);
	}
	
	private static void buyClose(BuySellInfo info, DailyOperate operate, long closeVolume) {
		operate.setTradeCost(operate.getTradeCost() + operate.getAvgPrice() * closeVolume);
		operate.setOpenShare(operate.getOpenShare() + closeVolume);
		double pnl = (operate.getAvgPrice() - info.getPrice()) * closeVolume;
		operate.setClosePnl(operate.getClosePnl() + pnl);
		operate.setPnlRate();
		info.setBuyShare(info.getBuyShare() - closeVolume);
	}
	
	private static void sellClose(BuySellInfo info, DailyOperate operate, long closeVolume) {
		operate.setTradeCost(operate.getTradeCost() + operate.getAvgPrice() * closeVolume);
		operate.setOpenShare(operate.getOpenShare() - closeVolume);
		double pnl = (info.getPrice() - operate.getAvgPrice()) * closeVolume;
		operate.setClosePnl(operate.getClosePnl() + pnl);
		operate.setPnlRate();
		info.setSellShare(info.getSellShare() - closeVolume);
	}
	
	private static DailyOperate createDailyOperate(BuySellInfo info) {
		DailyOperate operate = new DailyOperate();
		operate.setBrokerName(info.getBrokerName());
		operate.setDate(info.getDate());
		operate.setProduct(info.getProduct());
		return operate;
	}
	
	private static void calcOpenPnl(DataSource dataSource, String product, Date date, Map<String, DailyOperate> map) throws Exception {
		List<ProductClosingInfo> lst = dataSource.getProductClosingInfo(product, date, date);
		double closePrice = lst.get(0).getClosePrice();
		for(DailyOperate op : map.values()) {
			double openPnl = (closePrice - op.getAvgPrice()) * op.getOpenShare();
			op.setOpenPnl(openPnl);
		}
	}
}
