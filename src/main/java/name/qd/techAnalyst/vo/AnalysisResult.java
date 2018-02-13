package name.qd.techAnalyst.vo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import name.qd.fileCache.cache.FileCacheObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;
import name.qd.techAnalyst.util.TimeUtil;

public class AnalysisResult implements FileCacheObject {
	private SimpleDateFormat sdf = TimeUtil.getDateTimeFormat();
	private Date date;
	private double value;
	private String action;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public byte[] parseToFileFormat() throws IOException {
		TransOutputStream tOut = new TransOutputStream();
		tOut.writeLong(date.getTime());
		tOut.writeDouble(value);
		return tOut.toByteArray();
	}
	@Override
	public void toValueObject(byte[] bData) throws IOException {
		TransInputStream tIn = new TransInputStream(bData);
		long timestamp = tIn.getLong();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		date = calendar.getTime();
		value = tIn.getDouble();
	}
	@Override
	public String getKeyString() {
		return sdf.format(date);
	}
}
