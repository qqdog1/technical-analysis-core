package name.qd.analysis.tech.backTest;

import java.util.Date;
import java.util.List;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.vo.ActionResult;

public interface BackTesting {
	public List<ActionResult> getAction(DataSource dataSource, String product, Date from, Date to, String ... custom) throws Exception ;
	public List<String> getCustomDescreption();
}
