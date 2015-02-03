package com.clas.starlite.test.dao;

import com.clas.starlite.dao.AssessmentDao;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.QuestionHistoryDao;
import com.clas.starlite.domain.Assessment;
import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.QuestionHistory;
import com.clas.starlite.util.CommonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Admin on 1/21/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAssessment extends BaseDaoTest{
    @Test
    public void testGetByRevision() throws Exception{
        String userId = "15de4fe7-9aa3-4574-9e99-5e7a92e8bd2b";
        Long revisionByUser = 23L;
        List<Assessment> assessments = assessmentDao.getByRevision(userId, revisionByUser);
        for (Assessment assessment : assessments) {
//            System.out.println(assessment.getCountByUser());
            System.out.println();
            System.out.println("------------------------------------------------------------------");
//            System.out.println(assessment.getDto());
            System.out.println(assessment.getRootScenarioHistory());
            System.out.println("------------------------------------------------------------------");
            System.out.println();
        }
    }
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionHistoryDao questionHistoryDao;
    @Autowired
    private AssessmentDao assessmentDao;
}
