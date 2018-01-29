package com.amx.jax.scheduler.task.trigger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import com.amx.jax.scheduler.config.SchedulerConfig;

public class RateAlertTrigger implements Trigger {

	private ScheduledFuture<?> future;

	private boolean runNow;

	@Autowired
	SchedulerConfig config;

	private Logger log = Logger.getLogger(getClass());
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Override
	public Date nextExecutionTime(TriggerContext triggerContext) {
		Date scheduledTime = triggerContext.lastScheduledExecutionTime();
		Calendar val = Calendar.getInstance();
		if (scheduledTime != null && !runNow) {
			Date now = Calendar.getInstance().getTime();
			long durationMs = now.getTime() - scheduledTime.getTime();
			int durationInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(durationMs);
			if (durationInMinutes < config.getRateAlertTaskPollingTime()) {
				val.add(Calendar.MINUTE, config.getRateAlertTaskPollingTime() - durationInMinutes);
			}
		}
		runNow = false;
		log.info("Next execution of RateAlertTask " + df.format(val.getTime()));
		return val.getTime();
	}

	public ScheduledFuture<?> getFuture() {
		return future;
	}

	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}

	public boolean isRunNow() {
		return runNow;
	}

	public void setRunNow(boolean runNow) {
		this.runNow = runNow;
	}

}
