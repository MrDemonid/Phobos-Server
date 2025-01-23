package mr.demonid.rs232dev.service.controllers;

import mr.demonid.rs232dev.service.exceptions.BadSendMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BadSendMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badSendMessage(BadSendMessageException e) {
        return "Rs232Dev-service ERROR: [" + LocalDateTime.now() + "] " + e.getMessage();
    }


}
