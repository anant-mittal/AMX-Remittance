package com.amx.amxlib.exception;

import com.amx.jax.exception.AmxApiError;

public class LimitExeededException extends AbstractJaxException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LimitExeededException(AmxApiError error) {
        super(error);
    }

}
