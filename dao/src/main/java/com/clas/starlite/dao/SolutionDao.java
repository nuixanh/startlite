package com.clas.starlite.dao;

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
}
