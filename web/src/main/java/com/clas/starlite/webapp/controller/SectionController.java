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
import java.util.Map;

/**
 * Created by Son on 8/19/14.
 */
@RestController
public class SectionController extends ApplicationObjectSupport {

    @RequestMapping(value = "/sections", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO list(@RequestParam(value="revision", required=false) Long revision) {
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

    @RequestMapping(value = "/section/batch", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO batch(@RequestBody Section section, @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();

        Map<String, Object> output = sectionService.batchUpload(section, userId);
        ErrorCodeMap errorCode = (ErrorCodeMap) output.get(Constants.ERROR_CODE);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            Long errorLine = (Long) output.get(Constants.ERROR_LINE);
            restResultDTO.setData(String.valueOf(errorLine));
            return restResultDTO;
        }
        SectionDTO sectionDTO = (SectionDTO) output.get(Constants.DTO);
        restResultDTO.setData(sectionDTO);
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @RequestMapping(value = "/section/attach/{id}/{scenarioId}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO attach(@PathVariable("id") String sectionId,
                                @PathVariable("scenarioId") String scenarioId,
                                @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId,
                                @RequestParam(value="questions", required=false) String questionIDs) {
        RestResultDTO restResultDTO = new RestResultDTO();
        ErrorCodeMap errorCode = sectionService.attachToScenario(sectionId, scenarioId, userId, questionIDs);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @RequestMapping(value = "/section/detach/{id}/{scenarioId}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO detach(@PathVariable("id") String sectionId,
                                @PathVariable("scenarioId") String scenarioId,
                                @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        ErrorCodeMap errorCode = sectionService.detachToScenario(sectionId, scenarioId, userId);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        restResultDTO.setSuccessful(true);

        return restResultDTO;
    }

    @RequestMapping(value = "/section/delete/{id}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO delete(@PathVariable("id") String sectionId, @RequestHeader(value=Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        Map<String, Object> output = sectionService.delete(sectionId, userId);
        SectionDTO dto = (SectionDTO)output.get(Constants.DTO);
        if(dto != null){
            restResultDTO.setData(output);
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_OBJECT_NOT_FOUND);
        }

        return restResultDTO;
    }

    @RequestMapping(value = "/section/copy/{id}", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO copy(@PathVariable("id") String sectionId,
                              @RequestParam(value="name", required=false) String newName,
                              @RequestHeader(value=Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();
        Map<String, Object> output = sectionService.copy(sectionId, newName, userId);
        ErrorCodeMap errorCode = (ErrorCodeMap) output.get(Constants.ERROR_CODE);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
        }else{
            restResultDTO.setData(output);
            restResultDTO.setSuccessful(true);
        }

        return restResultDTO;
    }

    @Autowired
    SectionService sectionService;
}
