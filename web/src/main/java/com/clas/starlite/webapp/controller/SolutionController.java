package com.clas.starlite.webapp.controller;

import com.clas.starlite.common.Constants;
import com.clas.starlite.domain.Solution;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.dto.ScenarioDTO;
import com.clas.starlite.webapp.dto.SolutionDTO;
import com.clas.starlite.webapp.service.SolutionService;
import com.clas.starlite.webapp.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
@RestController
public class SolutionController {

    @RequestMapping(value = "/solutions", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO login(@RequestParam(value="revision", required=false) Long revision,
                               @RequestParam(value="id", required=false, defaultValue="") String solutionId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        List<SolutionDTO> solutionList = solutionService.getList(solutionId, revision);
        restResultDTO.setData(solutionList);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @RequestMapping(value = "/solution/create", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO create(@RequestBody Solution solution, @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        ErrorCodeMap errorCode = solutionService.validate(solution);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        SolutionDTO solutionDTO = solutionService.create(solution, userId);
        restResultDTO.setData(solutionDTO);
        restResultDTO.setSuccessful(true);
        return restResultDTO;
    }
    @Autowired
    SolutionService solutionService;
}
