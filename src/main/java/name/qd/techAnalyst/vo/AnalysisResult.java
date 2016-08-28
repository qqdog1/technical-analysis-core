package name.qd.techAnalyst.vo;

import java.io.IOException;

import name.qd.fileCache.cache.IFileCacheObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;

public class AnalysisResult implements IFileCacheObject {
	private String date;
	private double value;
	private String action;
	
	public String getDate() {
		return date;
	}
	public void setDate(String sDate) {
		this.date = sDate;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double dValue) {
		this.value = dValue;
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
		tOut.writeString(date);
		tOut.writeDouble(value);
		return tOut.toByteArray();
	}
	@Override
	public void toValueObject(byte[] bData) throws IOException {
		TransInputStream tIn = new TransInputStream(bData);
		date = tIn.getString();
		value = tIn.getDouble();
	}
	@Override
	public String getKeyString() {
		return date;
	}
}
