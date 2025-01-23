package mr.demonid.storage.service.controllers;

import mr.demonid.storage.service.exceptions.BadScheduleException;
import mr.demonid.storage.service.exceptions.BaseStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseStorageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBaseStorageException(BaseStorageException e) {
        return "[" + LocalDateTime.now() + "] " + e.getTitle() + ": " + e.getMessage();
    }

}

