package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.repository.CountryBranchRepository;

@Service
public class CountryBranchService {

	@Autowired
	CountryBranchRepository repo;

	public CountryBranchMdlv1 getOnlineCountryBranch() {
		return repo.findByBranchId(new BigDecimal(90));
	}

	public CountryBranchMdlv1 getCountryBranchByCountryBranchId(BigDecimal countryBranchId) {
		return repo.findByCountryBranchId(countryBranchId);
	}
	
	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranchList() {
		List<CountryBranchMdlv1> viewBranchList = repo.getCountryBranchList();
		return AmxApiResponse.buildList(convertCountryBranchDto(viewBranchList));
	}
	
	
	private List<CountryBranchDTO> convertCountryBranchDto(List<CountryBranchMdlv1> viewCountryBranchList) {
		List<CountryBranchDTO> output = new ArrayList<>();
		for (CountryBranchMdlv1 viewcountryBranchModel : viewCountryBranchList) {
			CountryBranchDTO dto = new CountryBranchDTO();
			dto.setBranchId(viewcountryBranchModel.getBranchId());
			dto.setBranchName(viewcountryBranchModel.getBranchName());
			dto.setCountryBranchId(viewcountryBranchModel.getCountryBranchId());
			output.add(dto);
		}
		return output;
	}
	
	
	
}
