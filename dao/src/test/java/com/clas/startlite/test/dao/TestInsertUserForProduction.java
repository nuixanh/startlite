package com.clas.startlite.test.dao;

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
public class TestInsertUserForProduction extends BaseDaoTest{
    @Test
    public void testInsert(){
        String userId = "d8be1fda-e01a-4c81-82c7-d8bde3c3f919";
        String sessionId = "49d0a953-c78e-43df-b3e6-7dd382cd729e";
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
        String userId = "a0a0164d-0d52-43b1-a9b5-cffd41d89587";
        String sessionId = "fceb809a-79e1-4870-b64c-013f03680339";
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
        String userId = "4d9d51aa-c3d8-47d1-b62d-5f4862c7a838";
        String sessionId = "646867be-ecd1-433d-a85a-2ee21b4d90be";
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
