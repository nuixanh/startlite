package com.clas.starlite.webapp.controller;

import com.clas.starlite.common.Status;
import com.clas.starlite.domain.Question;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.QuestionDTO;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.service.QuestionService;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Son on 8/17/14.
 */
@RestController
public class QuestionController {

    @RequestMapping(value = "/question/create", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO create(@RequestBody Question question, @RequestHeader(value="user", required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        if(question == null || StringUtils.isBlank(question.getDesc()) || question.getAnswers() == null){
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_INVALID_PARAMS);
            return restResultDTO;
        }
        question.setCreatedBy(userId);
        question.setModifiedBy(userId);
        QuestionDTO questionDTO = questionService.create(question);
        restResultDTO.setData(questionDTO);
        restResultDTO.setSuccessful(true);
        return restResultDTO;
    }

    @RequestMapping(value = "/question/approve/{id}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO approve(@PathVariable("id") String questionId, @RequestHeader(value="user", required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        QuestionDTO questionDTO = questionService.updateStatus(questionId, userId, Status.ACTIVE.getValue());
        if(questionDTO != null){
            restResultDTO.setData(questionDTO);
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_OBJECT_NOT_FOUND);
        }

        return restResultDTO;
    }
    @RequestMapping(value = "/question/delete/{id}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO delete(@PathVariable("id") String questionId, @RequestHeader(value="user", required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        QuestionDTO questionDTO = questionService.updateStatus(questionId, userId, Status.DEACTIVE.getValue());
        if(questionDTO != null){
            restResultDTO.setData(questionDTO);
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_OBJECT_NOT_FOUND);
        }

        return restResultDTO;
    }

    @Autowired
    QuestionService questionService;
}
