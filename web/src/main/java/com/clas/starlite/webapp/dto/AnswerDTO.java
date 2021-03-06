package com.clas.starlite.webapp.dto;

/**
 * Created by sonnt4 on 8/15/2014.
 */
public class AnswerDTO {
    private String id;
    private String desc;
    private long modified;
    private int score;

    public AnswerDTO(String id, String desc, long modified, int score) {
        this.id = id;
        this.desc = desc;
        this.modified = modified;
        this.score = score;
    }

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
