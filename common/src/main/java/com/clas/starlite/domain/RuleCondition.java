package com.clas.starlite.domain;

import java.util.List;

/**
 * Created by Son on 8/17/14.
 */

public class RuleCondition {
    private String questionId;
    private List<List<String>> answerIds;
    private List<List<String>> negativeAnswerIds;

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

    public List<List<String>> getNegativeAnswerIds() {
        return negativeAnswerIds;
    }

    public void setNegativeAnswerIds(List<List<String>> negativeAnswerIds) {
        this.negativeAnswerIds = negativeAnswerIds;
    }
}
