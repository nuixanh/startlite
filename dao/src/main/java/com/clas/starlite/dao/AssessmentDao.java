package com.clas.starlite.dao;

import com.clas.starlite.domain.Assessment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by sonnt4 on 21/1/2015.
 */
public class AssessmentDao extends BaseDao<Assessment, String>{
    public List<Assessment> getByRevision(String userId, Long revisionByUser){
        Criteria cr = Criteria.where("userId").is(userId);
        if(revisionByUser != null && revisionByUser > 0){
            cr = cr.and("countByUser").gt(revisionByUser);
        }
        Query q = Query.query(cr);
        return template.find(q, Assessment.class);
    }
}
