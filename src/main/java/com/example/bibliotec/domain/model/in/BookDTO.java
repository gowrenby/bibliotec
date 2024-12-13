package com.example.bibliotec.domain.model.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDTO {
  private String isbn;
  private String title;
  private String author;
  private int publicationYear;
  private int availableCopies;
}
