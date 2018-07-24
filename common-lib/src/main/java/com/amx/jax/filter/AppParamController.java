package com.amx.jax.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppParam;
import com.amx.jax.types.DigitsDnum;
import com.amx.jax.types.Pnum;
import com.amx.jax.types.WritersPnum;

@RestController
public class AppParamController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppParamController.class);

	static {
		// Pnum.readEnums();
		Pnum.init(WritersPnum.class);
	}

	@RequestMapping(value = "/pub/amx/params", method = RequestMethod.GET)
	public AppParam[] geoLocation(@RequestParam(required = false) AppParam id) {
		if (id != null) {
			id.setEnabled(!id.isEnabled());
			LOGGER.info("App Param {} changed to {}", id, id.isEnabled());
		}
		return AppParam.values();
	}

	@RequestMapping(value = "/pub/amx/pnum", method = RequestMethod.GET)
	public WritersPnum geoLocation(@RequestParam(required = false) WritersPnum id) {
		return id;
	}

	@RequestMapping(value = "/pub/amx/dnum", method = RequestMethod.GET)
	public DigitsDnum geoLocation(@RequestParam(required = false) DigitsDnum id) {
		new DigitsDnum("FOUR", 3);
		return id;
	}

}
