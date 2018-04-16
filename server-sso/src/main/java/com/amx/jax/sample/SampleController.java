/**  AlMulla Exchange
  *  
  */
package com.amx.jax.sample;

import static com.amx.jax.sample.SampleConstant.SAMPLE_API_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppConstants;
import com.amx.jax.postman.model.Message;
import com.amx.jax.sample.tranx.TestTranx;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(SAMPLE_API_ENDPOINT)
@Api(value = "Sample APIs")
public class SampleController {

	private Logger logger = Logger.getLogger(SampleController.class);

	@Autowired
	TestTranx testTranx;

	@RequestMapping(value = "/tranx", method = RequestMethod.GET)
	public Message unlockCustomer(@RequestParam(AppConstants.TRANX_ID_XKEY) String tranxId,
			@RequestParam TestTranx.Action action, @RequestParam(required = false) String message) {
		if (action == TestTranx.Action.COMMIT) {
			return testTranx.commit();
		} else if (action == TestTranx.Action.SET) {
			return testTranx.setMessage(message);
		} else {
			return testTranx.commit();
		}
	}

	@RequestMapping(value = "/sendOTP", method = RequestMethod.GET)
	public Message sendOTP(@RequestParam(AppConstants.TRANX_ID_XKEY) String tranxId,
			@RequestParam TestTranx.Action action, @RequestParam(required = false) String message) {
		if (action == TestTranx.Action.COMMIT) {
			return testTranx.commit();
		} else if (action == TestTranx.Action.SET) {
			return testTranx.setMessage(message);
		} else {
			return testTranx.commit();
		}
	}

}
