package com.clas.starlite.dao;

import com.clas.starlite.domain.Scenario;
import com.clas.starlite.domain.ScenarioHistory;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 1/21/2015.
 */
public class ScenarioHistoryDao extends BaseDao<ScenarioHistory, String>{
    public ScenarioHistory getOneByIdAndRevision(String id, long revision){
        Criteria cr = Criteria.where("id").is(id).and("revision").is(revision);
        Query q = Query.query(cr);
        return template.findOne(q, ScenarioHistory.class);
    }
    public ScenarioHistory snapshotScenario(Scenario scenario){
        ScenarioHistory sHistory;
        try {
            sHistory = getOneByIdAndRevision(scenario.getId(), scenario.getRevision());
            if(sHistory == null){
                sHistory = new ScenarioHistory();
                BeanUtilsBean.getInstance().copyProperties(sHistory, scenario);
                List<Scenario> scenarios = sHistory.getScenarios();
                if(scenarios != null && scenarios.size() > 0){
                    sHistory.setScenarioHistories(new ArrayList<ScenarioHistory>());
                    for (Scenario childSc : scenarios) {
                        ScenarioHistory childHistory = snapshotScenario(childSc);
                        sHistory.getScenarioHistories().add(childHistory);
                    }
                }
                sHistory.setScenarios(null);
                template.save(sHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sHistory = null;
        }
        return sHistory;
    }
}
