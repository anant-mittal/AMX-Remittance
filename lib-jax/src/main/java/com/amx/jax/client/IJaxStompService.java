package com.amx.jax.client;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.dict.AmxEnums.FxOrderStatus;

public interface IJaxStompService extends IJaxService {
    public void publishFxOrderStatusChange(BigDecimal orderNumber, BigDecimal orderYear,  FxOrderStatus status);
}