package com.clas.starlite.webapp.controller;

import com.clas.starlite.common.Constants;
import com.clas.starlite.domain.Assessment;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.AssessmentInstanceDTO;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.service.AssessmentService;
import com.clas.starlite.webapp.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by sonnt4 on 20/1/2015.
 */
@RestController
public class AssessmentController extends ApplicationObjectSupport {
    @RequestMapping(value = "/assessment/score", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO score(@RequestBody AssessmentInstanceDTO assessmentDto, @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        assessmentDto.setUserId(userId);
        Map<String, Object> output = assessmentService.score(assessmentDto);
        ErrorCodeMap errorCode = (ErrorCodeMap) output.get(Constants.ERROR_CODE);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        Assessment assessment = (Assessment) output.get(Constants.DATA);
        restResultDTO.setData(assessment.getId());
        restResultDTO.setSuccessful(true);
        return restResultDTO;
    }
    @RequestMapping(value = "/assessments", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO list(@RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId, @RequestParam(value="revision", required=false) Long revision) {
        RestResultDTO restResultDTO = new RestResultDTO();
        List<AssessmentInstanceDTO> dtos = assessmentService.getReport(userId, revision);
        restResultDTO.setData(dtos);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @Autowired
    AssessmentService assessmentService;
}
