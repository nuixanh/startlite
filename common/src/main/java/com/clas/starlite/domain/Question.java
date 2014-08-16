package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by sonnt4 on 8/15/2014.
 */
@Document(collection="question")
public class Question {
    private String id;
    private String desc;
    private Map<String, String> descMap;
    private long created;
    private String createdBy;
    private long modified;
    @Indexed
    private String scenarioId;
    private boolean status;
    @Indexed
    private long revision;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Map<String, String> getDescMap() {
        return descMap;
    }

    public void setDescMap(Map<String, String> descMap) {
        this.descMap = descMap;
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

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }
}
