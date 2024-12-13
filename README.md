# Simple Library Demo

This is a simple service demo mimicking a library of books using -
- Spring Boot 3.4.0
- H2 db
- JPA
- Lombok

The project is structured to separate business logic from persistence concerns roughly using
DDD and Hexagon Arch principles.

## Config
### Application, JPA and DB
Application related configurations including JPA and H2 are placed in application.properties
### DB data
H2 db is initialised from values stored in data.sql
### Caching
Caching is configured with `com.example.bibliotec.rest.config.LibraryCacheCustomizer` and 
`com.example.bibliotec.rest.config.CacheConfig` classes

## Build and Run
#### Build
> mvn clean package
#### Run
> mvn spring-boot:run

## API endpoints
| Feature endpoint                                          | Curl command                                                                                                                                                                                                                                                  | Method | Payload/Param   | Response Status | Response Body (Desc.)  |
|-----------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------|-----------------|-----------------|------------------------|
| Find book by ISBN <br/>/v1/library/book/isbn/{isbn}       | curl --location 'http://localhost:8080/v1/library/book/isbn/0-307-26543-9'                                                                                                                                                                                    | GET    | ISBN (String)   | 200             | Book object            |
|                                                           | curl --location 'http://localhost:8080/v1/library/book/isbn/0-307-26543'                                                                                                                                                                                      |        |                 | 404             | (Book not found)       |
| Find books by Author<br/>/v1/library/book/author/{author} | curl --location 'http://localhost:8080/v1/library/book/author/Kazuo Ishiguro'                                                                                                                                                                                 | GET    | Author (String) | 200             | List of Book objects   |
|                                                           | curl --location 'http://localhost:8080/v1/library/book/author/Stephen King'                                                                                                                                                                                   |        |                 | 404             | (Books not found)      |
| Add a book <br/>/v1/library/book                          | curl --location 'http://localhost:8080/v1/library/book' --header 'Content-Type: application/json' --data '{    "isbn": "978-0-099-27396-7", "title": "The Old Man And The Sea", "author": "Ernest Hemingway", "publicationYear": 1952, "availableCopies": 3}' | POST   | Book object     | 201             |                        |
|                                                           | curl --location 'http://localhost:8080/v1/library/book' --header 'Content-Type: application/json' --data '{    "title": "The Old Man And The Sea", "author": "Ernest Hemingway", "publicationYear": 1952, "availableCopies": 3}'                              |        |                 | 500             | (Book details missing) |
| Remove book by ISBN <br/>/v1/library/book/isbn/{isbn}     | curl --location --request DELETE 'http://localhost:8080/v1/library/book/isbn/0-307-26543-9'                                                                                                                                                                                    | DELETE | ISBN (String)   | 204             |                        |
|                                                           | curl --location --request DELETE 'http://localhost:8080/v1/library/book/isbn/0-307-26543'                                                                                                                                                                                      |        |                 | 404             | (Book not found)       |
| Borrow book <br/>/v1/library/book/isbn/{isbn}/borrow      | curl --location --request PUT 'http://localhost:8080/v1/library/book/isbn/0-307-26543-9/borrow'                                                                                                                                                               | PUT    | ISBN (String)   | 200             |                        |
|                                                           | curl --location --request PUT 'http://localhost:8080/v1/library/book/isbn/0-307-26543-9/borrow'                                                                                                                                                               |        |                 | 204             | (Book not available)   |
|                                                           | curl --location --request PUT 'http://localhost:8080/v1/library/book/isbn/0-307-26543/borrow'                                                                                                                                                                 |        |                 | 404             | (Book not found)       |
| Return book <br/>/v1/library/book/isbn/{isbn}/return      | curl --location --request PUT 'http://localhost:8080/v1/library/book/isbn/0-307-26543-9/return'                                                                                                                                                               | PUT    | ISBN (String)   | 200             |                        |
|                                                           | curl --location --request PUT 'http://localhost:8080/v1/library/book/isbn/0-307-26543/return'                                                                                                                                                                 |        |                 | 404             | (Book not found)       |

## Design decisions

### Aggregate root
This project idea appears specifically to be an issue of a library's management. As such, this 
solution tries to address it from the Library's perspective so that all future enhancements can be 
applied with ease.
- Although the Library class is not persisted, it is meant to manage lifecycles of Books. However, 
the only control it asserts is over the number of available copies for each book. It could in future 
be considered for persisting with changes to book availability logic.
- Library class is assisted in its duties by the LibraryService as it is best positioned to interact 
with the persistence layer.

### Book class
- Book class is designed to behave more like a value object however its immutability can be improved
- The input and output DTOs of Book are separated

### Exception handling
- Exception handling uses `ResponseStatusException` and not any global exception handling 
(`ControllerAdvice` etc) mainly due to the size and demo nature of the project.

### Caching
A simple caching is configured to cache calls to LibraryService method `findBookByISBN()`

### Test coverage
Test coverage is incomplete. In particular, logic in Library class needs covering.