package com.clas.starlite.webapp.dto;

import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
public class SolutionDTO {
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
    private long modified;
    private int status;
    private long revision;
    private String rootParentId;
    private List<String> precedenceIds;
    private List<SolutionDTO> solutions;
    private List<SolutionRuleDTO> rules;

    public SolutionDTO(String id, String desc, String name, List<String> infoUrl, List<String> caseStudy, List<String> video, String trial, String parentId, boolean isGroup, long created, long modified, int status, long revision, String rootParentId, List<String> precedenceIds) {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.infoUrl = infoUrl;
        this.caseStudy = caseStudy;
        this.video = video;
        this.trial = trial;
        this.parentId = parentId;
        this.isGroup = isGroup;
        this.created = created;
        this.modified = modified;
        this.status = status;
        this.revision = revision;
        this.rootParentId = rootParentId;
        this.precedenceIds = precedenceIds;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<SolutionDTO> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<SolutionDTO> solutions) {
        this.solutions = solutions;
    }

    public String getRootParentId() {
        return rootParentId;
    }

    public void setRootParentId(String rootParentId) {
        this.rootParentId = rootParentId;
    }

    public List<SolutionRuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<SolutionRuleDTO> rules) {
        this.rules = rules;
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
}
