package com.rafael.rest;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmissionResponse {

  @JsonAlias("formularyId")
  private Integer formId;
  @JsonAlias("submissionId")
  private Integer submissionId;
  @JsonAlias("result")
  private String result;
}
