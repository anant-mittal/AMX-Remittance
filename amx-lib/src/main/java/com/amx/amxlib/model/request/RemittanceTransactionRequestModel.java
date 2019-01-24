/**
 * 
 */
package com.amx.amxlib.model.request;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;


import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.utils.JsonUtil;

/**
 * @author Prashant
 *
 */
public class RemittanceTransactionRequestModel extends AbstractModel implements IRemitTransReqPurpose {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal beneId;
	private BigDecimal sourceOfFund;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private boolean availLoyalityPoints;
	private BigDecimal additionalBankRuleFiledId;
	private BigDecimal srlId;
	private String mOtp;
	private String eOtp;
	@NotNull
	private ExchangeRateBreakup exRateBreakup;
	private Map<String, Object> flexFields;
	private Map<String, FlexFieldDto> flexFieldDtoMap;
	private BigDecimal placeOrderId;
	private BigDecimal domXRate;

	public BigDecimal getDomXRate() {
		return domXRate;
	}

	public void setDomXRate(BigDecimal domXRate) {
		this.domXRate = domXRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.amxlib.model.AbstractModel#getModelType()
	 */
	@Override
	public String getModelType() {
		return "remittance_transaction";
	}

	@Override
	public BigDecimal getBeneId() {
		return beneId;
	}

	@Override
	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
	}

	public BigDecimal getSourceOfFund() {
		return sourceOfFund;
	}

	public void setSourceOfFund(BigDecimal sourceOfFund) {
		this.sourceOfFund = sourceOfFund;
	}

	@Override
	public BigDecimal getLocalAmount() {
		return localAmount;
	}

	@Override
	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}

	@Override
	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}

	@Override
	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	public boolean isAvailLoyalityPoints() {
		return availLoyalityPoints;
	}

	public void setAvailLoyalityPoints(boolean availLoyalityPoints) {
		this.availLoyalityPoints = availLoyalityPoints;
	}

	@Override
	public String toString() {
		return "RemittanceTransactionRequestModel [beneId=" + beneId + ", sourceOfFund=" + sourceOfFund
				+ ", localAmount=" + localAmount + ", foreignAmount=" + foreignAmount + ", availLoyalityPoints="
				+ availLoyalityPoints + "]";
	}

	public BigDecimal getSrlId() {
		return srlId;
	}

	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}

	public BigDecimal getAdditionalBankRuleFiledId() {
		return additionalBankRuleFiledId;
	}

	public void setAdditionalBankRuleFiledId(BigDecimal additionalBankRuleFiledId) {
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
	}

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	public String geteOtp() {
		return eOtp;
	}

	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

	public Map<String, FlexFieldDto> getFlexFieldDtoMap() {
		return flexFieldDtoMap;
	}

	public void setFlexFieldDtoMap(Map<String, FlexFieldDto> flexFieldDtoMap) {
		this.flexFieldDtoMap = flexFieldDtoMap;
	}

	public void populateFlexFieldDtoMap() {
		if (this.flexFields != null) {
			Map<String, String> flexFieldMap = createFlexFieldMap(flexFields);
			Function<Map.Entry<String, String>, FlexFieldDto> valueMapper = (entryObject) -> {
				String value = entryObject.getValue().toString();
				FlexFieldDto flexFieldDto = null;
				try {
					flexFieldDto = JsonUtil.fromJson(value, FlexFieldDto.class);
				} catch (Exception e) {
				}
				if (flexFieldDto == null) {
					flexFieldDto = new FlexFieldDto(value);
				}
				return flexFieldDto;
			};
			this.flexFieldDtoMap = flexFieldMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, valueMapper));
		}
	}

	private Map<String, String> createFlexFieldMap(Map<String, Object> flexFields2) {

		Set<Entry<String, Object>> es = flexFields2.entrySet();
		Map<String, String> output = es.stream()
				.collect(Collectors.toMap(x -> x.getKey(), x -> JsonUtil.toJson(x.getValue())));
		return output;
	}

	public Map<String, Object> getFlexFields() {
		return flexFields;
	}

	public void setFlexFields(Map<String, Object> flexFields) {
		this.flexFields = flexFields;
	}

	public BigDecimal getPlaceOrderId() {
		return placeOrderId;
	}

	public void setPlaceOrderId(BigDecimal placeOrderId) {
		this.placeOrderId = placeOrderId;
	}

}
