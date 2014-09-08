package com.clas.starlite.webapp.dto;

import java.util.List;

/**
 * Created by sonnt4 on 8/20/2014.
 */
public class SolutionRuleDTO {
    private String id;
    private int type;
    private String solutionId;
    private long created;
    private String createdBy;
    private String modifiedBy;
    private long modified;
    private int status;
    private long revision;

    private List<List<RuleConditionDTO>> conditions;

    public SolutionRuleDTO(String id, int type, String solutionId, long created, String createdBy, String modifiedBy, long modified, int status, long revision, List<List<RuleConditionDTO>> conditions) {
        this.id = id;
        this.type = type;
        this.solutionId = solutionId;
        this.created = created;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.modified = modified;
        this.status = status;
        this.revision = revision;
        this.conditions = conditions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
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

    public List<List<RuleConditionDTO>> getConditions() {
        return conditions;
    }

    public void setConditions(List<List<RuleConditionDTO>> conditions) {
        this.conditions = conditions;
    }
}
