package name.qd.analysis.tech.vo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import name.qd.analysis.utils.TimeUtils;
import name.qd.fileCache.cache.NormalObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;

public class AnalysisResult extends NormalObject {
	private SimpleDateFormat sdf = TimeUtils.getDateTimeFormat();
	private Date date;
	private List<Double> values = new ArrayList<>();
	
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
	public void setValue(double value) {
		values.add(value);
	}
	@Override
	public byte[] parseToFileFormat() throws IOException {
		TransOutputStream tOut = new TransOutputStream();
		tOut.writeLong(date.getTime());
		tOut.writeInt(values.size());
		for(double d : values) {
			tOut.writeDouble(d);
		}
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
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		date = calendar.getTime();
	}
	@Override
	public String getKeyString() {
		return sdf.format(date);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Date:").append(getKeyString());
		sb.append("Values:").append(values);
		return sb.toString();
	}
}
