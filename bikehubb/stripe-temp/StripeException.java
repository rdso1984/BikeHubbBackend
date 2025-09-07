package com.legacycorp.bikehubb.exception;

import lombok.Data;

@Data
public class StripeException extends RuntimeException {

    private final String stripeErrorCode;
    private final String userFriendlyMessage;

    public StripeException(String message, String stripeErrorCode, String userFriendlyMessage) {
        super(message);
        this.stripeErrorCode = stripeErrorCode;
        this.userFriendlyMessage = userFriendlyMessage;
    }

    public StripeException(String message, String stripeErrorCode, String userFriendlyMessage, Throwable cause) {
        super(message, cause);
        this.stripeErrorCode = stripeErrorCode;
        this.userFriendlyMessage = userFriendlyMessage;
    }

}
