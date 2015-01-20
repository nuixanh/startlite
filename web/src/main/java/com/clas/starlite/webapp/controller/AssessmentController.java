package com.clas.starlite.webapp.controller;

import com.clas.starlite.common.Constants;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.AssessmentInstanceDTO;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sonnt4 on 20/1/2015.
 */
@RestController
public class AssessmentController extends ApplicationObjectSupport {
    @RequestMapping(value = "/assessment/score", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO create(@RequestBody AssessmentInstanceDTO assessmentDto, @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        System.out.println(assessmentDto.toString());

        /*ErrorCodeMap errorCode = questionService.validate(question);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        QuestionDTO questionDTO = questionService.create(question, userId);
        restResultDTO.setData(questionDTO);
        restResultDTO.setSuccessful(true);*/
        restResultDTO.setSuccessful(true);
        return restResultDTO;
    }
}
