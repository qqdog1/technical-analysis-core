package name.qd.analysis.dataSource.TWSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import name.qd.analysis.utils.TimeUtil;
import name.qd.analysis.vo.BuySellInfo;
import name.qd.analysis.vo.DailyClosingInfo;
import name.qd.analysis.vo.ProductClosingInfo;

public class TWSEDataSource implements DataSource {
	private Logger log = LoggerFactory.getLogger(TWSEDataSource.class);
	private TWSEDataPoller poller;
	private TWSEDataParser parser;
	
	public TWSEDataSource() {
		poller = new TWSEDataPoller2018(new TWSEDataPoller2016(null));
		parser = new TWSEDataParser();
		
		initFolder();
		
		log.info("TWSE data source path:[{}]", TWSEConstants.FILE_DIR);
	}
	
	private void initFolder() {
		checkFolderExist(TWSEConstants.FILE_DIR);
		checkFolderExist(TWSEConstants.FILE_DIR + TWSEConstants.DAILY_CLOSING_INFO_DIR);
	}
	
	@Override
	public List<ProductClosingInfo> getProductClosingInfo(String product, Date from, Date to) throws Exception {
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		checkAndDownloadDailyClosing(lstDate);
		ArrayList<ProductClosingInfo> lstProd = new ArrayList<ProductClosingInfo>();
		for(String date : lstDate) {
			File file = new File(TWSEConstants.getDailyClosingFilePath(date));
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
			File file = new File(TWSEConstants.getDailyClosingFilePath(date));
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
			File file = new File(TWSEConstants.getDailyClosingFilePath(date));
			if(file.exists()) {
				List<ProductClosingInfo> lstProd = parser.readAllNormalStock(date);
				map.put(sdf.parse(date), lstProd);
			}
		}
		return map;
	}
	
	@Override
	public List<BuySellInfo> getBuySellInfo(Date date, String product) throws Exception {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		String dateString = sdf.format(date);
		File file = new File(TWSEConstants.getBuySellInfoFilePath(dateString, product));
		if(file.exists()) {
			return parser.getBuySellInfo(product, dateString);
		}
		return new ArrayList<>();
	}
	
	private void checkAndDownloadDailyClosing(List<String> lstDate) throws IOException {
		for(int i = 0 ; i < lstDate.size() ; i++) {
			File file = new File(TWSEConstants.getDailyClosingFilePath(lstDate.get(i)));
			if(!file.exists()) {
				checkFolderExist(TWSEConstants.getDailyClosingFolder(lstDate.get(i).substring(0, 4)));
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
	
	private void checkFolderExist(String folderPath) {
		Path path = new File(folderPath).toPath();
		if(!Files.exists(path)) {
			try {
				Files.createDirectory(path);
				log.info("Create folder. {}", folderPath);
			} catch (IOException e) {
				log.error("Create folder failed. {}", folderPath);
			}
		}
	}
}
