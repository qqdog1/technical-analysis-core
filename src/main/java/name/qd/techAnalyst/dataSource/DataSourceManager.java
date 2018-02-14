package name.qd.techAnalyst.dataSource;

import name.qd.techAnalyst.Constants.Exchange;
import name.qd.techAnalyst.dataSource.TWSE.TWSEDataSource;

public class DataSourceManager {
	private static DataSourceManager instance = new DataSourceManager();
	
	private DataSource twse;
	
	private DataSourceManager() {
	}
	
	public static DataSourceManager getInstance() {
		return instance;
	}
	
	public DataSource getDataSource(Exchange exchange) {
		switch (exchange) {
		case TWSE:
			if(twse == null) {
				twse = new TWSEDataSource();
			}
			return twse;
		}
		return null;
	}
}
