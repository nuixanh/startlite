package com.clas.starlite.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

/**
 * Created by Admin on 1/21/2015.
 */
@Document(collection="section_history")
public class SectionHistory extends Section{

    @DBRef
    private List<ScenarioHistory> scenarioHistories;
    @DBRef
    private List<QuestionHistory> questionHistories;

    @Id
    private String historyId;
    private long historyCreated;


    public SectionHistory() {
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

    public List<QuestionHistory> getQuestionHistories() {
        return questionHistories;
    }

    public void setQuestionHistories(List<QuestionHistory> questionHistories) {
        this.questionHistories = questionHistories;
    }
}
