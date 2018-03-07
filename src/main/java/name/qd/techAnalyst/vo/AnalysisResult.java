package name.qd.techAnalyst.vo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import name.qd.fileCache.cache.FileCacheObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;
import name.qd.techAnalyst.Constants.Action;
import name.qd.techAnalyst.util.TimeUtil;

public class AnalysisResult implements FileCacheObject {
	private SimpleDateFormat sdf = TimeUtil.getDateTimeFormat();
	private Date date;
	private List<Double> values;
	private Action action = Action.NONE;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<Double> getValue() {
		return values;
	}
	public void setValue(List<Double> values) {
		this.values = values;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	@Override
	public byte[] parseToFileFormat() throws IOException {
		TransOutputStream tOut = new TransOutputStream();
		tOut.writeLong(date.getTime());
		tOut.writeInt(values.size());
		for(double d : values) {
			tOut.writeDouble(d);
		}
		tOut.writeString(action.name());
		return tOut.toByteArray();
	}
	@Override
	public void toValueObject(byte[] bData) throws IOException {
		TransInputStream tIn = new TransInputStream(bData);
		long timestamp = tIn.getLong();
		int size = tIn.getInt();
		values = new ArrayList<>();
		for(int i = 0 ; i < size; i++) {
			values.add(tIn.getDouble());
		}
		String act = tIn.getString();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		date = calendar.getTime();
		action = Action.valueOf(act);
	}
	@Override
	public String getKeyString() {
		return sdf.format(date);
	}
}
