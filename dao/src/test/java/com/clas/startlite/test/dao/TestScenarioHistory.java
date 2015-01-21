package com.clas.startlite.test.dao;

import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.dao.ScenarioHistoryDao;
import com.clas.starlite.domain.Scenario;
import com.clas.starlite.domain.ScenarioHistory;
import com.clas.starlite.util.CommonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Admin on 1/21/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestScenarioHistory extends BaseDaoTest{
    @Test
    public void testSnapshotScenario() throws Exception{
        Scenario scenario = scenarioDao.findOne("c5f53b1f-acf5-4c25-a465-24783aec6d3b");
//        System.out.println(CommonUtils.printPrettyObj(scenario));
        ScenarioHistory sHistory = scenarioHistoryDao.snapshotScenario(scenario);
        System.out.println(CommonUtils.printPrettyObj(sHistory));
    }
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private ScenarioHistoryDao scenarioHistoryDao;
}
