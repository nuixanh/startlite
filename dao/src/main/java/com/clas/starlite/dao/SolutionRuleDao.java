package com.clas.starlite.dao;

import com.clas.starlite.common.Status;
import com.clas.starlite.domain.SolutionRule;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Son on 8/14/14.
 */
public class SolutionRuleDao extends BaseDao<SolutionRule, String>{

    public List<SolutionRule> getRulesByQuestionIds(Collection<String> qIDs){
        if(qIDs == null || qIDs.size() == 0){
            return new ArrayList<SolutionRule>();
        }
        DBObject subDbObj = new BasicDBObject();
        subDbObj.put("$elemMatch", Criteria.where("questionId").in(qIDs).getCriteriaObject());
        DBObject dbObj = new BasicDBObject();
        dbObj.put("$elemMatch", subDbObj);

        Criteria cr = Criteria.where("status").is(Status.ACTIVE.getValue()).and("conditions").all(dbObj);

        Query q = Query.query(cr);
        System.out.println(q.getQueryObject().toString());
        return template.find(q, SolutionRule.class);
    }
}
