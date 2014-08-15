package com.clas.starlite.dao;

import com.clas.starlite.domain.Scenario;
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
    public List<Scenario> findSelfAndChildren(String scenarioId, Boolean status, Long timestamp){
        if(StringUtils.isBlank(scenarioId) && status == null && timestamp == null){
            return new ArrayList<Scenario>();
        }
        Criteria cr = new Criteria();
        if(StringUtils.isNotBlank(scenarioId)){
            Criteria cr1 = where("_id").is(scenarioId);
            Criteria cr2 = where("parentId").is(scenarioId);
            cr = cr.orOperator(cr1, cr2);
        }
        if(status != null){
            cr = cr.and("status").is(status);
        }
        if(timestamp != null){
            cr = cr.and("modified").gt(timestamp);
        }
        Query query = new Query(cr);
        return template.find(query, Scenario.class);
    }
}
