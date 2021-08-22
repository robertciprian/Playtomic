package com.playtomic.tests.wallet.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{

    private String errorMessage;

    public BadRequestException(String errorMessage) {
        super(errorMessage);
        this.setErrorMessage(errorMessage);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        return "BadRequestException{" +
                "errorMessage='" + errorMessage + '\'' +
                '}' + super.toString();
    }
}
