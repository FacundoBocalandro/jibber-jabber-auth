package com.ingsis.jibberjabberauth.models;

public class RegisterUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    public RegisterUserDto(){

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
