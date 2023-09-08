package ru.karmazin.shorturl.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    private String username;
    private String password;
}
