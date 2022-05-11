package com.rafael.controller;

import java.util.Map;

import com.rafael.rest.FormResponse;
import com.rafael.service.DynamicFormCreationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class DynamicFormCreationController {

  @Autowired
  private DynamicFormCreationService creationService;

  @PostMapping("/api/forms/definitions")
  public ResponseEntity<FormResponse> processForm(@RequestBody Map<String, Object> atributes) {
    FormResponse formResponse;
    try {
      formResponse = creationService.formularyCreator(atributes);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(formResponse);
  }
}
