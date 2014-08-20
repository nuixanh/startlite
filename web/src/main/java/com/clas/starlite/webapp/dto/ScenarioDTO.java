package com.clas.starlite.webapp.dto;

import java.util.List;
import java.util.Set;

/**
 * Created by sonnt4 on 8/15/2014.
 */

public class ScenarioDTO {
    private String id;
    private String name;
    private long modified;
    private String parentId;
    private long revision;
    private int status;
    private String rootParentId;
    private List<ScenarioDTO> scenarios;
    private Set<String> sections;

    public ScenarioDTO(String id, String name, long modified, String parentId, long revision, int status, String rootParentId, Set<String> sections) {
        this.id = id;
        this.name = name;
        this.modified = modified;
        this.parentId = parentId;
        this.revision = revision;
        this.status = status;
        this.rootParentId = rootParentId;
        this.sections = sections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<ScenarioDTO> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ScenarioDTO> scenarios) {
        this.scenarios = scenarios;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRootParentId() {
        return rootParentId;
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
}
