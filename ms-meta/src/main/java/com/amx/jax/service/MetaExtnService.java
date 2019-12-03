package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.client.bene.InstitutionCategoryDto;
import com.amx.jax.dbmodel.bene.InstitutionCategoryMaster;
import com.amx.jax.repository.bene.InstitutionCategoryMasterRepository;

@Service
public class MetaExtnService {

	@Autowired
	InstitutionCategoryMasterRepository institutionCategoryMasterRepository;

	private static final Logger log = LoggerFactory.getLogger(MetaExtnService.class);

	public List<InstitutionCategoryDto> listInstitutionCategoryMaster() {
		List<InstitutionCategoryDto> allInstituteList = new ArrayList<InstitutionCategoryDto>();
		Iterable<InstitutionCategoryMaster> allInstitutes = institutionCategoryMasterRepository.findAll();
		allInstitutes.forEach(i -> {
			allInstituteList.add(convert(i));
		});
		return allInstituteList;
	}

	public InstitutionCategoryMaster getInstitutionCategoryMasterById(BigDecimal categoryId) {
		return institutionCategoryMasterRepository.findOne(categoryId);
	}

	private InstitutionCategoryDto convert(InstitutionCategoryMaster i) {
		InstitutionCategoryDto dto = new InstitutionCategoryDto();
		try {
			BeanUtils.copyProperties(dto, i);
		} catch (IllegalAccessException | InvocationTargetException e) {
		}
		return dto;
	}

}
