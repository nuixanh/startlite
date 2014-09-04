package com.clas.starlite.webapp.converter;

import com.clas.starlite.common.Status;
import com.clas.starlite.domain.Answer;
import com.clas.starlite.domain.Question;
import com.clas.starlite.webapp.dto.AnswerDTO;
import com.clas.starlite.webapp.dto.QuestionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
public class QuestionConverter {
    public static QuestionDTO convert(Question q){
        QuestionDTO dto = new QuestionDTO(q.getId(), q.getDesc(), q.getCreated(), q.getModified(), q.getRevision(), q.getSectionId(), q.getStatus(), q.getType());
        dto.setAnswers(convert(q.getAnswers()));
        return dto;
    }
    public static AnswerDTO convert(Answer answer){
        AnswerDTO dto = new AnswerDTO(answer.getId(), answer.getDesc(), answer.getModified(), answer.getScore());
        return dto;
    }
    public static List<AnswerDTO> convert(List<Answer> answers){
        List<AnswerDTO> dtos = null;
        if(answers != null){
            dtos = new ArrayList<AnswerDTO>();
            for (Answer answer : answers) {
                if(answer.getStatus() == Status.ACTIVE.getValue()){
                    AnswerDTO dto = convert(answer);
                    dtos.add(dto);
                }
            }
        }
        return dtos;
    }
}
