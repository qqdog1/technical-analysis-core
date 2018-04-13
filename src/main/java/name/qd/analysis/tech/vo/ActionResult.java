package name.qd.analysis.tech.vo;

import java.util.Date;

import name.qd.analysis.Constants.Action;

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
