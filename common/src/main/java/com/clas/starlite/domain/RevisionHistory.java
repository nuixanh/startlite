package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Son on 8/16/14.
 */
@Document(collection="revision_history")
public class RevisionHistory {
    private String id;
    @Indexed
    private String type;
    private long version;
    @Indexed
    private String action;
    private String entityId;
    private Date whenCreated;

    public RevisionHistory(String id, String type, long version, String action, String entityId, Date whenCreated) {
        this.id = id;
        this.type = type;
        this.version = version;
        this.action = action;
        this.entityId = entityId;
        this.whenCreated = whenCreated;
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

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
