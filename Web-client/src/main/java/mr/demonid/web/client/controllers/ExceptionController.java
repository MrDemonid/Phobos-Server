package mr.demonid.web.client.controllers;

import mr.demonid.web.client.exceptions.ClientBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ClientBaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadScheduleException(ClientBaseException e) {
        System.out.println("Error [" + LocalDateTime.now() + "] " + e.getTitle() + ": " + e.getMessage());
        return "Error [" + LocalDateTime.now() + "] " + e.getTitle() + ": " + e.getMessage();
    }

}
