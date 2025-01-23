package mr.demonid.logger.service.controllers;

import mr.demonid.logger.service.exceptions.LoggerIOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(LoggerIOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badSendMessage(LoggerIOException e) {
        return "Logger-service ERROR: [" + LocalDateTime.now() + "] " + e.getMessage();
    }
}
