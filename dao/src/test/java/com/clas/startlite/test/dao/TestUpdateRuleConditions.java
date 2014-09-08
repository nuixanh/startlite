package com.clas.startlite.test.dao;

import com.clas.starlite.dao.SolutionRuleDao;
import com.clas.starlite.domain.RuleCondition;
import com.clas.starlite.domain.SolutionRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private SolutionRuleDao solutionRuleDao;
}
