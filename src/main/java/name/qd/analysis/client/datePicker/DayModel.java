package name.qd.analysis.client.datePicker;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.jdatepicker.AbstractDateModel;

public class DayModel extends AbstractDateModel<java.util.Date> {
	
	public DayModel() {
		this(null);
	}
	
	public DayModel(Date value) {
		super();
		setValue(value);
	}
	
	@Override
	protected Calendar toCalendar(Date from) {
		Calendar to = Calendar.getInstance();
		to.setTime(from);
		return to;
	}

	@Override
	protected Date fromCalendar(Calendar from) {
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		return new Date(from.getTimeInMillis());
	}
}
