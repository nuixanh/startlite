package com.clas.starlite.webapp.controller;

import com.clas.starlite.common.Constants;
import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.UserGroup;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.QuestionDTO;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.service.GroupService;
import com.clas.starlite.webapp.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Admin on 2/4/2015.
 */
@RestController
public class GroupController {
    @RequestMapping(value = "/group/create", method= RequestMethod.POST, consumes="application/json", produces={"application/json"})
    public RestResultDTO update(@RequestBody UserGroup group, @RequestHeader(value= Constants.HTTP_HEADER_USER, required = true) String userId) {
        RestResultDTO restResultDTO = new RestResultDTO();

        return restResultDTO;
    }
    @Autowired
    private GroupService groupService;
}
