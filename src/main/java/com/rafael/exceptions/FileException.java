package com.rafael.exceptions;

/**
 * Created by c5279363 on 2/18/20.
 */
public class FileException extends RuntimeException {

  public FileException(String message, Class sourceClass) {
    super(message);
  }

  public FileException(String message, Throwable cause, Class sourceClass) {
    super(message, cause);
  }
}
