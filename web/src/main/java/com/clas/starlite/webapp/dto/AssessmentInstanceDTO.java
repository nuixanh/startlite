package com.clas.starlite.webapp.dto;

import java.util.List;

/**
 * Created by Admin on 1/20/2015.
 */
public class AssessmentInstanceDTO {
    private String username;
    private String customerName;
    private String customerEmail;
    private long timeStamp;
    private Scenario scenario;
    private List<Solution> solution;

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public List<Solution> getSolution() {
        return solution;
    }

    public void setSolution(List<Solution> solution) {
        this.solution = solution;
    }

    static class Solution{
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class Scenario{
        private String id;
        private List<Scenario> scenario;
        private List<Section> section;
        private double percent;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Scenario> getScenario() {
            return scenario;
        }

        public void setScenario(List<Scenario> scenario) {
            this.scenario = scenario;
        }

        public List<Section> getSection() {
            return section;
        }

        public void setSection(List<Section> section) {
            this.section = section;
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }
    }
    static class Section{
        private String id;
        private double percent;
        private List<Question> question;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }

        public List<AssessmentInstanceDTO.Question> getQuestion() {
            return question;
        }

        public void setQuestion(List<AssessmentInstanceDTO.Question> question) {
            this.question = question;
        }
    }
    static class Question{
        private String questionId;
        private double percent;
        private List<Answer> chosenAnswer;

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public List<Answer> getChosenAnswer() {
            return chosenAnswer;
        }

        public void setChosenAnswer(List<Answer> chosenAnswer) {
            this.chosenAnswer = chosenAnswer;
        }
    }
    static class Answer{
        private String id;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
