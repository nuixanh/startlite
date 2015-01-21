package com.clas.starlite.dao;

import com.clas.starlite.domain.Solution;
import com.clas.starlite.domain.SolutionHistory;
import org.apache.commons.beanutils.BeanUtilsBean;
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
    public SolutionHistory snapshotSolution(Solution solution){
        SolutionHistory slHistory;
        try {
            slHistory = getOneByIdAndRevision(solution.getId(), solution.getRevision());
            if(slHistory == null){
                slHistory = new SolutionHistory();
                BeanUtilsBean.getInstance().copyProperties(slHistory, solution);
                List<Solution> solutions = solution.getSolutions();
                if(solutions != null && solutions.size() > 0){
                    slHistory.setSolutionHistories(new ArrayList<SolutionHistory>());
                    for (Solution sl : solutions) {
                        SolutionHistory childHistory = snapshotSolution(sl);
                        slHistory.getSolutionHistories().add(childHistory);
                    }
                }
                slHistory.setSolutions(null);
                template.save(slHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            slHistory = null;
        }
        return slHistory;
    }
}
