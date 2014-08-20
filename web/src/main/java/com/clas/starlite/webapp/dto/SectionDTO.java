package com.clas.starlite.webapp.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by Son on 8/19/14.
 */
public class SectionDTO {
    private String id;
    private String name;
    private Map<String, String> nameMap;
    private long created;
    private String createdBy;
    private String modifiedBy;
    private long modified;
    private int status;
    private long revision;

    private List<QuestionDTO> questions;

    public SectionDTO(String id, String name, Map<String, String> nameMap, long created, String createdBy, String modifiedBy, long modified, int status, long revision) {
        this.id = id;
        this.name = name;
        this.nameMap = nameMap;
        this.created = created;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.modified = modified;
        this.status = status;
        this.revision = revision;
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

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
