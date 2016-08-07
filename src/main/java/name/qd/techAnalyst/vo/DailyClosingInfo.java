package name.qd.techAnalyst.vo;

public class DailyClosingInfo {
	private String sDate;
	private int iAdvance;
	private int iDecline;
	
	public String getDate() {
		return sDate;
	}
	public void setDate(String sDate) {
		this.sDate = sDate;
	}
	public int getAdvance() {
		return iAdvance;
	}
	public void setAdvance(int iAdvance) {
		this.iAdvance = iAdvance;
	}
	public int getDecline() {
		return iDecline;
	}
	public void setDecline(int iDecline) {
		this.iDecline = iDecline;
	}
}
