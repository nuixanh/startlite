package com.clas.starlite.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

/**
 * Created by sonnt4 on 21/1/2015.
 */
@Document(collection="solution_history")
public class SolutionHistory extends Solution{
    @DBRef
    private List<SolutionHistory> solutionHistories;
    @DBRef
    private List<SolutionRuleHistory> ruleHistories;

    @Id
    private String historyId;
    private long historyCreated;


    public SolutionHistory() {
        historyId = UUID.randomUUID().toString();
        historyCreated = System.currentTimeMillis();
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public long getHistoryCreated() {
        return historyCreated;
    }

    public void setHistoryCreated(long historyCreated) {
        this.historyCreated = historyCreated;
    }

    public List<SolutionHistory> getSolutionHistories() {
        return solutionHistories;
    }

    public void setSolutionHistories(List<SolutionHistory> solutionHistories) {
        this.solutionHistories = solutionHistories;
    }

    public List<SolutionRuleHistory> getRuleHistories() {
        return ruleHistories;
    }

    public void setRuleHistories(List<SolutionRuleHistory> ruleHistories) {
        this.ruleHistories = ruleHistories;
    }
}
