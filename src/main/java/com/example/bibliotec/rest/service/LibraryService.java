package com.example.bibliotec.rest.service;

import com.example.bibliotec.domain.model.BookDTO;
import com.example.bibliotec.rest.exception.BookNotAvailableException;
import com.example.bibliotec.rest.exception.BookNotFoundException;
import java.util.List;
import java.util.Optional;

public interface LibraryService {
  void addBook(com.example.bibliotec.domain.model.in.BookDTO book);
  void removeBook(String isbn) throws BookNotFoundException;
  Optional<BookDTO> findBookByISBN(String isbn) throws BookNotFoundException;
  List<BookDTO> findBooksByAuthor(String author) throws BookNotFoundException;
  void borrowBook(String isbn) throws BookNotFoundException, BookNotAvailableException;
  void returnBook(String isbn) throws BookNotFoundException;

}
