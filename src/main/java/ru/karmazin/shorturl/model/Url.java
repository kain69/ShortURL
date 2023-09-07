package ru.karmazin.shorturl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "url", uniqueConstraints = {
        @UniqueConstraint(columnNames = "shortUrl")
})
@Getter
@Setter
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String originalUrl;

    private String shortUrl;

    private Date createdDate;

    private int countRequests;
}