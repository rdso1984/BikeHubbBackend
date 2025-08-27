package com.legacycorp.bikehubb.exception;

public class StripeWebhookException extends StripeException {

    public StripeWebhookException(String message, String stripeErrorCode, String userFriendlyMessage) {
        super(message, stripeErrorCode, userFriendlyMessage);
    }

    public StripeWebhookException(String message, String stripeErrorCode, String userFriendlyMessage, Throwable cause) {
        super(message, stripeErrorCode, userFriendlyMessage, cause);
    }
}
