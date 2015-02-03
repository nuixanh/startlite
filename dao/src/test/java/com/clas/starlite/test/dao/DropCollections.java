package com.clas.starlite.test.dao;

import com.clas.starlite.dao.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Son on 8/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DropCollections extends BaseDaoTest{
    @Test
    public void dropCollections(){
        scenarioDao.deleteAll();
        revisionDao.deleteAll();
        revisionDao.dropRevisionHistory();
        solutionDao.deleteAll();
        solutionRuleDao.deleteAll();
        sectionDao.deleteAll();
        questionDao.deleteAll();
//        userDao.deleteAll();
//        sessionDao.deleteAll();
    }
    @Autowired
    private RevisionDao revisionDao;
    @Autowired
    private SolutionDao solutionDao;
    @Autowired
    private SolutionRuleDao solutionRuleDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SessionDao sessionDao;
}
