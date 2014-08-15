package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by sonnt4 on 8/15/2014.
 */
@Document(collection="scenario")
public class Scenario {
    private String id;
    private String name;
    private Map<String, String> nameMap;
    private long created;
    private String createdBy;
    private long modified;
    private boolean isSection;
    private String parentId;
    private boolean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean isSection) {
        this.isSection = isSection;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}