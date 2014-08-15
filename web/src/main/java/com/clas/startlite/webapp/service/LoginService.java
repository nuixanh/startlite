package com.clas.startlite.webapp.service;

import com.clas.startlite.common.Constants;
import com.clas.startlite.dao.SessionDao;
import com.clas.startlite.dao.UserDao;
import com.clas.startlite.domain.Session;
import com.clas.startlite.domain.User;
import com.clas.startlite.webapp.common.ErrorCodeMap;
import com.clas.startlite.webapp.dto.UserLoginDTO;
import com.clas.startlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Son on 8/15/14.
 */
@Component
public class LoginService {
    public UserLoginDTO login(String email, String password){
        UserLoginDTO output = null;
        if(StringUtils.isNotBlank(email) && StringUtils.isNoneBlank(password)){
            User user = userDao.findOneByEmailAndPassword(email, password);
            if(user != null){
                Session session = new Session(UUID.randomUUID().toString(), user.getId(), System.currentTimeMillis(),
                        Constants.SESSION_WITHOUT_EXPIRATION);
                sessionDao.save(session);
                output = new UserLoginDTO();
                output.setUserId(user.getId());
                output.setSessionId(session.getId());
            }
        }
        return output;
    }
    @Autowired
    private UserDao userDao;
    @Autowired
    private SessionDao sessionDao;
}
