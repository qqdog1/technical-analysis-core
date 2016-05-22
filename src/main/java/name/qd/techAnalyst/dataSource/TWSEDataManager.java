package name.qd.techAnalyst.dataSource;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import name.qd.techAnalyst.util.TimeUtil;

public class TWSEDataManager {
	private String sFilePath;
	private TWSEDataPoller poller;
	
	public TWSEDataManager(String sFilePath) {
		this.sFilePath = sFilePath;
		poller = new TWSEDataPoller(sFilePath);
	}
	
	public void checkDataAndDownload(String sFrom, String sTo, String sProdId) throws ParseException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<String> lst = TimeUtil.getYearMonthBetween(sdf.parse(sFrom), sdf.parse(sTo));
		for(String sYearMonth : lst) {
			File file = new File(sFilePath + sYearMonth + "_" + sProdId + ".csv");
			if(!file.exists()) {
				poller.downloadFile(sYearMonth, sProdId);
			}
		}
		poller.downloadFile(lst.get(lst.size()), sProdId);
	}
}
