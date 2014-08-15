package com.clas.startlite.test.dao;

import com.clas.starlite.dao.UserDao;
import com.clas.starlite.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by Son on 8/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestInsertUser extends BaseDaoTest{
    @Test
    public void testInsert(){
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("son@live.com");
        user.setPassword("test");
        userDao.save(user);
    }
    @Autowired
    private UserDao userDao;
}
