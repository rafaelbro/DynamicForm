package com.rafael.rest;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormResponse {

  @JsonAlias("formularyId")
  private Integer formId;
  @JsonAlias("result")
  private String result;
}
