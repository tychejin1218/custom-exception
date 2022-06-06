package com.example.customexception.advice;

import com.example.customexception.constant.ResponseCode;
import com.example.customexception.exception.ErrorException;
import com.example.customexception.response.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionAspect {

  /**
   * ExceptionHandler - Exception
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(
      HttpServletRequest request,
      Exception e) {
    log.error("handleException : {}", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - Custom Exception
   */
  @ExceptionHandler(ErrorException.class)
  public ResponseEntity<ErrorResponse> handleRtaException(
      HttpServletRequest request,
      ErrorException e) {
    log.error("handleRtaException : {}", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        ErrorResponse.builder()
            .statusCode(e.getResponseCode().getCode())
            .method(request.getMethod())
            .message(e.getMessage())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - MethodArgumentNotValidException
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      HttpServletRequest request,
      MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException : {}", e);

    String message = "";

    List<String> messages = new ArrayList<>();
    BindingResult bindingResult = e.getBindingResult();
    for (ObjectError error : bindingResult.getGlobalErrors()) {
      messages.add(
          camelToSnake(error.getObjectName()) + ":" + error.getDefaultMessage());
    }

    for (FieldError error : bindingResult.getFieldErrors()) {
      messages.add(camelToSnake(error.getField()) + ":" + error.getDefaultMessage());
    }

    if (!ObjectUtils.isEmpty(messages)) {
      message = String.join(", ", messages);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.METHOD_ARGUMENT_NOT_VALID.getCode())
            .message(message)
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - MissingServletRequestParameterException
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
      HttpServletRequest request,
      MissingServletRequestParameterException e) {
    log.error("handleMissingServletRequestParameterException : {}", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.MISSING_SERVLET_REQUEST_PARAMETER.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - ConstraintViolationException
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      HttpServletRequest request,
      ConstraintViolationException e) {
    log.error("handleConstraintViolationException : {}", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.CONSTRAINT_VIOLATION.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - MethodArgumentTypeMismatchException
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      HttpServletRequest request,
      MethodArgumentTypeMismatchException e) {
    log.error("handleMethodArgumentTypeMismatchException : {}", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.METHOD_ARGUMENT_TYPE_MISMATCH.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - HttpMessageNotReadableException
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpServletRequest request,
      HttpMessageNotReadableException e) {
    log.error("handleHttpMessageNotReadableException : {}", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - NoHandlerFoundException
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
      HttpServletRequest request,
      NoHandlerFoundException e) {
    log.error("handleNoHandlerFoundException : {}", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.NO_HANDLER_FOUND.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - HttpRequestMethodNotSupportedException
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpServletRequest request,
      HttpRequestMethodNotSupportedException e) {
    log.error("handleHttpRequestMethodNotSupportedException : {}", e);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * ExceptionHandler - HttpMediaTypeNotSupportedException
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
      HttpServletRequest request,
      HttpMediaTypeNotSupportedException e) {
    log.error("handleHttpMediaTypeNotSupportedException : {}", e);
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
        ErrorResponse.builder()
            .statusCode(ResponseCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED.getCode())
            .method(request.getMethod())
            .path(request.getRequestURI())
            .build()
    );
  }

  /**
   * Camal Case를 Snake Case로 변환
   */
  public static String camelToSnake(String str) {

    StringBuilder sb = new StringBuilder();

    char c = str.charAt(0);
    sb.append(Character.toLowerCase(c));

    for (int i = 1; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (Character.isUpperCase(ch)) {
        sb.append('_');
      }
      sb.append(Character.toLowerCase(ch));
    }

    return sb.toString();
  }
}