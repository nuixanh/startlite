package com.clas.starlite.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private String modifiedBy;
    private long modified;
    private String parentId;
    private String rootParentId;
    private Set<String> sections;
    @Indexed
    private int status;
    private long revision;
    private long myRevision;
    @DBRef
    private List<Scenario> scenarios;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public String getRootParentId() {
        return StringUtils.isBlank(rootParentId)? this.id : rootParentId;
    }

    public void setRootParentId(String rootParentId) {
        this.rootParentId = rootParentId;
    }

    public Set<String> getSections() {
        return sections;
    }

    public void setSections(Set<String> sections) {
        this.sections = sections;
    }

    public long getMyRevision() {
        return myRevision;
    }

    public void setMyRevision(long myRevision) {
        this.myRevision = myRevision;
    }
}
