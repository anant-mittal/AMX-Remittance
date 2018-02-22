package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class LimitExeededException extends AbstractException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LimitExeededException(ApiError error) {
        super(error);
    }

}
