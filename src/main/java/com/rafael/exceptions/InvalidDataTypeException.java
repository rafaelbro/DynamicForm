package com.rafael.exceptions;

/**
 * Created by c5279363 on 2/18/20.
 */
public class InvalidDataTypeException extends RuntimeException {

  public InvalidDataTypeException(String message, Class sourceClass) {
    super(message);
  }

  public InvalidDataTypeException(String message, Throwable cause, Class sourceClass) {
    super(message, cause);
  }
}
