package com.clas.starlite.webapp.controller;

import com.clas.starlite.common.Constants;
import com.clas.starlite.domain.Section;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.dto.ScenarioDTO;
import com.clas.starlite.webapp.dto.SectionDTO;
import com.clas.starlite.webapp.service.SectionService;
import com.clas.starlite.webapp.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Son on 8/19/14.
 */
@RestController
public class SectionController extends ApplicationObjectSupport {

    @RequestMapping(value = "/sections", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO login(@RequestParam(value="revision", required=false) Long revision) {
        RestResultDTO restResultDTO = new RestResultDTO();
        List<SectionDTO> sectionDTOs = sectionService.getList(revision);
        restResultDTO.setData(sectionDTOs);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @RequestMapping(value = "/section/create", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO create(@RequestBody Section section, @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        ErrorCodeMap errorCode = sectionService.validate(section);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        SectionDTO sectionDTO = sectionService.create(section, userId);
        restResultDTO.setData(sectionDTO);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }
    @RequestMapping(value = "/section/update", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO update(@RequestBody Section section, @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        ErrorCodeMap errorCode = sectionService.validate(section);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        SectionDTO sectionDTO = sectionService.update(section, userId);
        restResultDTO.setData(sectionDTO);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @RequestMapping(value = "/section/attach/{id}/{scenarioId}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO create(@PathVariable("id") String sectionId,
                                @PathVariable("scenarioId") String scenarioId,
                                @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        ErrorCodeMap errorCode = sectionService.attachToScenario(sectionId, scenarioId, userId);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @RequestMapping(value = "/scenario/delete/{id}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO delete(@PathVariable("id") String sectionId, @RequestHeader(value=Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        SectionDTO dto = sectionService.delete(sectionId, userId);
        if(dto != null){
            restResultDTO.setData(dto);
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_OBJECT_NOT_FOUND);
        }

        return restResultDTO;
    }

    @Autowired
    SectionService sectionService;
}
