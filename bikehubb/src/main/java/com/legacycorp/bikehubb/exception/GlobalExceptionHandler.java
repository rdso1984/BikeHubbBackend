package com.legacycorp.bikehubb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Data;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /*
    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponse> handleStripeException(StripeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getStripeErrorCode(),
            ex.getUserFriendlyMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripePaymentException.class)
    public ResponseEntity<ErrorResponse> handleStripePaymentException(StripePaymentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getStripeErrorCode(),
            ex.getUserFriendlyMessage(),
            HttpStatus.PAYMENT_REQUIRED.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(StripeWebhookException.class)
    public ResponseEntity<ErrorResponse> handleStripeWebhookException(StripeWebhookException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getStripeErrorCode(),
            "Erro no processamento do pagamento",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    */
}

// Classe auxiliar para a resposta de erro
@Data
class ErrorResponse {
    private String code;
    private String message;
    private int status;

    public ErrorResponse(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}

