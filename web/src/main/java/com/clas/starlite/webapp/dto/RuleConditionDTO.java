package com.clas.starlite.webapp.dto;

import java.util.List;

/**
 * Created by sonnt4 on 8/20/2014.
 */
public class RuleConditionDTO {
    private String id;
    private String solutionRuleId;
    private String questionId;
    private List<Integer> scoreList;
    private int operator;
    private long created;
    private long modified;

    public RuleConditionDTO(String id, String solutionRuleId, String questionId, List<Integer> scoreList, int operator, long created, long modified) {
        this.id = id;
        this.solutionRuleId = solutionRuleId;
        this.questionId = questionId;
        this.scoreList = scoreList;
        this.operator = operator;
        this.created = created;
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSolutionRuleId() {
        return solutionRuleId;
    }

    public void setSolutionRuleId(String solutionRuleId) {
        this.solutionRuleId = solutionRuleId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
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
}
