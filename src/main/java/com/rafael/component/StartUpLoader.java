package com.rafael.component;

import com.rafael.exceptions.FileException;
import com.rafael.service.DynamicFormCreationService;
import com.rafael.service.DynamicFormFileService;
import com.rafael.service.DynamicFormSubmissionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartUpLoader {

  @Autowired
  private DynamicFormFileService fileService;

  @Autowired
  private DynamicFormSubmissionService submissionService;

  @Autowired
  private DynamicFormCreationService formService;

  Logger logger = LoggerFactory.getLogger(StartUpLoader.class);

  @EventListener(ApplicationReadyEvent.class)
  public void loadDataFromFiles() {
    try {
      formService.setFormularyMap(fileService.readFormFormat());
      submissionService.setFormularyAndSubmissionMap(fileService.readFormSubmission());
    } catch (FileException e) {
      logger.warn("Could not load previous forms and submissions");
    }
  }
}
