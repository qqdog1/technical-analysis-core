package name.qd.techAnalyst.dataSource.TPEX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.dataSource.TWSE.TWSEDataSource;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class TPEXDataSource implements DataSource {
	private Logger log = LogManager.getLogger(TWSEDataSource.class);
	
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
