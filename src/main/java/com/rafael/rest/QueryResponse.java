package com.rafael.rest;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryResponse {

  @JsonAlias("result")
  private String result;
  @JsonAlias("data")
  private List<Map<String, Object>> data;
}
