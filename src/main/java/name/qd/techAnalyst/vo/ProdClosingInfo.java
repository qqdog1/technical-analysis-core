package name.qd.techAnalyst.vo;

public class ProdClosingInfo {
	private String date;
	private long filledShare;
	private double openPrice;
	private double closePrice;
	private double upperPrice;
	private double lowerPrice;
	private double avgPrice;
	private double filledAmount;
	
	public String getDate() {
		return date;
	}
	public void setDate(String sDate) {
		this.date = sDate;
	}
	public long getFilledShare() {
		return filledShare;
	}
	public void setFilledShare(long lFilledShare) {
		this.filledShare = lFilledShare;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double dOpenPrice) {
		this.openPrice = dOpenPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(double dClosePrice) {
		this.closePrice = dClosePrice;
	}
	public double getUpperPrice() {
		return upperPrice;
	}
	public void setUpperPrice(double dUpperPrice) {
		this.upperPrice = dUpperPrice;
	}
	public double getLowerPrice() {
		return lowerPrice;
	}
	public void setLowerPrice(double dLowerPrice) {
		this.lowerPrice = dLowerPrice;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double dAvgPrice) {
		this.avgPrice = dAvgPrice;
	}
	public double getFilledAmount() {
		return filledAmount;
	}
	public void setFilledAmount(double dFilledAmount) {
		this.filledAmount = dFilledAmount;
	}
}
