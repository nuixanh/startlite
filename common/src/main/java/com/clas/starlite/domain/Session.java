package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Son on 8/14/14.
 */
@Document(collection="session")
public class Session {
    private String id;
    private String userId;
    private long created;
    private long expired;

    public Session(String id, String userId, long created, long expired) {
        this.id = id;
        this.userId = userId;
        this.created = created;
        this.expired = expired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }
}
