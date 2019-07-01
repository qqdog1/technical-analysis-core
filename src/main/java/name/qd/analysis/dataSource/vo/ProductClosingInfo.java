package name.qd.analysis.dataSource.vo;

import java.util.Date;

public class ProductClosingInfo {
	public static int ADVANCE = 1;
	public static int DECLINE = 2;
	public static int UNCHANGE = 4;
	public static int CANT_COMPARE = 8;
	
	private String product;
	private Date date;
	private long filledShare;
	private double openPrice;
	private double closePrice;
	private double upperPrice;
	private double lowerPrice;
	private double avgPrice;
	private double filledAmount;
	private int ADStatus;
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
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
	public int getADStatus() {
		return ADStatus;
	}
	public void setADStatus(int ADStatus) {
		this.ADStatus = ADStatus;
	}
}
