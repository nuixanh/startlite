package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private long countByUser;
    @DBRef
    private List<SolutionHistory> solutionHistories;
    @DBRef
    private ScenarioHistory rootScenarioHistory;
    private Score score;
//    List<Score> scores = new ArrayList<Score>();

    public Assessment() {
        id = UUID.randomUUID().toString();
        created = System.currentTimeMillis();
        modified = created;
    }
    public static class Score {
        private String entityId;
        private String type;
        private double scorePercent;
        private List<String> answerIDs;
        private List<Score> children = new ArrayList<Score>();

        public Score(String entityId, String type, double scorePercent) {
            this.entityId = entityId;
            this.type = type;
            this.scorePercent = scorePercent;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getScorePercent() {
            return scorePercent;
        }

        public void setScorePercent(double scorePercent) {
            this.scorePercent = scorePercent;
        }

        public List<Score> getChildren() {
            return children;
        }

        public void setChildren(List<Score> children) {
            this.children = children;
        }

        public List<String> getAnswerIDs() {
            return answerIDs;
        }

        public void setAnswerIDs(List<String> answerIDs) {
            this.answerIDs = answerIDs;
        }
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

    public ScenarioHistory getRootScenarioHistory() {
        return rootScenarioHistory;
    }

    public void setRootScenarioHistory(ScenarioHistory rootScenarioHistory) {
        this.rootScenarioHistory = rootScenarioHistory;
    }

    public long getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(long scoreDate) {
        this.scoreDate = scoreDate;
    }

    /*public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }*/

    public long getCountByUser() {
        return countByUser;
    }

    public void setCountByUser(long countByUser) {
        this.countByUser = countByUser;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
