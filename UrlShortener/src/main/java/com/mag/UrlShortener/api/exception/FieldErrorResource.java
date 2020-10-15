package com.mag.UrlShortener.api.exception;

import lombok.Data;

@Data
public class FieldErrorResource {
  private final String resource;
  private final String field;
  private final String code;
  private final String message;
}
