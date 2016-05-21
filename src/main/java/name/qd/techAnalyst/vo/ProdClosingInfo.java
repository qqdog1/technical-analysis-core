package name.qd.techAnalyst.vo;

public class ProdClosingInfo {
	private String sDate;
	private long lFilledShare;
	private double dOpenPrice;
	private double dClosePrice;
	private double dUpperPrice;
	private double dLowerPrice;
	
	public String getDate() {
		return sDate;
	}
	public void setDate(String sDate) {
		this.sDate = sDate;
	}
	public long getFilledShare() {
		return lFilledShare;
	}
	public void setFilledShare(long lFilledShare) {
		this.lFilledShare = lFilledShare;
	}
	public double getOpenPrice() {
		return dOpenPrice;
	}
	public void setOpenPrice(double dOpenPrice) {
		this.dOpenPrice = dOpenPrice;
	}
	public double getClosePrice() {
		return dClosePrice;
	}
	public void setClosePrice(double dClosePrice) {
		this.dClosePrice = dClosePrice;
	}
	public double getUpperPrice() {
		return dUpperPrice;
	}
	public void setUpperPrice(double dUpperPrice) {
		this.dUpperPrice = dUpperPrice;
	}
	public double getLowerPrice() {
		return dLowerPrice;
	}
	public void setLowerPrice(double dLowerPrice) {
		this.dLowerPrice = dLowerPrice;
	}
}
