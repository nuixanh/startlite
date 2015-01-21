package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

/**
 * Created by Admin on 1/21/2015.
 */
@Document(collection="assessment")
public class Assessment {

    private String id;
    private String userId;
    private String customerName;
    private String customerEmail;
    private long scoreDate;
    private long created;
    private long modified;
    @DBRef
    List<SolutionHistory> solutionHistories;
    @DBRef
    List<ScenarioHistory> scenarioHistories;

    public Assessment() {
        id = UUID.randomUUID().toString();
        created = System.currentTimeMillis();
        modified = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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

    public List<SolutionHistory> getSolutionHistories() {
        return solutionHistories;
    }

    public void setSolutionHistories(List<SolutionHistory> solutionHistories) {
        this.solutionHistories = solutionHistories;
    }

    public List<ScenarioHistory> getScenarioHistories() {
        return scenarioHistories;
    }

    public void setScenarioHistories(List<ScenarioHistory> scenarioHistories) {
        this.scenarioHistories = scenarioHistories;
    }

    public long getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(long scoreDate) {
        this.scoreDate = scoreDate;
    }
}
