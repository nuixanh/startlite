package com.clas.starlite.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
@Document(collection="solution")
public class Solution {
    private String id;
    private String name;
    private String desc;
    private List<String> infoUrl;
    private List<String> caseStudy;
    private List<String> video;
    private String trial;
    private String parentId;
    private boolean isGroup;
    private long created;
    private String createdBy;
    private String modifiedBy;
    private long modified;
    private int status;
    private long revision;
    private long myRevision;
    private String rootParentId;
    private List<String> precedenceIds;
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
        return StringUtils.isNotBlank(rootParentId)? rootParentId: id;
    }

    public void setRootParentId(String rootParentId) {
        this.rootParentId = rootParentId;
    }

    public long getMyRevision() {
        return myRevision;
    }

    public void setMyRevision(long myRevision) {
        this.myRevision = myRevision;
    }

    public List<String> getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(List<String> infoUrl) {
        this.infoUrl = infoUrl;
    }

    public List<String> getCaseStudy() {
        return caseStudy;
    }

    public void setCaseStudy(List<String> caseStudy) {
        this.caseStudy = caseStudy;
    }

    public List<String> getVideo() {
        return video;
    }

    public void setVideo(List<String> video) {
        this.video = video;
    }

    public String getTrial() {
        return trial;
    }

    public void setTrial(String trial) {
        this.trial = trial;
    }

    public List<String> getPrecedenceIds() {
        return precedenceIds;
    }

    public void setPrecedenceIds(List<String> precedenceIds) {
        this.precedenceIds = precedenceIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
