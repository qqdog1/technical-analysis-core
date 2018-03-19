package name.qd.techAnalyst.dataSource.TWSE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.util.StringCombineUtil;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class TWSEDataParser {
	
	public TWSEDataParser() {
	}
	
	public List<ProductClosingInfo> readProdClosingInfo(String year, String month, String prodId) throws FileNotFoundException, IOException, ParseException {
		List<ProductClosingInfo> lst = new ArrayList<ProductClosingInfo>();
		String prefix = toTWSEDateFormat(year, month);
		try (BufferedReader br = new BufferedReader(new FileReader(TWSEConstants.getProdClosingFilePath(year, month, prodId)))) {
			for(String line; (line = br.readLine()) != null; ) {
				ProductClosingInfo prod = parse2ProdClosingInfo(line, prefix);
				if(prod != null) {
					lst.add(prod);
				}
			}
		}
		return lst;
	}
	
	public ProductClosingInfo readProdClosingInfo(String date, String prodId) throws FileNotFoundException, IOException, ParseException {
		ProductClosingInfo prodInfo = null;
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		prodId = StringCombineUtil.combine("\"", prodId, "\"");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TWSEConstants.getDailyClosingFilePath(date)), "Big5"))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(line.contains(prodId)) {
					List<String> lst = parseTWSEcsv(line);
					if(lst == null) continue;
					prodInfo = new ProductClosingInfo();
					prodInfo.setDate(sdf.parse(date));
					prodInfo.setFilledShare(Long.parseLong(lst.get(2)));
					prodInfo.setFilledAmount(Double.parseDouble(lst.get(4)));
					prodInfo.setOpenPrice(Double.parseDouble(lst.get(5)));
					prodInfo.setUpperPrice(Double.parseDouble(lst.get(6)));
					prodInfo.setLowerPrice(Double.parseDouble(lst.get(7)));
					prodInfo.setClosePrice(Double.parseDouble(lst.get(8)));
					prodInfo.setAvgPrice(prodInfo.getFilledAmount() / prodInfo.getFilledShare());
				}
			}
		}
		return prodInfo;
	}
	
	public DailyClosingInfo readDailyClosingInfo(String date) throws FileNotFoundException, IOException, ParseException {
		DailyClosingInfo dailyClosingInfo = new DailyClosingInfo();
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TWSEConstants.getDailyClosingFilePath(date)), "Big5"))) {
			for(String line; (line = br.readLine()) != null; ) {
				List<String> lst = parseTWSEcsv(line);
				if(lst == null) return null;
				if(line.contains(TWSEConstants.ADVANCE)) {
					String sAdvance = lst.get(2).split("\\(")[0];
					dailyClosingInfo.setAdvance(Integer.parseInt(sAdvance));
					dailyClosingInfo.setDate(sdf.parse(date));
				} else if(line.contains(TWSEConstants.DECLINE)) {
					String decline = lst.get(2).split("\\(")[0];
					dailyClosingInfo.setDecline(Integer.parseInt(decline));
				} else if(line.contains(TWSEConstants.UNCHANGED)) {
					String unchanged = lst.get(2).split("\\(")[0];
					dailyClosingInfo.setUnchanged(Integer.parseInt(unchanged));
				}
			}
		}
		return dailyClosingInfo;
	}
	
	public List<ProductClosingInfo> readAllProductClosingInfo(String date) throws FileNotFoundException, IOException, ParseException {
		List<ProductClosingInfo> lst = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TWSEConstants.getDailyClosingFilePath(date)), "Big5"))) {
			boolean start = false;
			while(true) {
				String line = br.readLine();
				if(line == null) {
					break;
				}
				
				if(start) {
					ProductClosingInfo info = parseProductCloseInfo(line, date);
					if(info != null) {
						lst.add(info);
					}
				}
				
				if(line.contains("\"1101\"")) {
					start = true;
					ProductClosingInfo info = parseProductCloseInfo(line, date);
					if(info != null) {
						lst.add(info);
					}
				} else {
					continue;
				}
			}
		}
		return lst;
	}
	
	private ProductClosingInfo parseProductCloseInfo(String line, String date) {
		List<String> lst = parseTWSEcsv(line);
		if(lst == null || lst.size() < 16) return null;
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		ProductClosingInfo prodInfo = new ProductClosingInfo();
		try {
			prodInfo = new ProductClosingInfo();
			prodInfo.setDate(sdf.parse(date));
			prodInfo.setFilledShare(Long.parseLong(lst.get(2)));
			prodInfo.setFilledAmount(Double.parseDouble(lst.get(4)));
			if("--".equals(lst.get(5))) {
				return null;
			}
			prodInfo.setOpenPrice(Double.parseDouble(lst.get(5)));
			prodInfo.setUpperPrice(Double.parseDouble(lst.get(6)));
			prodInfo.setLowerPrice(Double.parseDouble(lst.get(7)));
			prodInfo.setClosePrice(Double.parseDouble(lst.get(8)));
			prodInfo.setAvgPrice(prodInfo.getFilledAmount() / prodInfo.getFilledShare());
			if("+".equals(lst.get(9))) {
				prodInfo.setADStatus(ProductClosingInfo.ADVANCE);
			} else if("-".equals(lst.get(9))) {
				prodInfo.setADStatus(ProductClosingInfo.DECLINE);
			} else {
				prodInfo.setADStatus(ProductClosingInfo.UNCHANGE);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return prodInfo;
	}
	
	private String toTWSEDateFormat(String year, String month) {
		year = TimeUtil.AD2ROC(year);
		StringBuilder sb = new StringBuilder();
		sb.append(year).append("/").append(String.format("%1$02d", Integer.parseInt(month)));
		return sb.toString();
	}
	
	private ProductClosingInfo parse2ProdClosingInfo(String sData, String sPrefix) throws ParseException {
		if(!sData.contains(sPrefix)) {
			return null;
		}
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		List<String> lst = parseTWSEcsv(sData);
		if(lst == null) return null;
		ProductClosingInfo prod = new ProductClosingInfo();
		prod.setDate(sdf.parse(TimeUtil.getOutputFromROC(lst.get(0))));
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
			if(iComma == -1) {
				return null;
			}
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
