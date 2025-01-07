package com.modsen.commonmodels.models.entities;

import com.modsen.commonmodels.enums.attributes.CreationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column
    private String genre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CreationStatus creationStatus;

    /**
        Creates copy for current object from provided
    */
    public void copy(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.genre = book.getGenre();
        this.description = book.getDescription();
        this.author = book.getAuthor();
    }
}
