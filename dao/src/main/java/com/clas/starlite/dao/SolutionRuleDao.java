package com.clas.starlite.dao;

import com.clas.starlite.common.Status;
import com.clas.starlite.domain.SolutionRule;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
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
        Criteria cr = Criteria.where("status").is(Status.ACTIVE.getValue()).and("conditions.questionId").in(qIDs);

        Query q = Query.query(cr);
        return template.find(q, SolutionRule.class);
    }
}
