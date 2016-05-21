package name.qd.techAnalyst.vo;

public class ProdDailyClosingInfo {
	private String sDate;
	private long lFilledShare;
	private double dOpenPrice;
	private double dClosePrice;
	private double dUpperPrice;
	private double dLowerPrice;
	
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public long getlFilledShare() {
		return lFilledShare;
	}
	public void setlFilledShare(long lFilledShare) {
		this.lFilledShare = lFilledShare;
	}
	public double getdOpenPrice() {
		return dOpenPrice;
	}
	public void setdOpenPrice(double dOpenPrice) {
		this.dOpenPrice = dOpenPrice;
	}
	public double getdClosePrice() {
		return dClosePrice;
	}
	public void setdClosePrice(double dClosePrice) {
		this.dClosePrice = dClosePrice;
	}
	public double getdUpperPrice() {
		return dUpperPrice;
	}
	public void setdUpperPrice(double dUpperPrice) {
		this.dUpperPrice = dUpperPrice;
	}
	public double getdLowerPrice() {
		return dLowerPrice;
	}
	public void setdLowerPrice(double dLowerPrice) {
		this.dLowerPrice = dLowerPrice;
	}
}
