package com.example.bibliotec.infrastructure;

import com.example.bibliotec.domain.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository  extends JpaRepository<Book, String> {
  Optional<List<Book>> findByAuthor(String author);
}
