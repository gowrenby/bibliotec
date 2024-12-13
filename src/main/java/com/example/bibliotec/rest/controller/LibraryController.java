package com.example.bibliotec.rest.controller;

import com.example.bibliotec.domain.model.BookDTO;
import com.example.bibliotec.rest.exception.BookNotAvailableException;
import com.example.bibliotec.rest.exception.BookNotFoundException;
import com.example.bibliotec.rest.service.LibraryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {
  @Autowired
  private LibraryService libraryService;

  @GetMapping(value = "/book/isbn/{isbn}")
  public ResponseEntity<BookDTO> findByIsbn(@PathVariable String isbn) {
    try {
      Optional<BookDTO> book = libraryService.findBookByISBN(isbn);
      return ResponseEntity.status(HttpStatus.OK).body(book.get());
    }
    catch(BookNotFoundException rnfe) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BookDTO not found");
    }
  }

  @GetMapping(value = "/book/author/{author}")
  public ResponseEntity<List<BookDTO>> findByAuthor(@PathVariable String author) {
    try {
      List<BookDTO> books = libraryService.findBooksByAuthor(author);
      return ResponseEntity.status(HttpStatus.OK).body(books);
    }
    catch (BookNotFoundException rnfe) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Books not found");
    }
  }

  @PostMapping(value = "/book")
  public ResponseEntity addBook(@RequestBody com.example.bibliotec.domain.model.in.BookDTO bookDTO) {
    libraryService.addBook(bookDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping(value = "/book/isbn/{isbn}")
  public ResponseEntity removeBook(@PathVariable String isbn) {
    try {
      libraryService.removeBook(isbn);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
  }

  @PutMapping(value = "/book/isbn/{isbn}/borrow")
  public ResponseEntity borrowBook(@PathVariable String isbn) {
    try {
      libraryService.borrowBook(isbn);
      return ResponseEntity.status(HttpStatus.OK).build();
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
    } catch (BookNotAvailableException e) {
      throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Book not available");
    }
  }

  @PutMapping(value = "/book/isbn/{isbn}/return")
  public ResponseEntity returnBook(@PathVariable String isbn) {
    try {
      libraryService.returnBook(isbn);
      return ResponseEntity.status(HttpStatus.OK).build();
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
    }
  }
}
