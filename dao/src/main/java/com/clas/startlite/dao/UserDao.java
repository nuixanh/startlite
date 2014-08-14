package com.clas.startlite.dao;

import com.clas.startlite.domain.User;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Son on 8/14/14.
 */
public class UserDao extends BaseDao<User, String>{
    public User findOneByEmailAndPassword(String email, String password){
        Criteria cr = where("email").is(email).and("password").is(password);
        Query query = new Query(cr);
        return template.findOne(query, User.class);
    }
    public User findOneByEmail(String email){
        Criteria cr = where("email").is(email);
        Query query = new Query(cr);
        return template.findOne(query, User.class);
    }
}
