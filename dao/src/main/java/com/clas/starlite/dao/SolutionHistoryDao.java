package com.clas.starlite.dao;

import com.clas.starlite.domain.Solution;
import com.clas.starlite.domain.SolutionHistory;
import com.clas.starlite.domain.SolutionRule;
import com.clas.starlite.domain.SolutionRuleHistory;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonnt4 on 21/1/2015.
 */
public class SolutionHistoryDao extends BaseDao<SolutionHistory, String> {
    public SolutionHistory getOneByIdAndRevision(String id, long revision){
        Criteria cr = Criteria.where("id").is(id).and("revision").is(revision);
        Query q = Query.query(cr);
        return template.findOne(q, SolutionHistory.class);
    }
    public SolutionRuleHistory getOneRuleByIdAndRevision(String id, long revision){
        Criteria cr = Criteria.where("id").is(id).and("revision").is(revision);
        Query q = Query.query(cr);
        return template.findOne(q, SolutionRuleHistory.class);
    }
    public SolutionHistory snapshotSolution(Solution solution){
        SolutionHistory slHistory;
        try {
            slHistory = getOneByIdAndRevision(solution.getId(), solution.getRevision());
            if(slHistory == null){
                slHistory = new SolutionHistory();
                BeanUtilsBean.getInstance().copyProperties(slHistory, solution);
                List<Solution> solutions = solution.getSolutions();
                if(CollectionUtils.isNotEmpty(solutions)){
                    slHistory.setSolutionHistories(new ArrayList<SolutionHistory>());
                    for (Solution sl : solutions) {
                        SolutionHistory childHistory = snapshotSolution(sl);
                        slHistory.getSolutionHistories().add(childHistory);
                    }
                }
                slHistory.setSolutions(null);
                List<SolutionRule> rules = solution.getRules();
                if(CollectionUtils.isNotEmpty(rules)){
                    slHistory.setRuleHistories(new ArrayList<SolutionRuleHistory>());
                    for (SolutionRule rule : rules) {
                        SolutionRuleHistory ruleHistory = getOneRuleByIdAndRevision(rule.getId(), rule.getRevision());
                        if(ruleHistory == null){
                            ruleHistory = new SolutionRuleHistory();
                            BeanUtilsBean.getInstance().copyProperties(ruleHistory, rule);
                            template.save(ruleHistory);
                        }
                        slHistory.getRuleHistories().add(ruleHistory);
                    }
                }
                slHistory.setRules(null);
                template.save(slHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            slHistory = null;
        }
        return slHistory;
    }
}
