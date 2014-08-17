package com.clas.starlite.dao;

import com.clas.starlite.domain.Scenario;
import com.clas.starlite.domain.Solution;
import com.clas.starlite.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Son on 8/14/14.
 */
public class ScenarioDao extends BaseDao<Scenario, String>{
    public List<Scenario> getTree(String scenarioId, Long revision){
        Criteria cr;
        if(StringUtils.isNotBlank(scenarioId)){
            cr = Criteria.where("id").is(scenarioId);
        }else{
            cr = Criteria.where("parentId").exists(Boolean.FALSE);
        }
        return template.find(Query.query(cr), Scenario.class);
    }
}
