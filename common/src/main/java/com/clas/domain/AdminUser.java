package com.clas.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Son on 8/6/14.
 */
@Document(collection="admin_user")
public class AdminUser {
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public int role;

    public AdminUser() {
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public AdminUser(String username, String password, String firstName,
                     String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return "First Name:" + this.firstName + " Last Name:" + this.lastName + " Username:" + this.username ;
    }
}
