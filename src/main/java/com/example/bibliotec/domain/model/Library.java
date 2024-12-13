package com.example.bibliotec.domain.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class Library {
  private Map<String, Integer> availableCount;

  public Library(){
    availableCount = new ConcurrentHashMap<>();
  }

  public int getAvailableCount(String isbn, int totalCount) {
    return availableCount.containsKey(isbn) ? availableCount.get(isbn) : totalCount;
  }

  public void borrowBook(String isbn, int updatedCount) {
    availableCount.put(isbn, updatedCount);
  }

  public void returnBook(String isbn, int updatedCount) {
    availableCount.put(isbn, updatedCount);
  }
}
