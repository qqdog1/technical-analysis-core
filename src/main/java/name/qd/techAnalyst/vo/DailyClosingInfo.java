package name.qd.techAnalyst.vo;

import java.util.Date;

public class DailyClosingInfo {
	private Date date;
	private int advance;
	private int decline;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date sDate) {
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
