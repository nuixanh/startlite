package com.clas.starlite.webapp.controller;

import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.dto.ScenarioDTO;
import com.clas.starlite.webapp.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Son on 8/15/14.
 */
@RestController
public class ScenarioController extends ApplicationObjectSupport {

    @RequestMapping(value = "/scenarios", method= RequestMethod.GET, produces={"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public RestResultDTO login(@RequestParam(value="timestamp", required=false) Long timestamp,
                               @RequestParam(value="id", required=false, defaultValue="") String scenarioId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        List<ScenarioDTO> scenarioList = scenarioService.getList(scenarioId,timestamp);
        restResultDTO.setData(scenarioList);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @Autowired
    ScenarioService scenarioService;
}
