package name.qd.analysis.dataSource.TWSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.TWSE.utils.TWSEPathUtil;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.dataSource.vo.DailyClosingInfo;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.utils.TimeUtil;

public class TWSEDataSource implements DataSource {
	private Logger log = LoggerFactory.getLogger(TWSEDataSource.class);
	private TWSEDataPoller poller;
	private TWSEDataParser parser;
	private final String baseFolder;
	
	public TWSEDataSource(String baseFolder) {
		this.baseFolder = baseFolder;
		poller = new TWSEDataPoller2018(new TWSEDataPoller2016(null, baseFolder), baseFolder);
		parser = new TWSEDataParser(baseFolder);
		
		log.info("TWSE data source path:[{}]", baseFolder);
	}
	
	@Override
	public List<ProductClosingInfo> getProductClosingInfo(String product, Date from, Date to) throws Exception {
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		checkAndDownloadDailyClosing(lstDate);
		ArrayList<ProductClosingInfo> lstProd = new ArrayList<ProductClosingInfo>();
		for(String date : lstDate) {
			File file = new File(TWSEPathUtil.getDailyClosingFilePath(baseFolder, date).toString());
			if(file.exists()) {
				ProductClosingInfo prodInfo = parser.readProdClosingInfo(date, product);
				if(prodInfo != null) {
					lstProd.add(prodInfo);
				}
			}
		}
		return lstProd;
	}
	
	@Override
	public List<DailyClosingInfo> getDailyClosingInfo(Date from, Date to) throws ParseException, FileNotFoundException, IOException {
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		checkAndDownloadDailyClosing(lstDate);
		ArrayList<DailyClosingInfo> lstInfo = new ArrayList<DailyClosingInfo>();
		for(String date : lstDate) {
			File file = new File(TWSEPathUtil.getDailyClosingFilePath(baseFolder, date).toString());
			if(file.exists()) {
				DailyClosingInfo dailyClosingInfo = parser.readDailyClosingInfo(date);
				if(dailyClosingInfo != null) {
					lstInfo.add(dailyClosingInfo);
				}
			}
		}
		return lstInfo;
	}
	
	@Override
	public Map<Date, List<ProductClosingInfo>> getAllProductClosingInfo(Date from, Date to) throws Exception {
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		checkAndDownloadDailyClosing(lstDate);
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		Map<Date, List<ProductClosingInfo>> map = new HashMap<>();
		for(String date : lstDate) {
			File file = new File(TWSEPathUtil.getDailyClosingFilePath(baseFolder, date).toString());
			if(file.exists()) {
				List<ProductClosingInfo> lstProd = parser.readAllStock(date);
				map.put(sdf.parse(date), lstProd);
			}
		}
		return map;
	}
	
	@Override
	public Map<Date, List<BuySellInfo>> getBuySellInfo(String product, Date from, Date to) throws Exception {
		Map<Date, List<BuySellInfo>> map = new HashMap<>();
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		for(String date : lstDate) {
			File file = new File(TWSEPathUtil.getBuySellInfoFilePath(baseFolder, date, product).toString());
			if(file.exists()) {
				List<BuySellInfo> lst = parser.getBuySellInfo(product, date);
				map.put(sdf.parse(date), lst);
			}
		}
		return map;
	}
	
	@Override
	public Map<Date, Map<String, List<BuySellInfo>>> getBuySellInfo(Date from, Date to) throws Exception {
		Map<Date, Map<String, List<BuySellInfo>>> map = new HashMap<>();
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		for(String date : lstDate) {
			Map<String, List<BuySellInfo>> mapProduct = parser.getBuySellInfo(date);
			map.put(TimeUtil.getDateFormat().parse(date), mapProduct);
		}
		return map;
	}
	
	private void checkAndDownloadDailyClosing(List<String> lstDate) throws IOException {
		for(int i = 0 ; i < lstDate.size() ; i++) {
			File file = new File(TWSEPathUtil.getDailyClosingFilePath(baseFolder, lstDate.get(i)).toString());
			if(!file.exists()) {
				checkFolderExist(TWSEPathUtil.getDailyClosingFolder(baseFolder, lstDate.get(i).substring(0, 4)));
				log.info("Download daily closing info. {}", lstDate.get(i));
				poller.downloadDailyClosingInfo(lstDate.get(i));
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void checkFolderExist(Path path) {
		if(!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				log.info("Create folder. {}", path);
			} catch (IOException e) {
				log.error("Create folder failed. {}", path, e);
			}
		}
	}
}
