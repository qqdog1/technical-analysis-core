package name.qd.techAnalyst.dataSource.TWSE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.util.FileConstUtil;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TWSEDataParser {
	
	public TWSEDataParser() {
	}
	
	public List<ProdClosingInfo> readProdClosingInfo(String year, String month, String prodId) throws FileNotFoundException, IOException {
		List<ProdClosingInfo> lst = new ArrayList<ProdClosingInfo>();
		String prefix = toTWSEDateFormat(year, month);
		try (BufferedReader br = new BufferedReader(new FileReader(FileConstUtil.getProdClosingFilePath(year, month, prodId)))) {
			for(String line; (line = br.readLine()) != null; ) {
				ProdClosingInfo prod = parse2ProdClosingInfo(line, prefix);
				if(prod != null) {
					lst.add(prod);
				}
			}
		}
		return lst;
	}
	
	public DailyClosingInfo readDailyClosingInfo(String date) throws FileNotFoundException, IOException, ParseException {
		DailyClosingInfo dailyClosingInfo = null;
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileConstUtil.getDailyClosingFilePath(date)), "Big5"))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(line.contains(FileConstUtil.ADVANCE)) {
					dailyClosingInfo = new DailyClosingInfo();
					List<String> lst = parseTWSEcsv(line);
					String sAdvance = lst.get(2).split("\\(")[0];
					dailyClosingInfo.setAdvance(Integer.parseInt(sAdvance));
					dailyClosingInfo.setDate(date);
				} else if(line.contains(FileConstUtil.DECLINE)) {
					List<String> lst = parseTWSEcsv(line);
					String decline = lst.get(2).split("\\(")[0];
					dailyClosingInfo.setDecline(Integer.parseInt(decline));
				}
			}
		}
		return dailyClosingInfo;
	}
	
	private String toTWSEDateFormat(String year, String month) {
		year = TimeUtil.AD2ROC(year);
		StringBuilder sb = new StringBuilder();
		sb.append(year).append("/").append(String.format("%1$02d", Integer.parseInt(month)));
		return sb.toString();
	}
	
	private ProdClosingInfo parse2ProdClosingInfo(String sData, String sPrefix) {
		if(!sData.contains(sPrefix)) {
			return null;
		}
		List<String> lst = parseTWSEcsv(sData);
		
		ProdClosingInfo prod = new ProdClosingInfo();
		prod.setDate(TimeUtil.getOutputFromROC(lst.get(0)));
		prod.setFilledShare(Long.parseLong(lst.get(1)));
		prod.setFilledAmount(Double.parseDouble(lst.get(2)));
		prod.setOpenPrice(Double.parseDouble(lst.get(3)));
		prod.setUpperPrice(Double.parseDouble(lst.get(4)));
		prod.setLowerPrice(Double.parseDouble(lst.get(5)));
		prod.setClosePrice(Double.parseDouble(lst.get(6)));
		prod.setAvgPrice(prod.getFilledAmount() / prod.getFilledShare());
		return prod;
	}
	
	private List<String> parseTWSEcsv(String sData) {
		List<String> lst = new ArrayList<String>();
		while(sData.length() != 0) {
			int iIndex = 0;
			int iComma = sData.indexOf(",");
			int iQuotation = sData.indexOf("\"");
			if(iComma == 0) {
				iIndex = 1;
			} else if(iComma == -1 && iQuotation == -1) {
				lst.add(sData.trim());
				iIndex = sData.length();
			} else if (iQuotation == -1 || iComma < iQuotation) {
				lst.add(sData.substring(0, iComma).trim().replaceAll(",", ""));
				iIndex = iComma + 1;
			} else {
				iIndex = iQuotation + 1;
				iQuotation = sData.substring(iIndex, sData.length()).indexOf("\"");
				lst.add(sData.substring(iIndex, iQuotation + iIndex).trim().replaceAll(",", ""));
				iIndex = iQuotation + iIndex + 1;
			}
			sData = sData.substring(iIndex, sData.length());
		}
		return lst;
	}
}