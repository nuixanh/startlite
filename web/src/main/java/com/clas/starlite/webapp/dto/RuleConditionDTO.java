package com.clas.starlite.webapp.dto;

import java.util.List;

/**
 * Created by sonnt4 on 8/20/2014.
 */
public class RuleConditionDTO {
    private String questionId;
    private List<List<String>> answerIds;
    private List<List<String>> negativeAnswerIds;

    public RuleConditionDTO(String questionId, List<List<String>> answerIds, List<List<String>> negativeAnswerIds) {
        this.questionId = questionId;
        this.answerIds = answerIds;
        this.negativeAnswerIds = negativeAnswerIds;
    }

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
