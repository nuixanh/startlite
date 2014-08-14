package com.clas.startlite.webapp.controller;

import com.clas.startlite.common.Constants;
import com.clas.startlite.dao.SessionDao;
import com.clas.startlite.domain.Session;
import com.clas.startlite.webapp.common.ErrorCodeMap;
import com.clas.startlite.webapp.dto.RestResultDTO;
import com.clas.startlite.webapp.dto.UserLoginDTO;
import com.clas.startlite.dao.UserDao;
import com.clas.startlite.domain.User;
import com.clas.startlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
            User user = userDao.findOneByEmailAndPassword(email, password);
            if(user != null){
                Session session = new Session(UUID.randomUUID().toString(), user.getId(), System.currentTimeMillis(),
                        Constants.SESSION_WITHOUT_EXPIRATION);
                sessionDao.save(session);
                UserLoginDTO userDto = new UserLoginDTO();
                userDto.setUserId(user.getId());
                userDto.setSessionId(session.getId());
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
    private UserDao userDao;
    @Autowired
    private SessionDao sessionDao;
}
