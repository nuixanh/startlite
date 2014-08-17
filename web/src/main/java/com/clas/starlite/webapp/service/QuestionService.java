package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.domain.Answer;
import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.Revision;
import com.clas.starlite.webapp.converter.QuestionConverter;
import com.clas.starlite.webapp.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by Son on 8/17/14.
 */
@Component
public class QuestionService {

    public QuestionDTO create(Question q){
        q.setId(UUID.randomUUID().toString());
        q.setCreated(System.currentTimeMillis());
        q.setModified(System.currentTimeMillis());
        q.setStatus(Status.PENDING.getValue());
        for (Answer answer : q.getAnswers()) {
            answer.setId(UUID.randomUUID().toString());
            answer.setModified(System.currentTimeMillis());
        }
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_ADD, q.getId());
        q.setRevision(revision.getVersion());
        questionDao.save(q);
        return QuestionConverter.convert(q);
    }
    public QuestionDTO updateStatus(String questionId, String userId, int status){
        Question q = questionDao.findOne(questionId);
        if(q != null){
            q.setStatus(status);
            q.setModifiedBy(userId);
            questionDao.save(q);
            return QuestionConverter.convert(q);
        }else{
            return null;
        }
    }

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private RevisionDao revisionDao;
}
