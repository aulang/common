package cn.aulang.common.web;

import cn.aulang.common.core.lang.Constant;
import cn.aulang.common.exception.NotFoundException;
import cn.aulang.common.exception.ParameterException;
import cn.aulang.common.exception.SearchException;
import cn.aulang.common.exception.ServiceException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@Slf4j
public class WebExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(ConstraintViolationException e) {
        return e.getConstraintViolations()
                .stream()
                .map(error -> error.getPropertyPath() + Constant.SPACE + error.getMessage())
                .collect(Collectors.joining(Constant.SEMICOLON));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + Constant.SPACE + error.getDefaultMessage())
                .collect(Collectors.joining(Constant.SEMICOLON));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(MissingServletRequestParameterException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(TypeMismatchException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(HttpMessageNotReadableException e) {
        return StringUtils.substringBefore(e.getMessage(), Constant.SEMICOLON);
    }

    @ExceptionHandler(value = ParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(ParameterException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = SearchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(SearchException e) {
        log.warn("Search error", e);
        return e.getMessage();
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(DuplicateKeyException e) {
        log.warn("Duplicate key error", e);
        return "重复的数据，记录已存在";
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.GONE)
    public String exceptionHandler(NotFoundException e) {
        return "Id " + e.getId() + " is not found";
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String exceptionHandler(HttpRequestMethodNotSupportedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(ServiceException e) {
        log.error("Service error", e);
        return e.getMessage();
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(NullPointerException e) {
        log.error("Server internal NPE error", e);
        return "服务器内部错误";
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Exception e) {
        log.error("Server internal error", e);
        return e.getMessage();
    }
}
