package com.clas.starlite.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Created by Admin on 1/23/2015.
 */
@Document(collection="solution_rule_history")
public class SolutionRuleHistory extends SolutionRule {

    @Id
    private String historyId;
    private long historyCreated;

    public SolutionRuleHistory() {
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
}
