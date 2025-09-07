package com.legacycorp.bikehubb.exception;

public class StripePaymentException extends StripeException {

    public StripePaymentException(String message, String stripeErrorCode, String userFriendlyMessage) {
        super(message, stripeErrorCode, userFriendlyMessage);
    }

    public StripePaymentException(String message, String stripeErrorCode, String userFriendlyMessage, Throwable cause) {
        super(message, stripeErrorCode, userFriendlyMessage, cause);
    }
}
