package com.example.bibliotec.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import com.example.bibliotec.domain.model.BookDTO;
import com.example.bibliotec.rest.exception.BookNotFoundException;
import com.example.bibliotec.rest.service.LibraryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(LibraryController.class)
class LibraryControllerTest {
  @MockitoBean
  LibraryServiceImpl libraryService;
  @Autowired
  MockMvc mockMvc;

  @Test
  public void testFindByIsbn_OK() throws Exception {
    BookDTO bookDTO = new BookDTO("ISBN-123", "Book of Indexes",
        "Book Binder", 2020, 3);

    Mockito.when(libraryService.findBookByISBN("ISBN-123")).thenReturn(Optional.of(bookDTO));

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/library/book/isbn/ISBN-123"))
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Book of Indexes")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("Book Binder")));
  }

  @Test
  public void testFindByIsbn_NOT_FOUND() throws Exception {
    Mockito.when(libraryService.findBookByISBN("ISBN-1")).thenThrow(new BookNotFoundException("Book not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/library/book/isbn/ISBN-1"))
        .andExpect(MockMvcResultMatchers.status().is(404));
  }

  @Test
  public void testFindByAuthor_OK() throws Exception {
    BookDTO bookDTO = new BookDTO("ISBN-123", "Book of Indexes",
        "Book Binder", 2020, 3);

    Mockito.when(libraryService.findBooksByAuthor("Book Binder")).thenReturn(List.of(bookDTO));

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/library/book/author/Book Binder"))
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].author", Matchers.is("Book Binder")));
  }

  @Test
  public void testFindByAuthor_NOT_FOUND() throws Exception {
    Mockito.when(libraryService.findBooksByAuthor("Binder")).thenThrow(new BookNotFoundException("Books not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/library/book/author/Binder"))
        .andExpect(MockMvcResultMatchers.status().is(404));
  }

  @Test
  public void testAddBook_CREATED() throws Exception {
    com.example.bibliotec.domain.model.in.BookDTO bookDTO =
        new com.example.bibliotec.domain.model.in.BookDTO("ISBN-123", "Book of Indexes",
        "Book Binder", 2020, 3);

    ArgumentCaptor<com.example.bibliotec.domain.model.in.BookDTO> valueCapture =
        ArgumentCaptor.forClass(com.example.bibliotec.domain.model.in.BookDTO.class);
    Mockito.doNothing().when(libraryService).addBook(valueCapture.capture());

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String bookJson = ow.writeValueAsString(bookDTO);

    mockMvc.perform(MockMvcRequestBuilders.post("/v1/library/book")
        .contentType(APPLICATION_JSON_UTF8)
        .content(bookJson));
    assertTrue(bookDTO.getIsbn().equals(valueCapture.getValue().getIsbn()));
  }

  @Test
  public void testRemoveBook_NO_CONTENT() throws Exception {
    ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
    Mockito.doNothing().when(libraryService).removeBook(valueCapture.capture());

    mockMvc.perform(MockMvcRequestBuilders.delete("/v1/library/book/isbn/ISBN-123"));
    Mockito.verify(libraryService, Mockito.times(1)).removeBook("ISBN-123");
  }

  @Test
  public void testBorrowBook_OK() throws Exception {
    ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
    Mockito.doNothing().when(libraryService).borrowBook(valueCapture.capture());

    mockMvc.perform(MockMvcRequestBuilders.put("/v1/library/book/isbn/ISBN-123/borrow"))
        .andExpect(MockMvcResultMatchers.status().is(200));
    Mockito.verify(libraryService, Mockito.times(1)).borrowBook("ISBN-123");
    assertEquals("ISBN-123", valueCapture.getValue());
  }

  @Test
  public void testReturnBook_OK() throws Exception {
    ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
    Mockito.doNothing().when(libraryService).returnBook(valueCapture.capture());

    mockMvc.perform(MockMvcRequestBuilders.put("/v1/library/book/isbn/ISBN-123/return"))
        .andExpect(MockMvcResultMatchers.status().is(200));
    Mockito.verify(libraryService, Mockito.times(1)).returnBook("ISBN-123");
    assertEquals("ISBN-123", valueCapture.getValue());
  }
}