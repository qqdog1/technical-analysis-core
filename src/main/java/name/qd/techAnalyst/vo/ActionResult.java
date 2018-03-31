package name.qd.techAnalyst.vo;

import java.util.Date;

import name.qd.techAnalyst.Constants.Action;

public class ActionResult {
	private Date date;
	private Action action = Action.NONE;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
}
