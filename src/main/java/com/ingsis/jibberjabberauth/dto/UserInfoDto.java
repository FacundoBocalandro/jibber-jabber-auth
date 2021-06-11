package com.ingsis.jibberjabberauth.dto;

import com.ingsis.jibberjabberauth.models.User;

public class UserInfoDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String role;

    private UserInfoDto(Long id, String username, String firstName, String lastName, String role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public static UserInfoDto fromUser(User user) {
        return new UserInfoDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getRole());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }
}
