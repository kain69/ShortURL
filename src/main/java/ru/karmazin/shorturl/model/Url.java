package ru.karmazin.shorturl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "url")
@Getter
@Setter
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @NotNull
    private String originalUrl;

    @Column(unique = true)
    private String shortUrl;

    @Column
    @NotNull
    private Date createdDate;

    @Column
    @NotNull
    private int countRequests;
}