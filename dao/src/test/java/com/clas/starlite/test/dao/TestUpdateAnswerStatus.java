package com.clas.starlite.test.dao;

import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.domain.Answer;
import com.clas.starlite.domain.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Son on 8/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUpdateAnswerStatus extends BaseDaoTest{
    @Test
    public void testUpdateStatus(){
        List<Question> questions = questionDao.findAll();
        System.out.println("------- number of questions: " + questions.size());
        for (Question question : questions) {
            for(Answer ans: question.getAnswers()){
                System.out.println("-----" + ans.getId() + "\t" + ans.getStatus());
                ans.setStatus(Status.ACTIVE.getValue());
            }
//            questionDao.save(question);
        }
    }

    @Test
    public void testUpdateAQuestion(){
        Question question = questionDao.findOne("b8d3b650-6373-4372-ab53-555f2660d9a2");
        System.out.println("------- number of questions: " + question);
        for(Answer ans: question.getAnswers()){
            System.out.println("-----" + ans.getId() + "\t" + ans.getStatus());
            ans.setStatus(Status.ACTIVE.getValue());
        }
        questionDao.save(question);
    }
    @Autowired
    private QuestionDao questionDao;
}
