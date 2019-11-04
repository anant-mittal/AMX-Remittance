package com.amx.jax.task;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.AbstractTest;
import com.amx.jax.dbmodel.task.JaxNotificationTask;
import com.amx.jax.services.NotificationTaskService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NotificationTaskTest extends AbstractTest {

	@Autowired
	NotificationTaskService notificationTaskService;

	@Test
	public void testFetchInsuranceDetail() {
		//List<JaxNotificationTask> tasks = notificationTaskService.getNotificationTaskForCustomer(new BigDecimal(5218));
		//assertNotNull(tasks);
	}
}
