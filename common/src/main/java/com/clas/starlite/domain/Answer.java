package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by sonnt4 on 8/15/2014.
 */
@Document(collection="answer")
public class Answer {
    private String id;
    private String desc;
    private long modified;
    private int score;

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
