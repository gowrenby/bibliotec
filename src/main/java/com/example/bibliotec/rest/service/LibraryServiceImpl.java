package com.example.bibliotec.rest.service;

import com.example.bibliotec.domain.model.Book;
import com.example.bibliotec.domain.model.BookDTO;
import com.example.bibliotec.domain.model.Library;
import com.example.bibliotec.infrastructure.LibraryRepository;
import com.example.bibliotec.rest.exception.BookNotAvailableException;
import com.example.bibliotec.rest.exception.BookNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class LibraryServiceImpl implements LibraryService {
  private final LibraryRepository libraryRepository;
  private final Library library;

  public LibraryServiceImpl(LibraryRepository libraryRepository, Library library) {
    this.libraryRepository = libraryRepository;
    this.library = library;
  }

  @Override
  public void addBook(com.example.bibliotec.domain.model.in.BookDTO bookDTO) {
    this.libraryRepository.save(convertToEntity(bookDTO));
  }

  @Override
  public void removeBook(String isbn) throws BookNotFoundException{
    Optional<Book> book = this.libraryRepository.findById(isbn);
    if (book.isPresent()) {
      this.libraryRepository.delete(book.get());
    }
    else {
      throw new BookNotFoundException("Book with isbn "+ isbn +" not found");
    }
  }

  @Override
  @Cacheable("books")
  public Optional<BookDTO> findBookByISBN(String isbn) throws BookNotFoundException {
    Optional<Book> book = libraryRepository.findById(isbn);
    if (book.isEmpty()) {
      throw new BookNotFoundException("Book with isbn "+ isbn +" not found");
    }
    return book.map(this::convertToDTO);
  }

  @Override
  public List<BookDTO> findBooksByAuthor(String author) throws BookNotFoundException {
    Optional<List<Book>> books = libraryRepository.findByAuthor(author);
    if (books.isEmpty()) {
      throw new BookNotFoundException("Books by author "+ author + " not found");
    }
    return books.get().stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  @Override
  public void borrowBook(String isbn) throws BookNotFoundException, BookNotAvailableException {
    Optional<Book> book = libraryRepository.findById(isbn);
    int totalCount = book.get().getAvailableCopies();
    int availableCount = library.getAvailableCount(isbn, totalCount);
    if (book.isPresent() && availableCount > 0 && totalCount >= availableCount) {
      library.borrowBook(isbn, availableCount-1);
    }
    else if (book.isPresent() && book.get().getAvailableCopies() == 0){
      throw new BookNotAvailableException("Book with isbn "+ isbn +" not found");
    }
    else {
      throw new BookNotFoundException("Book with isbn "+ isbn +" not found");
    }
  }

  @Override
  public void returnBook(String isbn) throws BookNotFoundException {
    Optional<Book> book = libraryRepository.findById(isbn);
    int totalCount = book.get().getAvailableCopies();
    int bookCount = library.getAvailableCount(isbn, totalCount);
    if (book.isPresent() && book.get().getAvailableCopies() > bookCount) {
      library.returnBook(isbn, bookCount+1);
    }
    else {
      throw new BookNotFoundException("Book with isbn "+ isbn +" not found");
    }
  }

  private BookDTO convertToDTO(Book book) {
    return new BookDTO(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublicationYear(),
        book.getAvailableCopies());
  }

  private Book convertToEntity(com.example.bibliotec.domain.model.in.BookDTO bookDTO) {
    return new Book(bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(),
        bookDTO.getPublicationYear(), bookDTO.getAvailableCopies());
  }
}
