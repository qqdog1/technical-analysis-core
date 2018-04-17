package name.qd.analysis.chip.utils;

import java.util.List;
import java.util.Map;

import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.vo.BuySellInfo;

public class ChipUtils {

	// lstInfo most be same product, same day (single file)
	public static void bsInfoToOperate(List<BuySellInfo> lstInfo, Map<String, DailyOperate> map) {
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
		operate.setPnl(operate.getPnl() + pnl);
		operate.setPnlRate();
		info.setBuyShare(info.getBuyShare() - closeVolume);
	}
	
	private static void sellClose(BuySellInfo info, DailyOperate operate, long closeVolume) {
		operate.setTradeCost(operate.getTradeCost() + operate.getAvgPrice() * closeVolume);
		operate.setOpenShare(operate.getOpenShare() - closeVolume);
		double pnl = (info.getPrice() - operate.getAvgPrice()) * closeVolume;
		operate.setPnl(operate.getPnl() + pnl);
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
}
