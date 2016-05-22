package name.qd.techAnalyst.dataSource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TWSEDataParser {
	private String sFilePath;
	
	TWSEDataParser(String sFilePath) {
		this.sFilePath = sFilePath;
	}
	
	public List<ProdClosingInfo> readProdClosingInfo(String sYearMonth, String sProdId) throws FileNotFoundException, IOException {
		List<ProdClosingInfo> lst = new ArrayList<ProdClosingInfo>();
		String sPrefix = toTWSEDateFormat(sYearMonth);
		try (BufferedReader br = new BufferedReader(new FileReader(sFilePath + sYearMonth + "_" + sProdId + ".csv"))) {
			for(String line; (line = br.readLine()) != null; ) {
				ProdClosingInfo prod = parse2ProdClosingInfo(line, sPrefix);
				if(prod != null) {
					lst.add(prod);
				}
			}
		}
		return lst;
	}
	
	private String toTWSEDateFormat(String sYearMonth) {
		String sYear = sYearMonth.substring(0, 4);
		String sMonth = sYearMonth.substring(4, 6);
		sYear = TimeUtil.AD2ROC(sYear);
		return " " + sYear + "/" + sMonth;
	}
	
	private ProdClosingInfo parse2ProdClosingInfo(String sData, String sPrefix) {
		if(!sData.contains(sPrefix)) {
			return null;
		}
		List<String> lst = parseTWSEcsv(sData);
		
		ProdClosingInfo prod = new ProdClosingInfo();
		prod.setDate(lst.get(0));
		prod.setFilledShare(Long.parseLong(lst.get(1)));
		prod.setOpenPrice(Double.parseDouble(lst.get(3)));
		prod.setUpperPrice(Double.parseDouble(lst.get(4)));
		prod.setLowerPrice(Double.parseDouble(lst.get(5)));
		prod.setClosePrice(Double.parseDouble(lst.get(6)));
		return prod;
	}
	
	private List<String> parseTWSEcsv(String sData) {
		List<String> lst = new ArrayList<String>();
		while(sData.length() != 0) {
			int iIndex = 0;
			int iComma = sData.indexOf(",");
			int iQuotation = sData.indexOf("\"");
			if(iComma == -1 && iQuotation == -1) {
				lst.add(sData.trim());
				iIndex = sData.length();
			} else if (iQuotation == -1 || iComma < iQuotation) {
				lst.add(sData.substring(0, iComma).trim().replaceAll(",", ""));
				iIndex = iComma + 1;
			} else {
				iIndex = iQuotation + 1;
				iQuotation = sData.substring(iIndex, sData.length()).indexOf("\"");
				lst.add(sData.substring(iIndex, iQuotation + iIndex).trim().replaceAll(",", ""));
				iIndex = iQuotation + iIndex + 2;
			}
			sData = sData.substring(iIndex, sData.length());
		}
		return lst;
	}
}
