package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dbmodel.CurrencyPairView;
import com.amx.jax.dbmodel.forexoutlook.ForexOutlook;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.ForexOutLookRequest;
import com.amx.jax.model.response.fx.CurrencyPairDTO;
import com.amx.jax.model.response.fx.ForexOutLookResponseDTO;
import com.amx.jax.repository.ICurrencyPairRepository;
import com.amx.jax.repository.IForexOutlookDao;
import com.amx.jax.services.AbstractService;

@Service
public class ForexOutlookService extends AbstractService {

	private static final Logger LOGGER = LoggerService.getLogger(ForexOutlookService.class);

	@Autowired
	ICurrencyPairRepository currencyPairRepository;

	@Autowired
	IForexOutlookDao forexOutlookDao;

	@Autowired
	MetaData metaData;

	public List<CurrencyPairDTO> getCurrencyPairList() {
		List<CurrencyPairView> currencyPairList = currencyPairRepository.findAll();

		LOGGER.debug("currencyPairList Size" + currencyPairList.size());

		if (currencyPairList.isEmpty()) {
			throw new GlobalException(JaxError.DATA_NOT_FOUND, "currencyPairList list is not abaliable");
		}

		List<CurrencyPairDTO> dtoList = new ArrayList<CurrencyPairDTO>();
		for (CurrencyPairView entity : currencyPairList) {
			CurrencyPairDTO dto = new CurrencyPairDTO();
			dto.importFrom(entity);
			dtoList.add(dto);

		}
		return dtoList;

	}

	public List<ForexOutLookResponseDTO> getCurpairHistory() {
		List<ForexOutlook> curPairHistoryList = forexOutlookDao.findAll();

		LOGGER.debug("getCurpairHistory Size" + curPairHistoryList.size());

		if (curPairHistoryList.isEmpty()) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "currencyPairList list is not abaliable");
		}

		List<ForexOutLookResponseDTO> dtoList = new ArrayList<ForexOutLookResponseDTO>();
		for (ForexOutlook entity : curPairHistoryList) {
			ForexOutLookResponseDTO dto = new ForexOutLookResponseDTO();
			dto.importFrom(entity);
			dto.setIsActive(entity.getIsActive());
			dto.setModifiedDate(entity.getModifiedDate());
			dtoList.add(dto);

		}

		return dtoList;
	}

	public BoolRespModel saveUpdateCurrencyPair(ForexOutLookRequest dto, BigDecimal appContryId, BigDecimal langId,
			BigDecimal custId) {
		
		LOGGER.debug("dto in service" + dto.toString());

		List<ForexOutlook> fxoList = null;
		BigDecimal pairId = dto.getPairId();
		String message = dto.getMessage();

		try {
			if (pairId != null && message != null) {
				fxoList = forexOutlookDao.getCurrencyPairById(pairId, langId);

				if (!fxoList.isEmpty()) {
					LOGGER.debug("saveUpdateCurrencyPair currpair list " + fxoList.toString());
					for (ForexOutlook rec : fxoList) {
						rec.setOutlookDesc(dto.getMessage());
						rec.setModifiedBy(custId.toString());
						forexOutlookDao.save(rec);
					}
				} else {
					ForexOutlook forexOut = new ForexOutlook();

					forexOut.setAppCountryId(appContryId);
					forexOut.setPairId(pairId);
					forexOut.setLangId(langId);
					forexOut.setOutlookDesc(dto.getMessage());
					forexOut.setIsActive("Y");
					forexOut.setCreatedDate(new Date());
					forexOut.setModifiedDate(new Date());
					forexOut.setCreatedBy(custId.toString());
					forexOut.setModifiedBy(custId.toString());

					forexOutlookDao.save(forexOut);
				}

			}

		}

		catch (Exception e) {
			return new BoolRespModel(Boolean.FALSE);
		}

		return new BoolRespModel(Boolean.TRUE);

	}

	public BoolRespModel deleteCurrencyPair(BigDecimal pairId) {
		try {
			List<ForexOutlook> curPairHistoryList = forexOutlookDao.getCurrencyPairById(pairId,
					metaData.getLanguageId());
			if (!curPairHistoryList.isEmpty()) {
				ForexOutlook rec = curPairHistoryList.get(0);
				rec.setIsActive("D");
				rec.setModifiedDate(new Date());
				forexOutlookDao.save(rec);
			} else {
				throw new GlobalException("No record found");
			}
		} catch (Exception e) {
			return new BoolRespModel(Boolean.FALSE);
		}

		LOGGER.debug("CurrencyPair Deleted ");
		return new BoolRespModel(Boolean.TRUE);
	}

	public void validateForexOutlookDto(BigDecimal pairId) {

		List<CurrencyPairView> currencyPairList = currencyPairRepository.findAll();

		if (currencyPairList.contains(pairId)) {
			throw new GlobalException(JaxError.INVALID_PAIR_ID, "Invalid Pair Id ");

		}

	}

}
