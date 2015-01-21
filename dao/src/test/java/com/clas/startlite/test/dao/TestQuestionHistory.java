package com.clas.startlite.test.dao;

import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.QuestionHistoryDao;
import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.QuestionHistory;
import com.clas.starlite.util.CommonUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Admin on 1/21/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestQuestionHistory extends BaseDaoTest{
    @Test
    public void testSnapshotQuestion() throws Exception{
        Question question = questionDao.findOne("7c26dc33-481e-4e98-8e52-c3505cfd5d05");
        QuestionHistory qHistory = questionHistoryDao.snapshotQuestion(question);
        System.out.println(CommonUtils.printPrettyObj(qHistory));
    }
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionHistoryDao questionHistoryDao;
}
