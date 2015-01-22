package com.clas.starlite.dao;

import com.clas.starlite.domain.Revision;
import com.clas.starlite.domain.RevisionHistory;
import com.clas.starlite.domain.User;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Calendar;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
    public User incSurveyCount(User user){
        Update update = new Update();
        update.inc("surveyCount", 1);
        //return the modified object rather than the original
        User newUser = template.findAndModify(query(where("_id").is(user.getId())),
                update, FindAndModifyOptions.options().returnNew(true).upsert(true), User.class);
        return newUser;
    }
}
