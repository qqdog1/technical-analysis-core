package name.qd.techAnalyst.vo;

public class DailyClosingInfo {
	private String date;
	private int advance;
	private int decline;
	
	public String getDate() {
		return date;
	}
	public void setDate(String sDate) {
		this.date = sDate;
	}
	public int getAdvance() {
		return advance;
	}
	public void setAdvance(int iAdvance) {
		this.advance = iAdvance;
	}
	public int getDecline() {
		return decline;
	}
	public void setDecline(int iDecline) {
		this.decline = iDecline;
	}
}
