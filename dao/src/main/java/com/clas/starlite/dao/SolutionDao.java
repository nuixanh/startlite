package com.clas.starlite.dao;

import com.clas.starlite.domain.Solution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Son on 8/14/14.
 */
public class SolutionDao extends BaseDao<Solution, String>{
    public List<Solution> getTree(String solutionId, Long revision){
        Criteria cr;
        if(StringUtils.isNotBlank(solutionId)){
            cr = Criteria.where("id").is(solutionId);
        }else{
            cr = Criteria.where("isGroup").is(Boolean.TRUE);
        }
        return template.find(Query.query(cr), Solution.class);
    }
}
