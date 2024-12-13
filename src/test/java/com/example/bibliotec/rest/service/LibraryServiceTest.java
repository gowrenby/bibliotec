package com.example.bibliotec.rest.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.bibliotec.domain.model.Book;
import com.example.bibliotec.domain.model.BookDTO;
import com.example.bibliotec.infrastructure.LibraryRepository;
import com.example.bibliotec.rest.exception.BookNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {
  @InjectMocks
  LibraryServiceImpl libraryService;

  @Mock
  LibraryRepository libraryRepository;

  @Test
  void testFindBookByIsbn() throws BookNotFoundException {
    Book book = Book.builder()
        .isbn("ISBN-123")
        .title("Book of Indexes")
        .author("Book Binder")
        .publicationYear(2020)
        .availableCopies(3)
        .build();
    Mockito.when(libraryRepository.findById("ISBN-123")).thenReturn(Optional.of(book));

    Optional<BookDTO> foundBook = libraryService.findBookByISBN("ISBN-123");
    assertTrue(foundBook.isPresent());

  }
}