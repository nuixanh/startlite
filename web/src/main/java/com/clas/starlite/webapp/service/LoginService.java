package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.UserRole;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.SessionDao;
import com.clas.starlite.dao.UserDao;
import com.clas.starlite.domain.*;
import com.clas.starlite.util.CommonUtils;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.UserLoginDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Son on 8/15/14.
 */
@Component
public class LoginService {
    public ErrorCodeMap validateForSignup(String email, String password){
        if(StringUtils.isBlank(email) || StringUtils.isBlank(password)){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else if(!CommonUtils.isValidEmail(email)){
            return ErrorCodeMap.FAILURE_INVALID_EMAIL;
        }else{
            User user = userDao.findOneByEmail(email);
            if(user != null){
                return ErrorCodeMap.FAILURE_EMAIL_EXISTED;
            }
        }
        return null;
    }
    public void signup(String email, String password, String name, String firstName, String lastName, String locale, String link){
        User user = new User(UUID.randomUUID().toString(), password, name, firstName, lastName, email, UserRole.ROLE_REGULAR.getValue(),
                System.currentTimeMillis(), null, System.currentTimeMillis(), link, locale);
        userDao.save(user);
    }
    public ErrorCodeMap setRole(String email, int role){
        User user = userDao.findOneByEmail(email);
        if(user == null){
            return ErrorCodeMap.FAILURE_USER_NOT_FOUND;
        }
        user.setRole(role);
        userDao.save(user);
        return null;
    }
    public UserLoginDTO login(String email, String password){
        UserLoginDTO output = null;
        if(StringUtils.isNotBlank(email) && StringUtils.isNotBlank(password)){
            User user = userDao.findOneByEmailAndPassword(email, password);
            if(user != null){
                Session session = new Session(UUID.randomUUID().toString(), user.getId(), System.currentTimeMillis(),
                        Constants.SESSION_WITHOUT_EXPIRATION);
                sessionDao.save(session);
                output = new UserLoginDTO();
                output.setUserId(user.getId());
                output.setSessionId(session.getId());
                output.setRole(user.getRole());
                output.setSurveyCount(user.getSurveyCount());
            }
        }
        return output;
    }
    public Map<String, Long> getCurrentRevision(){
        Map<String, Long> revisionMap = new HashMap<String, Long>();
        revisionMap.put(Constants.REVISION_TYPE_QUESTION, 0L);
        revisionMap.put(Constants.REVISION_TYPE_SCENARIO, 0L);
        revisionMap.put(Constants.REVISION_TYPE_SOLUTION, 0L);
        List<Revision> revisions = revisionDao.findAll();
        for (Revision revision : revisions) {
            if(revisionMap.keySet().contains(revision.getType())){
                revisionMap.put(revision.getType(), revision.getVersion());
            }
        }

        return revisionMap;
    }
    @Autowired
    private UserDao userDao;
    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private RevisionDao revisionDao;

}
