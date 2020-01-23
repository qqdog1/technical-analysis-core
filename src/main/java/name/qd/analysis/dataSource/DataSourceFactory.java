package name.qd.analysis.dataSource;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.dataSource.TWSE.TWSEDataSource;

public class DataSourceFactory {
	private static DataSourceFactory instance = new DataSourceFactory();
	
	private DataSource twse;
	
	private DataSourceFactory() {
	}
	
	public static DataSourceFactory getInstance() {
		return instance;
	}
	
	public DataSource getDataSource(Exchange exchange, String fileFolder) {
		switch (exchange) {
		case TWSE:
			if(twse == null) {
				twse = new TWSEDataSource(fileFolder);
			}
			return twse;
		case TPEX:
			break;
		default:
			break;
		}
		return null;
	}
}
