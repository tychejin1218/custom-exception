package com.example.customexception.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.customexception.constant.ResponseCode;
import com.example.customexception.dto.ErrorExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
class ExceptionControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Order(1)
  @DisplayName("exception01_조회_에러 코드:900")
  @Test
  void testException01() throws Exception {

    // Given & When
    String url = "/api/exception01";
    ResultActions resultActions = mockMvc.perform(get(url));

    // Then
    resultActions
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.GET.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(2)
  @DisplayName("exception02_조회_에러 코드:800")
  @Test
  void testException02() throws Exception {

    // Given & When
    String url = "/api/exception02";
    ResultActions resultActions = mockMvc.perform(get(url));

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.CUSTOM_EXCEPTION.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.CUSTOM_EXCEPTION.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.GET.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(3)
  @DisplayName("exception03_조회_에러 코드:800")
  @Test
  void testException03() throws Exception {

    // Given & When
    String url = "/api/exception03";
    ResultActions resultActions = mockMvc.perform(get(url));

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.CUSTOM_EXCEPTION.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value("ErrorException 발생")
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.GET.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(4)
  @DisplayName("methodArgumentNotValidException_조회_에러 코드:901")
  @Test
  void testMethodArgumentNotValidException() throws Exception {

    // Given & When
    String url = "/api/methodArgumentNotvalidException";
    ErrorExceptionDto.Request errorRequest = ErrorExceptionDto.Request.builder()
        .title("")
        .description("")
        .completed(false)
        .build();

    ResultActions resultActions = mockMvc.perform(
        post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(errorRequest))
    );

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.METHOD_ARGUMENT_NOT_VALID.getCode())
        )
        .andExpect(jsonPath("$.message").isNotEmpty())
        .andExpect(jsonPath("$.method").value(HttpMethod.POST.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(5)
  @DisplayName("missingServletRequestParameterException_조회_에러 코드:902")
  @Test
  void testMissingServletRequestParameterException() throws Exception {

    // Given & When
    String url = "/api/missingServletRequestParameterException";
    LinkedMultiValueMap queryParams = new LinkedMultiValueMap<>();
    queryParams.add("missingId", "1");

    ResultActions resultActions = mockMvc.perform(
        get(url)
            .queryParams(queryParams)
    );

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.MISSING_SERVLET_REQUEST_PARAMETER.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.MISSING_SERVLET_REQUEST_PARAMETER.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.GET.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(6)
  @DisplayName("constraintViolationException 코드:903")
  @Test
  void testConstraintViolationException() throws Exception {

    // Given & When
    String url = "/api/constraintViolationException";
    ErrorExceptionDto.Request errorExceptionDto = ErrorExceptionDto.Request.builder()
        .title("")
        .description("")
        .completed(false)
        .build();

    ResultActions resultActions = mockMvc.perform(
        post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(errorExceptionDto))
    );

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.CONSTRAINT_VIOLATION.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.CONSTRAINT_VIOLATION.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.POST.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(7)
  @DisplayName("methodArgumentTypeMismatchException_조회_에러 코드:904")
  @Test
  void testMethodArgumentTypeMismatchException() throws Exception {

    // Given & When
    String url = "/api/methodArgumentTypeMismatchException";
    LinkedMultiValueMap queryParams = new LinkedMultiValueMap<>();
    queryParams.add("id", "test");

    ResultActions resultActions = mockMvc.perform(
        get(url)
            .queryParams(queryParams)
    );

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.METHOD_ARGUMENT_TYPE_MISMATCH.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.METHOD_ARGUMENT_TYPE_MISMATCH.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.GET.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(8)
  @DisplayName("httpMessageNotReadableException 코드:905")
  @Test
  void testHttpMessageNotReadableException() throws Exception {

    // Given & When
    String url = "/api/httpMessageNotReadableException";
    ResultActions resultActions = mockMvc.perform(
        post(url)
            .contentType(MediaType.APPLICATION_JSON)
    );

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.POST.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(9)
  @DisplayName("noHandlerFoundException 코드:906")
  @Test
  void testNoHandlerFoundException() throws Exception {

    // Given & When
    String url = "/api/noHandlerFoundExceptionFailed";
    LinkedMultiValueMap queryParams = new LinkedMultiValueMap<>();
    queryParams.add("id", "1");

    ResultActions resultActions = mockMvc.perform(
        get(url)
            .queryParams(queryParams)
    );

    // Then
    resultActions
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.NO_HANDLER_FOUND.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.NO_HANDLER_FOUND.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.GET.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(10)
  @DisplayName("httpRequestMethodNotSupportedException 코드:907")
  @Test
  void testHttpRequestMethodNotSupportedException() throws Exception {

    // Given & When
    String url = "/api/httpRequestMethodNotSupportedException";
    LinkedMultiValueMap queryParams = new LinkedMultiValueMap<>();
    queryParams.add("id", "1");

    ResultActions resultActions = mockMvc.perform(
        get(url)
            .queryParams(queryParams)
    );

    // Then
    resultActions
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.GET.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }

  @Order(11)
  @DisplayName("httpMediaTypeNotSupportedException 코드:908")
  @Test
  void testHttpMediaTypeNotSupportedException() throws Exception {

    // Given & When
    String url = "/api/httpMediaTypeNotSupportedException";
    ErrorExceptionDto.Request errorExceptionDto = ErrorExceptionDto.Request.builder()
        .title("Spring Boot")
        .description("description00")
        .completed(false)
        .build();

    ResultActions resultActions = mockMvc.perform(
        post(url)
            .contentType(MediaType.TEXT_PLAIN)
            .content(objectMapper.writeValueAsString(errorExceptionDto))
    );

    // Then
    resultActions
        .andExpect(status().isUnsupportedMediaType())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.status_code")
                .value(ResponseCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED.getCode())
        )
        .andExpect(
            jsonPath("$.message")
                .value(ResponseCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED.getMessage())
        )
        .andExpect(jsonPath("$.method").value(HttpMethod.POST.toString()))
        .andExpect(jsonPath("$.path").value(url))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andDo(print());
  }
}