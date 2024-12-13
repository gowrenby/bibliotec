package com.example.bibliotec.rest.exception;

public class BookNotFoundException extends Exception {
  public BookNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
