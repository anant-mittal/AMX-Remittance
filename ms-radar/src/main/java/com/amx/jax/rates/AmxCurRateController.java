package com.amx.jax.rates;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AmxCurRateController {

	@Autowired
	private AmxCurRateRepository curRateRepository;

	@ResponseBody
	@RequestMapping(value = "/pub/currate", method = RequestMethod.POST)
	public Map<String, Object> postVote(@RequestBody AmxCurRate vote) {
		return curRateRepository.updateRateById(vote.getId(), vote);
	}

	@ResponseBody
	@RequestMapping(value = "/pub/currate", method = RequestMethod.PUT)
	public AmxCurRate putVote(@RequestBody AmxCurRate vote) {
		return curRateRepository.insertRate(vote);
	}

}
