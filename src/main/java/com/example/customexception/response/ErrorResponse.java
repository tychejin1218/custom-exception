package com.example.customexception.response;

import com.example.customexception.constant.ResponseCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class ErrorResponse {

  @JsonProperty("status_code")
  private String statusCode;

  @JsonProperty("message")
  private String message;

  @JsonProperty("method")
  private String method;

  @JsonProperty("path")
  private String path;

  @JsonProperty("timestamp")
  private String timestamp;

  @Builder
  ErrorResponse(
      String statusCode,
      String message,
      String method,
      String path,
      String timestamp
  ) {
    this.statusCode = statusCode;
    this.message =
        StringUtils.isNotBlank(message) ? message
            : ResponseCode.valueOfStatusCode(statusCode).getMessage();
    this.method = method;
    this.path = path;
    this.timestamp =
        StringUtils.isNotBlank(timestamp) ? timestamp
            : LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDDHHMMSS"));
  }
}