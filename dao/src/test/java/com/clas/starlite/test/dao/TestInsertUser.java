package com.clas.starlite.test.dao;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.UserRole;
import com.clas.starlite.dao.SessionDao;
import com.clas.starlite.dao.UserDao;
import com.clas.starlite.domain.Session;
import com.clas.starlite.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Son on 8/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestInsertUser extends BaseDaoTest{
    @Test
    public void testInsert(){
        String userId = "751e988f-2118-48ae-9ee4-14efda7a9c59";
        String sessionId = "5e356083-ae1b-4c42-8039-7627827ca2f1";
        User user = new User();
        user.setId(userId);
        user.setEmail("son@live.com");
        user.setPassword("test");
        user.setRole(UserRole.ROLE_REGULAR.getValue());
        userDao.save(user);

        Session session = new Session(sessionId, userId, System.currentTimeMillis(), Constants.SESSION_WITHOUT_EXPIRATION);
        sessionDao.save(session);
    }
    @Test
    public void testInsertQuestionContributor(){
        String userId = "68b25d39-d90d-47cb-8132-4447947437d2";
        String sessionId = "d6608e39-22c3-4714-81ac-b36345ad4b20";
        User user = new User();
        user.setId(userId);
        user.setEmail("contributor@live.com");
        user.setPassword("test");
        user.setRole(UserRole.ROLE_CONTRIBUTOR.getValue());
        userDao.save(user);
        Session session = new Session(sessionId, userId, System.currentTimeMillis(), Constants.SESSION_WITHOUT_EXPIRATION);
        sessionDao.save(session);
    }
    @Test
    public void testInsertScenarioCreator(){
        String userId = "000b6350-23b9-4989-9754-fcaa14d20b68";
        String sessionId = "96f6b9ab-ce18-4505-84a0-130f569680e6";
        User user = new User();
        user.setId(userId);
        user.setEmail("scenario@live.com");
        user.setPassword("test");
        user.setRole(UserRole.ROLE_SCENARIO_CREATOR.getValue());
        userDao.save(user);
        Session session = new Session(sessionId, userId, System.currentTimeMillis(), Constants.SESSION_WITHOUT_EXPIRATION);
        sessionDao.save(session);
    }
    @Autowired
    private UserDao userDao;
    @Autowired
    private SessionDao sessionDao;
}
