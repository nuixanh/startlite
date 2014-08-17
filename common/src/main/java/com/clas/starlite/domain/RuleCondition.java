package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
@Document(collection="rule_condition")
public class RuleCondition {
    private String id;
    private String solutionRuleId;
    private String questionId;
    private List<Integer> scoreList;
    private int operator;
    private long created;
    private long modified;

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
