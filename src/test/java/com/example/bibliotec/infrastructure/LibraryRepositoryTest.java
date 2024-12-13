package com.example.bibliotec.infrastructure;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bibliotec.domain.model.Book;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LibraryRepositoryTest {

  @Autowired
  LibraryRepository libraryRepository;

  private Book testBook;

  @BeforeEach
  public void setUp() {
    testBook = Book.builder()
        .isbn("ISBN-123")
        .title("Book of Indexes")
        .author("Book Binder")
        .publicationYear(2020)
        .availableCopies(3)
        .build();
    libraryRepository.save(testBook);
  }

  @AfterEach
  public void tearDown() {
    libraryRepository.delete(testBook);
  }

  @Test
  @DisplayName("Test: find book by ISBN")
  public void findByIsbnTest() {
    Book savedBook = libraryRepository.findById("ISBN-123").get();
    assertNotNull(savedBook);
  }

  @Test
  @DisplayName("Test: find book by Author")
  public void findByAuthorTest() {
    List<Book> savedBooks = libraryRepository.findByAuthor("Book Binder").get();
    assertNotNull(savedBooks);
  }

  @Test
  @DisplayName("Test: Delete book")
  public void removeBookTest() {
    Book savedBook = libraryRepository.findById("ISBN-123").get();
    libraryRepository.delete(savedBook);
    assertTrue(libraryRepository.findById("ISBN-123").isEmpty());
  }
}