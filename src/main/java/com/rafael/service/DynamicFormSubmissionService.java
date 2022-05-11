package com.rafael.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rafael.data.Location;
import com.rafael.exceptions.InvalidDataTypeException;
import com.rafael.exceptions.InvalidFieldException;
import com.rafael.rest.QueryResponse;
import com.rafael.rest.SubmissionResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicFormSubmissionService {

  @Autowired
  private DynamicFormCreationService dynamicFormCreation;

  @Autowired
  private DynamicFormFileService fileService;

  private static final long MILISECONDS_IN_YEAR = 31536000000L;

  private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

  private Map<Integer, Map<Integer, Map<String, Object>>> formularyAndSubmittionMap = new HashMap<>();

  public void setFormularyAndSubmissionMap(Map<Integer, Map<Integer, Map<String, Object>>> formularyAndSubmittionMap) {
    this.formularyAndSubmittionMap = formularyAndSubmittionMap;
  }

  public SubmissionResponse submitFormulary(Integer formId, Map<String, Object> submittion)
      throws InvalidDataTypeException {

    Map<String, Object> formFormat = dynamicFormCreation.getCreateForm(formId);
    if (formFormat == null) {
      throw new InvalidFieldException("Invalid formulary id: " + formId, this.getClass());
    }

    if (formFormat.size() != submittion.size()) {
      throw new InvalidFieldException("Submission contains extra or missing fields, according to formulary:" + formId,
          this.getClass());
    }

    for (Map.Entry<String, Object> entry : submittion.entrySet()) {
      if (formFormat.get(entry.getKey()) == null) {
        throw new InvalidFieldException("Field " + entry.getKey() + " not present in formulary.", this.getClass());
      }
      validateEntry(formFormat.get(entry.getKey()).toString(), entry.getValue());
    }

    Map<Integer, Map<String, Object>> formularyMap = formularyAndSubmittionMap.get(formId);
    if (formularyMap == null) {
      formularyMap = new HashMap<>();
    }
    Integer submittionSize = formularyMap.size();
    formularyMap.put(submittionSize, submittion);
    formularyAndSubmittionMap.put(formId, formularyMap);
    fileService.writeFormAndSubmissions(null, formularyAndSubmittionMap);
    return new SubmissionResponse(formId, submittionSize, "success");
  }

  public QueryResponse queryFoundationDate(Integer formId) {
    List<Map<String, Object>> entries = new ArrayList<>();
    Map<Integer, Map<String, Object>> submissionsMap = formularyAndSubmittionMap.get(formId);
    if (submissionsMap != null) {
      for (Map.Entry<Integer, Map<String, Object>> entry : submissionsMap.entrySet()) {
        if (entry.getValue().get("date") != null) {
          if (dateSuperiorToThreeYears(entry.getValue().get("date").toString())) {
            entries.add(entry.getValue());
          }
        } else {
          throw new InvalidFieldException("Field 'date' not present in formulary " + formId, this.getClass());
        }
      }
      return new QueryResponse("success", entries);
    }
    return new QueryResponse("success", Collections.EMPTY_LIST);
  }

  private Boolean dateSuperiorToThreeYears(String date) {
    Date convertedDate;
    try {
      convertedDate = dateFormatter.parse(date);
      long convertedDateInMilis = convertedDate.getTime();
      long timeDifference = System.currentTimeMillis() - convertedDateInMilis;
      if (Math.floorDiv(timeDifference, MILISECONDS_IN_YEAR * 3) >= 1) {
        return true;
      }
      return false;
    } catch (ParseException e) {
      throw new InvalidFieldException("Invalid value for date comparisson: " + date, this.getClass());
    }
  }

  private void validateEntry(String format, Object value) {
    String valueString = StringUtils.trim(value.toString());
    switch (StringUtils.lowerCase(format)) {
      case "integer":
        this.validateInteger(valueString);
        break;
      case "boolean":
        this.validateBoolean(valueString);
        break;
      case "location":
        this.validateLocation(valueString);
        break;
      case "date":
        this.validateDate(valueString);
        break;
      case "string":
        break;
      default:
        throw new Error("Invalid state");
    }
    return;
  }

  private void validateBoolean(String value) {
    if (value.equalsIgnoreCase("t") || value.equalsIgnoreCase("true")) {
      return;
    }
    if (value.equalsIgnoreCase("f") || value.equalsIgnoreCase("false")) {
      return;
    }
    throw new InvalidFieldException("Invalid value for boolean: " + value, this.getClass());
  }

  private void validateInteger(String value) {
    try {
      Integer.valueOf(value);
    } catch (Exception e) {
      throw new InvalidFieldException("Invalid value for integer: " + value, this.getClass());
    }

  }

  private void validateLocation(String value) {
    try {
      Location.fromInputString(value);
    } catch (Exception e) {
      throw new InvalidFieldException("Invalid value for location: " + value, this.getClass());
    }
    return;
  }

  private void validateDate(String value) {
    dateFormatter.setLenient(false);
    try {
      Date parsedDate = dateFormatter.parse(value);
      if (System.currentTimeMillis() < parsedDate.getTime()) {
        throw new InvalidFieldException("Invalid value date: " + value + " surpasses current time", this.getClass());
      }
    } catch (ParseException e) {
      throw new InvalidFieldException("Invalid value for date: " + value, this.getClass());
    }
    return;
  }
}
