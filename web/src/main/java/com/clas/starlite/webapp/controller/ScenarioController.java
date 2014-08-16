package com.clas.starlite.webapp.controller;

import com.clas.starlite.domain.Scenario;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.dto.ScenarioDTO;
import com.clas.starlite.webapp.service.ScenarioService;
import com.clas.starlite.webapp.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Son on 8/15/14.
 */
@RestController
public class ScenarioController extends ApplicationObjectSupport {

    @RequestMapping(value = "/scenarios", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO login(@RequestParam(value="timestamp", required=false) Long timestamp,
                               @RequestParam(value="id", required=false, defaultValue="") String scenarioId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        List<ScenarioDTO> scenarioList = scenarioService.getList(scenarioId,timestamp);
        restResultDTO.setData(scenarioList);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }
    @RequestMapping(value = "/scenario/create", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO create(@RequestBody Scenario scenario, @RequestHeader(value="user", required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        scenario.setCreatedBy(userId);
        scenario.setModifiedBy(userId);
        ScenarioDTO scenarioDTO = scenarioService.create(scenario);
        restResultDTO.setData(scenarioDTO);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }
    @RequestMapping(value = "/scenario/update", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO update(@RequestBody Scenario scenario, @RequestHeader(value="user", required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        scenario.setModifiedBy(userId);
        ScenarioDTO scenarioDTO = scenarioService.update(scenario);
        if(scenarioDTO != null){
            restResultDTO.setData(scenarioDTO);
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_OBJECT_NOT_FOUND);
        }

        return restResultDTO;
    }

    @RequestMapping(value = "/scenario/delete/{id}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO delete(@PathVariable("id") String scenarioId, @RequestHeader(value="user", required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        ScenarioDTO scenarioDTO = scenarioService.delete(scenarioId, userId);
        if(scenarioDTO != null){
            restResultDTO.setData(scenarioDTO);
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_OBJECT_NOT_FOUND);
        }

        return restResultDTO;
    }

    @Autowired
    ScenarioService scenarioService;
}
