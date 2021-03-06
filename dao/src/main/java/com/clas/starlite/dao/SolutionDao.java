package com.clas.starlite.dao;

import com.clas.starlite.common.Status;
import com.clas.starlite.domain.Solution;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Son on 8/14/14.
 */
public class SolutionDao extends BaseDao<Solution, String>{
    public List<Solution> getTrees(Long revision){
        Criteria cr = Criteria.where("parentId").exists(Boolean.FALSE);
        if(revision != null && revision > 0){
            cr = cr.and("revision").gt(revision);
        }
        Query q = Query.query(cr);
        return template.find(q, Solution.class);
    }

    public Solution getOneActiveByName(String name, Boolean isGroup){
        Criteria cr = Criteria.where("name").is(name).and("status").is(Status.ACTIVE.getValue());
        if(isGroup != null){
            cr = cr.and("isGroup").is(isGroup);
        }
        Query q = Query.query(cr);
        return template.findOne(q, Solution.class);
    }

    public List<Solution> getActiveByName(String name, Boolean isGroup){
        Criteria cr = Criteria.where("name").is(name).and("status").is(Status.ACTIVE.getValue());
        if(isGroup != null){
            cr = cr.and("isGroup").is(isGroup);
        }
        Query q = Query.query(cr);
        return template.find(q, Solution.class);
    }
    public List<Solution> getAllActive(Boolean isGroup){
        Criteria cr = Criteria.where("status").is(Status.ACTIVE.getValue());
        if(isGroup != null){
            cr = cr.and("isGroup").is(isGroup);
        }
        Query q = Query.query(cr);
        return template.find(q, Solution.class);
    }
}
