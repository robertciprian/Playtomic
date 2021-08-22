package com.playtomic.tests.wallet.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class ServiceUnavailableException extends RuntimeException{

    private String errorMessage;

    public ServiceUnavailableException(String errorMessage) {
        super(errorMessage);
        this.setErrorMessage(errorMessage);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        return "ServiceUnavailableException{" +
                "errorMessage='" + errorMessage + '\'' +
                '}' + super.toString();
    }
}
