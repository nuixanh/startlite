package com.clas.starlite.dao;

import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.QuestionHistory;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by Son on 8/14/14.
 */
public class QuestionHistoryDao extends BaseDao<QuestionHistory, String>{
    public QuestionHistory getOneByIdAndRevision(String id, long revision){
        Criteria cr = Criteria.where("id").is(id).and("revision").is(revision);
        Query q = Query.query(cr);
        return template.findOne(q, QuestionHistory.class);
    }
    public QuestionHistory snapshotQuestion(Question question){
        QuestionHistory qHistory;
        try {
            qHistory = getOneByIdAndRevision(question.getId(), question.getRevision());
            if(qHistory == null){
                qHistory = new QuestionHistory();
                BeanUtilsBean.getInstance().copyProperties(qHistory, question);
                template.save(qHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            qHistory = null;
        }
        return qHistory;
    }
}
