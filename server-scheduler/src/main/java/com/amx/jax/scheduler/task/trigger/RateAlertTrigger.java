package com.amx.jax.scheduler.task.trigger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

public class RateAlertTrigger implements Trigger {

	private Logger log = Logger.getLogger(getClass());
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Override
	public Date nextExecutionTime(TriggerContext triggerContext) {
		Date scheduledTime = triggerContext.lastScheduledExecutionTime();
		Calendar val = Calendar.getInstance();
		if (scheduledTime != null) {
			Date now = Calendar.getInstance().getTime();
			long durationMs = now.getTime() - scheduledTime.getTime();
			int durationInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(durationMs);
			if (durationInMinutes < 5) {
				val.add(Calendar.MINUTE, 5 - durationInMinutes);
			}
		}
		log.info("Next execution of RateAlertTask " + df.format(val.getTime()));
		return val.getTime();
	}

}
