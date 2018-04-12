package name.qd.analysis.dataSource.TPEX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.TWSE.TWSEDataSource;
import name.qd.analysis.vo.BuySellInfo;
import name.qd.analysis.vo.DailyClosingInfo;
import name.qd.analysis.vo.ProductClosingInfo;

public class TPEXDataSource implements DataSource {
	private Logger log = LoggerFactory.getLogger(TWSEDataSource.class);
	
	public TPEXDataSource() {
		initFolder();
	}
	
	private void initFolder() {
		checkFolderExist(TPEXConstants.FILE_DIR);
		checkFolderExist(TPEXConstants.FILE_DIR + TPEXConstants.DAILY_CLOSING_INFO_DIR);
	}

	@Override
	public List<ProductClosingInfo> getProductClosingInfo(String product, Date from, Date to) throws Exception {
		return null;
	}

	@Override
	public List<DailyClosingInfo> getDailyClosingInfo(Date from, Date to) throws Exception {
		return null;
	}

	@Override
	public Map<Date, List<ProductClosingInfo>> getAllProductClosingInfo(Date from, Date to) throws Exception {
		return null;
	}
	
	@Override
	public List<BuySellInfo> getBuySellInfo(Date date, String product) throws Exception {
		return null;
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
