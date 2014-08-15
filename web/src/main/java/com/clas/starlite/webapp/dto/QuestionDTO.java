package com.clas.starlite.webapp.dto;

import java.util.List;

/**
 * Created by sonnt4 on 8/15/2014.
 */
public class QuestionDTO {
    private String id;
    private String desc;
    private long modified;
    private List<AnswerDTO> answers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}
