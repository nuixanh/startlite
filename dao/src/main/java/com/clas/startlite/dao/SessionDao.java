package com.clas.startlite.dao;

import com.clas.startlite.common.Constants;
import com.clas.startlite.domain.Session;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Son on 8/14/14.
 */
public class SessionDao extends BaseDao<Session, String>{
    public Session validate(String id, String userId, long time){
        Criteria cr = where("id").is(id).and("userId").is(userId);
        Query query = new Query(cr);
        Session session = template.findOne(query, Session.class);
        if(session != null && session.getExpired() != Constants.SESSION_WITHOUT_EXPIRATION){
            if(session.getExpired() < time){
                session = null;
            }
        }
        return session;
    }
}
