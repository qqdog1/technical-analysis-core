package name.qd.techAnalyst.vo;

import java.io.IOException;

import name.qd.fileCache.cache.IFileCacheObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;

public class AnalysisResult implements IFileCacheObject {
	private String sDate;
	private double dValue;
	
	public String getDate() {
		return sDate;
	}
	public void setDate(String sDate) {
		this.sDate = sDate;
	}
	public double getValue() {
		return dValue;
	}
	public void setValue(double dValue) {
		this.dValue = dValue;
	}
	@Override
	public byte[] parseToFileFormat() throws IOException {
		TransOutputStream tOut = new TransOutputStream();
		tOut.writeString(sDate);
		tOut.writeDouble(dValue);
		return tOut.toByteArray();
	}
	@Override
	public void toValueObject(byte[] bData) throws IOException {
		TransInputStream tIn = new TransInputStream(bData);
		sDate = tIn.getString();
		dValue = tIn.getDouble();
	}
	@Override
	public String getKeyString() {
		return sDate;
	}
}
