package com.clas.starlite.webapp.dto;

import java.util.List;

/**
 * Created by Admin on 1/20/2015.
 */
public class AssessmentInstanceDTO {
    private String id;
    private String userId;
    private String username;
    private String customerName;
    private String customerEmail;
    private long timeStamp;
    private long countByUser;
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
    public static class RuleCondition{
        private String questionId;
        private List<List<String>> answerIds;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public List<List<String>> getAnswerIds() {
            return answerIds;
        }

        public void setAnswerIds(List<List<String>> answerIds) {
            this.answerIds = answerIds;
        }
    }
    public static class SolutionRule{
        private String id;
        private int type;
        private String solutionId;
        private List<List<RuleCondition>> ruleConditions;

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

        public List<List<RuleCondition>> getRuleConditions() {
            return ruleConditions;
        }

        public void setRuleConditions(List<List<RuleCondition>> ruleConditions) {
            this.ruleConditions = ruleConditions;
        }
    }
    public static class Solution{
        private String id;
        private String name;
        private String desc;
        private boolean isGroup;
        private List<Solution> solutions;
        private List<SolutionRule> rules;

        public List<SolutionRule> getRules() {
            return rules;
        }

        public void setRules(List<SolutionRule> rules) {
            this.rules = rules;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<Solution> getSolutions() {
            return solutions;
        }

        public void setSolutions(List<Solution> solutions) {
            this.solutions = solutions;
        }

        public boolean isGroup() {
            return isGroup;
        }

        public void setGroup(boolean isGroup) {
            this.isGroup = isGroup;
        }
    }

    public static class Scenario{
        private String id;
        private String name;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public static class Section{
        private String id;
        private String name;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public static class Question{
        private String questionId;
        private double percent;
        private String desc;
        private int type;
        private List<Answer> allAnswers;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<Answer> getAllAnswers() {
            return allAnswers;
        }

        public void setAllAnswers(List<Answer> allAnswers) {
            this.allAnswers = allAnswers;
        }
    }
    public static class Answer{
        private String id;
        private String content;
        private int score;

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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCountByUser() {
        return countByUser;
    }

    public void setCountByUser(long countByUser) {
        this.countByUser = countByUser;
    }
}
