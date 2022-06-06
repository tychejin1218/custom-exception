package com.example.customexception.controller;

import com.example.customexception.constant.ResponseCode;
import com.example.customexception.dto.ErrorExceptionDto;
import com.example.customexception.exception.ErrorException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

  /**
   * RuntimeException
   */
  @GetMapping("/api/exception01")
  public String exception01() {
    throw new RuntimeException("RuntimeException 발생");
  }

  /**
   * ErrorException(ResponseCode rtaStatusCode)
   */
  @GetMapping("/api/exception02")
  public String exception02() {
    throw new ErrorException(ResponseCode.CUSTOM_EXCEPTION);
  }

  /**
   * ErrorException(String message)
   */
  @GetMapping("/api/exception03")
  public String exception03() {
    throw new ErrorException("ErrorException 발생");
  }

  /**
   * MethodArgumentNotvalidException
   */
  @PostMapping("/api/methodArgumentNotvalidException")
  public String methodArgumentNotValidException(
      @Valid @RequestBody ErrorExceptionDto.Request errorRequest
  ) {
    return "methodArgumentNotvalidException";
  }

  /**
   * MethodArgumentNotvalidException
   */
  @GetMapping("/api/missingServletRequestParameterException")
  public String missingServletRequestParameterException(
      @RequestParam("id") Long id
  ) {
    return "missingServletRequestParameterException";
  }

  /**
   * MethodArgumentNotvalidException
   */
  @PostMapping("/api/constraintViolationException")
  public String constraintViolationException(
      @RequestBody ErrorExceptionDto.Request errorRequest
  ) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<ErrorExceptionDto.Request>> violations
        = validator.validate(errorRequest);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
    return "constraintViolationException";
  }

  /**
   * MethodArgumentNotvalidException
   */
  @GetMapping("/api/methodArgumentTypeMismatchException")
  public String methodArgumentTypeMismatchException(
      @RequestParam("id") Long id
  ) {
    return "methodArgumentTypeMismatchException";
  }

  /**
   * HttpMessageNotReadableException
   */
  @PostMapping("/api/httpMessageNotReadableException")
  public String httpMessageNotReadableException(
      @Valid @RequestBody ErrorExceptionDto.Request errorRequest
  ) {
    return "httpMessageNotReadableException";
  }

  /**
   * NoHandlerFoundException
   */
  @DeleteMapping("/api/noHandlerFoundException")
  public String noHandlerFoundException(
      @RequestParam("id") Long id
  ) {
    return "noHandlerFoundException";
  }

  /**
   * HttpRequestMethodNotSupportedException
   */
  @PostMapping("/api/httpRequestMethodNotSupportedException")
  public String httpRequestMethodNotSupportedException(
      @Valid @RequestBody ErrorExceptionDto.Request errorRequest
  ) {
    return "httpRequestMethodNotSupportedException";
  }

  /**
   * HttpMediaTypeNotSupportedException
   */
  @PostMapping("/api/httpMediaTypeNotSupportedException")
  public String httpMediaTypeNotSupportedException(
      @Valid @RequestBody ErrorExceptionDto.Request errorRequest
  ) {
    return "httpMediaTypeNotSupportedException";
  }
}