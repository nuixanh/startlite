package com.clas.starlite.webapp.controller;

import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.dto.UserLoginDTO;
import com.clas.starlite.webapp.service.LoginService;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Son on 8/6/14.
 */
@RestController
public class LoginController extends ApplicationObjectSupport {

    @RequestMapping(value = "/login", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO login(@RequestParam(value="email", required=true, defaultValue="") String email,
                              @RequestParam(value="password", required=true, defaultValue="") String password) {
        RestResultDTO restResultDTO = new RestResultDTO();

        if(StringUtils.isNotBlank(email) && StringUtils.isNotBlank(password)){
            UserLoginDTO userDto = loginService.login(email, password);
            if(userDto != null){
                Map<String, Long> revisionMap = loginService.getCurrentRevision();
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("revision", revisionMap);
                data.put("user", userDto);
                restResultDTO.setData(data);
                restResultDTO.setSuccessful(true);
            }else{
                restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_LOGIN_FAIL);
            }
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_INVALID_PARAMS);
        }

        return restResultDTO;
    }

    @RequestMapping(value = "/setRole", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO setRole(@RequestParam(value="email", required=true, defaultValue="") String email,
                               @RequestParam(value="role", required=true, defaultValue="0") int role) {
        RestResultDTO restResultDTO = new RestResultDTO();

        if(StringUtils.isNotBlank(email)){
            ErrorCodeMap errorCode = loginService.setRole(email, role);
            if(errorCode != null){
                restResultDTO = RestUtils.createInvalidOutput(errorCode);
                return restResultDTO;
            }
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_INVALID_PARAMS);
        }

        return restResultDTO;
    }

    @RequestMapping(value = "/signup", method= RequestMethod.GET, produces={"application/json"})
    public RestResultDTO signup(@RequestParam(value="email", required=true, defaultValue="") String email,
                               @RequestParam(value="id", required=true, defaultValue="") String password,
                               @RequestParam(value="name", defaultValue="") String name,
                               @RequestParam(value="firstName", defaultValue="") String firstName,
                               @RequestParam(value="lastName", defaultValue="") String lastName,
                               @RequestParam(value="link", defaultValue="") String link,
                               @RequestParam(value="locale", defaultValue="") String locale) {
        RestResultDTO restResultDTO = new RestResultDTO();
        email = email.trim();
        ErrorCodeMap errorCode = loginService.validateForSignup(email, password);
        if(errorCode != null){
            restResultDTO = RestUtils.createInvalidOutput(errorCode);
            return restResultDTO;
        }
        loginService.signup(email, password, name, firstName, lastName, link, locale);
        UserLoginDTO userDto = loginService.login(email, password);
        if(userDto != null){
            Map<String, Long> revisionMap = loginService.getCurrentRevision();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("revision", revisionMap);
            data.put("user", userDto);
            restResultDTO.setData(data);
            restResultDTO.setSuccessful(true);
        }else{
            restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_LOGIN_FAIL);
        }

        return restResultDTO;
    }

    @Autowired
    private LoginService loginService;
}
