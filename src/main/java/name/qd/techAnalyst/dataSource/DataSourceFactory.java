package name.qd.techAnalyst.dataSource;

import name.qd.techAnalyst.Constants.Exchange;
import name.qd.techAnalyst.dataSource.TWSE.TWSEDataSource;

public class DataSourceFactory {
	private static DataSourceFactory instance = new DataSourceFactory();
	
	private DataSource twse;
	
	private DataSourceFactory() {
	}
	
	public static DataSourceFactory getInstance() {
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
