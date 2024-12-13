package com.example.bibliotec.rest.exception;

public class BookNotAvailableException extends Exception {
  public BookNotAvailableException(String errorMessage) {
    super(errorMessage);
  }
}
