package com.rafael.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.rafael.exceptions.FileException;

import org.springframework.stereotype.Service;

@Service
public class DynamicFormFileService {
  private static final String FORM_FORMAT_FILE = "FormFormat.txt";
  private static final String SUBMISSIONS_FILE = "Submissions.txt";

  public void writeFormAndSubmissions(Map<Integer, Map<String, Object>> formMap, Map<Integer, 
    Map<Integer, Map<String, Object>>> submissionMap) throws FileException {
    String currentFile = formMap == null ? SUBMISSIONS_FILE: FORM_FORMAT_FILE;
    try {
      FileOutputStream fos = new FileOutputStream(currentFile);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(formMap == null ? submissionMap: formMap);
      oos.close();
      fos.close();
      System.out.println("Serialized form format data is saved in " + currentFile);
    } catch (Exception ioe) {
      throw new FileException("Error writing to " + currentFile, this.getClass());
    }
  }

  public Map<Integer, Map<String, Object>> readFormFormat() throws FileException {    
    return (HashMap) readFile(FORM_FORMAT_FILE);
  }

  public Map<Integer, Map<Integer, Map<String, Object>>> readFormSubmission() throws FileException {
    return (HashMap) readFile(SUBMISSIONS_FILE);
  }

  private Object readFile(String file) throws FileException {
    Object object = new Object();
    try {
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream ois = new ObjectInputStream(fis);
      object = ois.readObject();
      ois.close();
      fis.close();
    } catch (Exception e) {
      throw new FileException("Error reading from " + file, this.getClass());
    }
    return object;
  }  
}
