package com.clas.starlite.test.dao;

import com.clas.starlite.common.Status;
import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.domain.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by Son on 8/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestInsertScenario extends BaseDaoTest{
    @Test
    public void testInsert(){
        Scenario scenario = new Scenario();
        scenario.setId(UUID.randomUUID().toString());
        scenario.setName("Scenario 1");
        scenario.setCreated(System.currentTimeMillis());
        scenario.setModified(scenario.getCreated());
        scenario.setStatus(Status.ACTIVE.getValue());
        scenarioDao.save(scenario);
    }
    @Autowired
    private ScenarioDao scenarioDao;
}
