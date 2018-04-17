package name.qd.analysis.chip.vo;

import java.util.Date;

public class DailyOperate {
	private Date date;
	private String brokerName;
	private String product;
	private double pnl;
	private double tradeCost;
	private long openShare;
	private double avgPrice;
	private double pnlRate;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public double getPnl() {
		return pnl;
	}
	public void setPnl(double pnl) {
		this.pnl = pnl;
	}
	public double getTradeCost() {
		return tradeCost;
	}
	public void setTradeCost(double tradeCost) {
		this.tradeCost = tradeCost;
	}
	public long getOpenShare() {
		return openShare;
	}
	public void setOpenShare(long openShare) {
		this.openShare = openShare;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	public double getPnlRate() {
		return pnlRate;
	}
	public void setPnlRate() {
		pnlRate = pnl/tradeCost;
	}
}
