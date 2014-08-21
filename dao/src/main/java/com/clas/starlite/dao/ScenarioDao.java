package com.clas.starlite.dao;

import com.clas.starlite.domain.Scenario;
import com.clas.starlite.domain.Solution;
import com.clas.starlite.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Son on 8/14/14.
 */
public class ScenarioDao extends BaseDao<Scenario, String>{
    public List<Scenario> getTrees(Long revision){
        Criteria cr = Criteria.where("parentId").exists(Boolean.FALSE);
        if(revision != null && revision > 0){
            cr = cr.and("revision").gt(revision);
        }
        Query q = Query.query(cr);
        return template.find(q, Scenario.class);
    }
    /*public List<Scenario> getTree(Collection<String> scIds){
        if(CollectionUtils.isEmpty(scIds)){
            return new ArrayList<Scenario>();
        }
        Criteria cr = Criteria.where("id").in(scIds);
        Query q = Query.query(cr);
        return template.find(q, Scenario.class);
    }
    public List<Scenario> getAllTrees(){
        Criteria cr = Criteria.where("parentId").exists(Boolean.FALSE);
        return template.find(Query.query(cr), Scenario.class);
    }*/
    public Set<String> getRootScenarioIdSet(Collection<String> scIds){
        Set<String> rs = new HashSet<String>();
        Criteria cr = Criteria.where("id").in(scIds);
        Query q = Query.query(cr);
        q.fields().include("rootParentId");
        List<Scenario> scenarios = template.find(q, Scenario.class);
        for (Scenario scenario : scenarios) {
            if(StringUtils.isNotBlank(scenario.getRootParentId())){
                rs.add(scenario.getRootParentId());
            }else{
                rs.add(scenario.getId());
            }
        }
        return rs;
    }
}
