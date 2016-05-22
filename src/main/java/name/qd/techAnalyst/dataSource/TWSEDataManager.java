package name.qd.techAnalyst.dataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TWSEDataManager {
	private String sFilePath;
	private TWSEDataPoller poller;
	private TWSEDataParser parser;
	
	public TWSEDataManager(String sFilePath) {
		this.sFilePath = sFilePath;
		poller = new TWSEDataPoller(sFilePath);
		parser = new TWSEDataParser(sFilePath);
	}
	
	public void checkDateAndDownload(String sFrom, String sTo, String sProdId) throws ParseException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<String> lst = TimeUtil.getYearMonthBetween(sdf.parse(sFrom), sdf.parse(sTo));
		for(String sYearMonth : lst) {
			File file = new File(sFilePath + sYearMonth + "_" + sProdId + ".csv");
			if(!file.exists()) {
				poller.downloadFile(sYearMonth, sProdId);
			}
		}
		poller.downloadFile(lst.get(lst.size() - 1), sProdId);
	}
	
	public ArrayList<ProdClosingInfo> getProdClosingInfo(String sFrom, String sTo, String sProdId) throws ParseException, FileNotFoundException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<String> lstYearMonth = TimeUtil.getYearMonthBetween(sdf.parse(sFrom), sdf.parse(sTo));
		ArrayList<ProdClosingInfo> lstProd = new ArrayList<ProdClosingInfo>();
		for(String sYearMonth : lstYearMonth) {
			File file = new File(sFilePath + sYearMonth + "_" + sProdId + ".csv");
			if(file.exists()) {
				lstProd.addAll(parser.readProdClosingInfo(sYearMonth, sProdId));
			}
		}
		return lstProd;
	}
}
