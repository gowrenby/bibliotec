package com.example.bibliotec.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Book implements Serializable {
  @Id
  @Column(name="isbn")
  private String isbn;
  @Column(name="title")
  private String title;
  @Column(name="author")
  private String author;
  @Column(name="publicationYear")
  private int publicationYear;
  @Column(name="availableCopies")
  private int availableCopies;
}
