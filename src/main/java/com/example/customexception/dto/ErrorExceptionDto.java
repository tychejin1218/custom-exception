package com.example.customexception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ErrorExceptionDto {

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class Request {

    @JsonProperty("id")
    private Long id;

    @NotBlank
    @JsonProperty("title")
    private String title;

    @NotBlank
    @JsonProperty("description")
    private String description;

    @AssertFalse
    @JsonProperty("completed")
    private Boolean completed;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public class Response {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String titile;

    @JsonProperty("description")
    private String description;

    @JsonProperty("completed")
    private Boolean completed;
  }
}
