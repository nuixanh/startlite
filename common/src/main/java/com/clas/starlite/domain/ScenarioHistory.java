package com.clas.starlite.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Admin on 1/21/2015.
 */
@Document(collection="scenario_history")
public class ScenarioHistory extends Scenario {
    @DBRef
    private List<ScenarioHistory> scenarioHistories;

    private Set<String> sectionHistories;

    @Id
    private String historyId;
    private long historyCreated;

    public ScenarioHistory() {
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

    public List<ScenarioHistory> getScenarioHistories() {
        return scenarioHistories;
    }

    public void setScenarioHistories(List<ScenarioHistory> scenarioHistories) {
        this.scenarioHistories = scenarioHistories;
    }

    public Set<String> getSectionHistories() {
        return sectionHistories;
    }

    public void setSectionHistories(Set<String> sectionHistories) {
        this.sectionHistories = sectionHistories;
    }
}
