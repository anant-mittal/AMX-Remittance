package com.amx.jax.polls;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VoteController {

	@Autowired
	private VoteRepository voteRepository;

	@ResponseBody
	@RequestMapping(value = "/pub/voting", method = RequestMethod.POST)
	public Map<String, Object> postVote(@RequestBody Vote vote) {
		return voteRepository.updateVoteById(vote.getId(), vote);
	}

	@ResponseBody
	@RequestMapping(value = "/pub/voting", method = RequestMethod.PUT)
	public Vote putVote(@RequestBody Vote vote) {
		return voteRepository.insertVote(vote);
	}

}
