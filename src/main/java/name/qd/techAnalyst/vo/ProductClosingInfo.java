package name.qd.techAnalyst.vo;

import java.util.Date;

public class ProductClosingInfo {
	private Date date;
	private long filledShare;
	private double openPrice;
	private double closePrice;
	private double upperPrice;
	private double lowerPrice;
	private double avgPrice;
	private double filledAmount;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getFilledShare() {
		return filledShare;
	}
	public void setFilledShare(long filledShare) {
		this.filledShare = filledShare;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}
	public double getUpperPrice() {
		return upperPrice;
	}
	public void setUpperPrice(double upperPrice) {
		this.upperPrice = upperPrice;
	}
	public double getLowerPrice() {
		return lowerPrice;
	}
	public void setLowerPrice(double lowerPrice) {
		this.lowerPrice = lowerPrice;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	public double getFilledAmount() {
		return filledAmount;
	}
	public void setFilledAmount(double filledAmount) {
		this.filledAmount = filledAmount;
	}
}
