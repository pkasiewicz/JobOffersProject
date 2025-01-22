package pl.pkasiewicz.infrastructure.offer.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pkasiewicz.domain.offer.OfferNotFoundException;

@ControllerAdvice
@Log4j2
public class JobOfferControllerErrorHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JobOfferErrorResponse handleJobOfferNotFoundException(OfferNotFoundException exception) {
        final String message = exception.getMessage();
        log.error(message);
        return new JobOfferErrorResponse(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public JobOfferPostErrorResponse handleJobOfferDuplicationException(DuplicateKeyException exception) {
        final String message = "Offer with this url already exists";
        log.error(exception.getMessage());
        return new JobOfferPostErrorResponse(message, HttpStatus.CONFLICT);
    }
}
