package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class RemittanceTransactionValidationException extends AbstractException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RemittanceTransactionValidationException(ApiError error) {
        super(error);
    }

}
