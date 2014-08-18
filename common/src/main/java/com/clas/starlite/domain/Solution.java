package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
@Document(collection="solution")
public class Solution {
    private String id;
    private String desc;
    private String attr;
    private String parentId;
    private boolean isGroup;
    private long created;
    private String createdBy;
    private String modifiedBy;
    private long modified;
    private int status;
    private long revision;
    private String rootParentId;

    @DBRef
    private List<Solution> solutions;
    @DBRef
    private List<SolutionRule> rules;

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

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
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

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public List<SolutionRule> getRules() {
        return rules;
    }

    public void setRules(List<SolutionRule> rules) {
        this.rules = rules;
    }

    public String getRootParentId() {
        return rootParentId;
    }

    public void setRootParentId(String rootParentId) {
        this.rootParentId = rootParentId;
    }
}
