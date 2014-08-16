package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Son on 8/16/14.
 */
@Document(collection="revision")
public class Revision {
    private String id;
    @Indexed
    private String type;
    @Indexed
    private long version;
    private Date whenUpdated;

    public Revision(String id, String type, long version, Date whenUpdated) {
        this.id = id;
        this.type = type;
        this.version = version;
        this.whenUpdated = whenUpdated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getWhenUpdated() {
        return whenUpdated;
    }

    public void setWhenUpdated(Date whenUpdated) {
        this.whenUpdated = whenUpdated;
    }
}
