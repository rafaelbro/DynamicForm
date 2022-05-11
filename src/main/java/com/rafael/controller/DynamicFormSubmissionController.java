package com.rafael.controller;

import java.util.List;
import java.util.Map;

import com.rafael.rest.QueryResponse;
import com.rafael.rest.SubmissionResponse;
import com.rafael.service.DynamicFormSubmissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class DynamicFormSubmissionController {

  @Autowired
  private DynamicFormSubmissionService formSubmittionService;

  @PostMapping("/api/forms/{form_id}/entries")
  public ResponseEntity<SubmissionResponse> processSubmissions(@PathVariable("form_id") Integer formId,
      @RequestBody Map<String, Object> formulary) {
    SubmissionResponse processedSubmittion;
    try {
      processedSubmittion = formSubmittionService.submitFormulary(formId, formulary);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(processedSubmittion);
  }

  @GetMapping("/api/forms/{form_id}/entries")
  public ResponseEntity<QueryResponse> processSubmissionsQuery(@PathVariable("form_id") Integer formId) {
    QueryResponse processedQuery;
    try {
      processedQuery = formSubmittionService.queryFoundationDate(formId);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e);
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(processedQuery);
  }
}
