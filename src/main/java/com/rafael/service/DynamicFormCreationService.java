package com.rafael.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rafael.exceptions.InvalidDataTypeException;
import com.rafael.rest.FormResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicFormCreationService {

  @Autowired
  private DynamicFormFileService fileService;

  private final List<String> VALID_TYPES = Arrays.asList("integer", "boolean", "location", "date", "string");
  private Map<Integer, Map<String, Object>> formularyMap = new HashMap<>();

  public void setFormularyMap(Map<Integer, Map<String, Object>> formularyMap) {
    this.formularyMap = formularyMap;
  }

  public FormResponse formularyCreator(Map<String, Object> attributes) throws InvalidDataTypeException {
    for (Map.Entry<String, Object> entry : attributes.entrySet()) {
      if (!VALID_TYPES.contains(StringUtils.lowerCase(entry.getValue().toString()))) {
        throw new InvalidDataTypeException("Invalid type: " + entry.getValue(), this.getClass());
      }
    }
    Integer mapSize = formularyMap.size();
    formularyMap.put(mapSize, attributes);
    fileService.writeFormAndSubmissions(formularyMap, null);
    return new FormResponse(mapSize, "success");
  }

  public Map<String, Object> getCreateForm(Integer formId) {
    return formularyMap.get(formId);
  }
}
