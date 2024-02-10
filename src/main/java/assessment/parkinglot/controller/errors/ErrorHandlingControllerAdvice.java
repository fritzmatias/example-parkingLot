package assessment.parkinglot.controller.errors;

import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.stream.Collectors;


@ControllerAdvice
class ErrorHandlingControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(ErrorHandlingControllerAdvice.class);

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        log.error(e.getMessage(),e);
        var responseStatus=HttpStatus.BAD_REQUEST;
        return new ValidationErrorResponse(
                responseStatus.name()
                ,responseStatus.value()
                ,"Request error"
                ,e.getConstraintViolations().stream()
                    .map(error->new Violation(
                            error.getPropertyPath().toString()
                            ,error.getMessage())
                    )
                    .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        log.error(e.getMessage(),e);
        var responseStatus=HttpStatus.BAD_REQUEST;
        return new ValidationErrorResponse(
                responseStatus.name()
                ,responseStatus.value()
                ,"Request error"
                , Collections.singleton(new Violation(
                        e.getParameterName()
                        , e.getMessage()
                ))
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        log.error(e.getMessage(),e);
        var responseStatus=HttpStatus.BAD_REQUEST;
        return new ValidationErrorResponse(
                responseStatus.name()
                ,responseStatus.value()
                ,"Request error"
                ,e.getBindingResult().getFieldErrors().stream()
                    .map(error->new Violation(
                            error.getField()
                            ,error.getDefaultMessage())
                    )
                    .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(DataNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorResponse onDataNotFound(
            DataNotFound e
    ) {
        log.error(e.getAllMessages());
        HttpStatus responseStatus=HttpStatus.NOT_FOUND;
        return new ErrorResponse(
                responseStatus.name()
                ,responseStatus.value()
                ,e.getMessage()
        );
    }

    @ExceptionHandler(DataDuplication.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    ErrorResponse onDataDuplication(
            DataDuplication e
    ) {
        log.error(e.getAllMessages());
        HttpStatus responseStatus=HttpStatus.CONFLICT;
        return new ErrorResponse(
                responseStatus.name()
                ,responseStatus.value()
                ,e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ErrorResponse onDataDuplication(
            Exception e
    ) {
        log.error(e.getMessage(),e);
        HttpStatus responseStatus=HttpStatus.INTERNAL_SERVER_ERROR;
        return new ErrorResponse(
                responseStatus.name()
                ,responseStatus.value()
                ,"Unexpected error"
        );
    }

}