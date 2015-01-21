package com.clas.starlite.webapp.service;

import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.QuestionHistoryDao;
import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.QuestionHistory;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Admin on 1/21/2015.
 */
@Component
public class HistoryService {
    public QuestionHistory snapshotQuestion(Question question){
        QuestionHistory qHistory;
        try {
            qHistory = questionHistoryDao.getOneByIdAndRevision(question.getId(), question.getRevision());
            if(qHistory == null){
                qHistory = new QuestionHistory();
                BeanUtilsBean.getInstance().copyProperties(qHistory, question);
                questionHistoryDao.save(qHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            qHistory = null;
        }
        return qHistory;
    }

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionHistoryDao questionHistoryDao;
}
