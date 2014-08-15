package com.clas.starlite.webapp.controller;

import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.dto.UserLoginDTO;
import com.clas.starlite.webapp.service.LoginService;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Son on 8/6/14.
 */
@RestController
public class LoginController extends ApplicationObjectSupport {

    @RequestMapping(value = "/login", method= RequestMethod.GET, produces={"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public RestResultDTO login(@RequestParam(value="email", required=true, defaultValue="") String email,
                              @RequestParam(value="password", required=true, defaultValue="") String password) {
        RestResultDTO restResultDTO = new RestResultDTO();

        if(StringUtils.isNotBlank(email) && StringUtils.isNoneBlank(password)){
            UserLoginDTO userDto = loginService.login(email, password);
            if(userDto != null){
                restResultDTO.setData(userDto);
                restResultDTO.setSuccessful(true);
            }else{
                restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_LOGIN_FAIL);
            }
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_INVALID_PARAMS);
        }

        return restResultDTO;
    }

    @Autowired
    private LoginService loginService;
}
