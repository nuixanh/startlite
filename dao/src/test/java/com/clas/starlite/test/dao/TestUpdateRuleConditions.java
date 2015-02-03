package com.clas.starlite.test.dao;

import com.clas.starlite.dao.SolutionRuleDao;
import com.clas.starlite.domain.SolutionRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Son on 8/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUpdateRuleConditions extends BaseDaoTest{
    @Test
    public void testUpdateStatus(){
        List<SolutionRule> rules = solutionRuleDao.findAll();
        System.out.println("------- number of rules: " + rules.size());
        for (SolutionRule rule : rules) {

//            solutionRuleDao.save(rule);
        }
    }

    @Test
    public void testGetRulesByQuestionIds(){
        Set<String> qIDs = new HashSet<String>();
        qIDs.add("b8d3b650-6373-4372-ab53-555f2660d9a2");
        List<SolutionRule> rules = solutionRuleDao.getRulesByQuestionIds(qIDs);
        System.out.println("------- number of rules: " + rules.size());
        for (SolutionRule rule : rules) {

//            solutionRuleDao.save(rule);
        }
    }

    @Autowired
    private SolutionRuleDao solutionRuleDao;
}
