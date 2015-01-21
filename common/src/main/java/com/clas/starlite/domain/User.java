package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Son on 8/6/14.
 */
@Document(collection="user")
public class User {
    private String id;
    private String password;
    private String name;
    private String firstName;
    private String lastName;
    @Indexed
    private String email;
    private int role;
    private long created;
    private String createdBy;
    private long modified;
    private String link;
    private String locale;

    private long surveyCount;

    public User(String id, String password, String name, String firstName, String lastName, String email, int role, long created, String createdBy, long modified, String link, String locale) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.link = link;
        this.locale = locale;
    }

    public User() {
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public long getSurveyCount() {
        return surveyCount;
    }

    public void setSurveyCount(long surveyCount) {
        this.surveyCount = surveyCount;
    }
}
