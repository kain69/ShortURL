package ru.karmazin.shorturl.dto;

import lombok.Getter;
import lombok.Setter;
import ru.karmazin.shorturl.model.Role;
import ru.karmazin.shorturl.model.Url;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private Set<Role> roles = new HashSet<>();
    private Set<Url> urls = new HashSet<>();

    public UserDto() {}

    public UserDto(Long id, String username, Set<Role> roles, Set<Url> urls) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.urls = urls;
    }
}
