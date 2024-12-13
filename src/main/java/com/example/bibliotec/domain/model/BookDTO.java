package com.example.bibliotec.domain.model;

public record BookDTO(String isbn, String title, String author, int publicationYear, int availableCopies) {}
