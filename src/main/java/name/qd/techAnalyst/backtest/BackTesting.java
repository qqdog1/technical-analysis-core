package name.qd.techAnalyst.backtest;

import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.ActionResult;

public interface BackTesting {
	public List<ActionResult> getAction(DataSource dataSource, String product, Date from, Date to, String ... custom) throws Exception ;
	public List<String> getCustomDescreption();
}
