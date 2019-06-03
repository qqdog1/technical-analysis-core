package name.qd.analysis.dataSource.TWSE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.dataSource.vo.DailyClosingInfo;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.utils.StringCombineUtil;
import name.qd.analysis.utils.TimeUtil;

public class TWSEDataParser {
	private static Logger log = LoggerFactory.getLogger(TWSEDataParser.class);
	
	public TWSEDataParser() {
	}
	
	public ProductClosingInfo readProdClosingInfo(String date, String prodId) throws FileNotFoundException, IOException, ParseException {
		ProductClosingInfo prodInfo = null;
		prodId = StringCombineUtil.combine("\"", prodId, "\"");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TWSEConstants.getDailyClosingFilePath(date)), "Big5"))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(line.contains(prodId)) {
					List<String> lst = parseLineToArray(line);
					prodInfo = parseProductCloseInfo(lst, date);
				}
			}
		}
		return prodInfo;
	}
	
	public DailyClosingInfo readDailyClosingInfo(String date) throws FileNotFoundException, IOException, ParseException {
		DailyClosingInfo dailyClosingInfo = null;
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TWSEConstants.getDailyClosingFilePath(date)), "Big5"))) {
			for(String line; (line = br.readLine()) != null; ) {
				List<String> lst = parseLineToArray(line);
				if(lst == null) return null;
				if(line.contains(TWSEConstants.ADVANCE)) {
					dailyClosingInfo = new DailyClosingInfo();
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
	
	public List<ProductClosingInfo> readAllNormalStock(String date) throws FileNotFoundException, IOException, ParseException {
		List<ProductClosingInfo> lst = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TWSEConstants.getDailyClosingFilePath(date)), "Big5"))) {
			boolean start = false;
			while(true) {
				String line = br.readLine();
				if(line == null) {
					break;
				}
				
				List<String> lstValue = parseLineToArray(line);
				if(!isNormalStock(lstValue)) continue;
				
				if(start) {
					ProductClosingInfo info = parseProductCloseInfo(lstValue, date);
					if(info != null) {
						lst.add(info);
					}
				}
				
				if(line.contains("\"1101\"")) {
					start = true;
					ProductClosingInfo info = parseProductCloseInfo(lstValue, date);
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
	
	public Map<String, List<BuySellInfo>> getBuySellInfo(String date) throws IOException {
		Map<String, List<BuySellInfo>> map = new HashMap<>();
		
		Path path = Paths.get(TWSEConstants.getBuySellInfoFolder(date));
		
		if(Files.exists(path)) {
			Files.walk(path).forEach(p->{
				if(!Files.isDirectory(p)) {
					List<BuySellInfo> lst;
					try {
						String product = p.getFileName().toString().split("\\.")[0];
						lst = getBuySellInfo(p, date, product);
						if(lst.size() > 0) {
							map.put(product, lst);
						}
					} catch (IOException | ParseException e) {
						log.error("Read file failed. {}", p, e);
					}
				}
			});
		}
		return map;
	}
	
	private List<BuySellInfo> getBuySellInfo(Path path, String date, String product) throws IOException, ParseException {
		List<BuySellInfo> lst = new ArrayList<>();
		List<String> lstLine = Files.readAllLines(path, Charset.forName("Windows-950"));
		
		boolean start = false;
		for(String line : lstLine) {
			if(line.startsWith("1")) {
				start = true;
			}
			
			if(start) {
				setBuySellInfo(lst, TimeUtil.getDateFormat().parse(date), product, line);
			}
		}
		return lst;
	}
	
	public List<BuySellInfo> getBuySellInfo(String product, String date) throws UnsupportedEncodingException, FileNotFoundException, ParseException, IOException {
		List<BuySellInfo> lst = new ArrayList<>();
		Date d = TimeUtil.getDateFormat().parse(date);
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TWSEConstants.getBuySellInfoFilePath(date, product)), "Big5"))) {
			boolean start = false;
			for(String line; (line = br.readLine()) != null; ) {
				if(line.startsWith("1")) {
					start = true;
				}
				
				if(start) {
					setBuySellInfo(lst, d, product, line);
				}
			}
		}
		return lst;
	}
	
	private void setBuySellInfo(List<BuySellInfo> lst, Date date, String product, String line) {
		String[] s = line.split(",");
		
		BuySellInfo info1 = new BuySellInfo();
		info1.setDate(date);
		info1.setProduct(product);
		info1.setSeqNo(Integer.parseInt(s[0].trim()));
		info1.setBrokerName(s[1].trim());
		info1.setPrice(Double.parseDouble(s[2].trim()));
		info1.setBuyShare(Long.parseLong(s[3].trim()));
		info1.setSellShare(Long.parseLong(s[4].trim()));
		lst.add(info1);
		
		if(!"".equals(s[6].trim())) {
			BuySellInfo info2 = new BuySellInfo();
			info2.setDate(date);
			info2.setProduct(product);
			info2.setSeqNo(Integer.parseInt(s[6].trim()));
			info2.setBrokerName(s[7].trim());
			info2.setPrice(Double.parseDouble(s[8].trim()));
			info2.setBuyShare(Long.parseLong(s[9].trim()));
			info2.setSellShare(Long.parseLong(s[10].trim()));
			lst.add(info2);
		}
	}
	
	private ProductClosingInfo parseProductCloseInfo(List<String> lst, String date) {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		ProductClosingInfo prodInfo = new ProductClosingInfo();
		try {
			prodInfo.setDate(sdf.parse(date));
			prodInfo.setProduct(lst.get(0));
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
			} else if(" ".equals(lst.get(9))){
				prodInfo.setADStatus(ProductClosingInfo.UNCHANGE);
			} else {
				return null;
			}
		} catch (ParseException | NumberFormatException e) {
			log.error("Parse data failed. Date:[{}] [{}]", date, lst, e);
		}
		return prodInfo;
	}
	
	private boolean isNormalStock(List<String> lst) {
		if(lst == null || lst.size() < 16) return false;
		if(lst.get(0).length() > 4) return false;
		if(lst.get(1).contains("DR")) return false;
		return true;
	}
	
	private List<String> parseLineToArray(String line) {
		List<String> lst = new ArrayList<String>();
		String[] ss = line.split("\",\"");
		for(String s : ss) {
			lst.add(s.replace("\"", "").replace(",", ""));
		}
		return lst;
	}
}
