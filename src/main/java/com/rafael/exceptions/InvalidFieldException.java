package com.rafael.exceptions;

/**
 * Created by c5279363 on 2/18/20.
 */
public class InvalidFieldException extends RuntimeException {

  public InvalidFieldException(String message, Class sourceClass) {
    super(message);
  }

  public InvalidFieldException(String message, Throwable cause, Class sourceClass) {
    super(message, cause);
  }
}
