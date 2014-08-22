package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.dao.SectionDao;
import com.clas.starlite.domain.*;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.QuestionConverter;
import com.clas.starlite.webapp.dto.QuestionDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Son on 8/17/14.
 */
@Component
public class QuestionService {

    public ErrorCodeMap validate(Question q){
        if(q == null || StringUtils.isBlank(q.getDesc()) || q.getAnswers() == null || StringUtils.isBlank(q.getSectionId())){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else {
            Section section = sectionDao.findOne(q.getSectionId());
            if(section == null){
                return ErrorCodeMap.FAILURE_SECTION_NOT_FOUND;
            }
        }
        return null;
    }
    public QuestionDTO create(Question q, String userId){
        q.setId(UUID.randomUUID().toString());
        q.setCreated(System.currentTimeMillis());
        q.setModified(System.currentTimeMillis());
        //TODO: will be changed to Status.PENDING
        q.setStatus(Status.ACTIVE.getValue());

        //TODO: will be removed
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_ADD, q.getId());
        q.setRevision(revision.getVersion());

        q.setCreatedBy(userId);
        q.setModifiedBy(userId);
        for (Answer answer : q.getAnswers()) {
            answer.setId(UUID.randomUUID().toString());
            answer.setModified(System.currentTimeMillis());
        }
        if(StringUtils.isNotBlank(q.getSectionId())){
            Section section = sectionDao.findOne(q.getSectionId());
            if(section != null){
                if(section.getQuestions() == null){
                    section.setQuestions(new ArrayList<Question>());
                }
                section.getQuestions().add(q);
                section.setRevision(revision.getVersion());
                sectionDao.save(section);
            }
        }
        questionDao.save(q);
        return QuestionConverter.convert(q);
    }

    public QuestionDTO update(Question question, String userId){
        if(StringUtils.isBlank(question.getId())){
            return null;
        }
        Question oldQuestion = questionDao.findOne(question.getId());
        if(oldQuestion == null){
            return null;
        }
        oldQuestion.setDesc(question.getDesc());
        oldQuestion.setModified(System.currentTimeMillis());
        oldQuestion.setModifiedBy(userId);
        for (Answer answer : question.getAnswers()) {
            answer.setId(UUID.randomUUID().toString());
            answer.setModified(System.currentTimeMillis());
        }
        oldQuestion.setAnswers(question.getAnswers());
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_EDIT, oldQuestion.getId());
        oldQuestion.setRevision(revision.getVersion());

        if(StringUtils.isNotBlank(question.getSectionId())){
            Section section = sectionDao.findOne(question.getSectionId());
            if(section != null){
                section.setRevision(revision.getVersion());
                sectionDao.save(section);
            }
            if(!question.getSectionId().equals(oldQuestion.getSectionId())){
                if(section != null){
                    if(section.getQuestions() == null){
                        section.setQuestions(new ArrayList<Question>());
                    }
                    section.getQuestions().add(oldQuestion);
                    sectionDao.save(section);
                }
                if(StringUtils.isNotBlank(oldQuestion.getSectionId())){
                    Section oldSection = sectionDao.findOne(oldQuestion.getSectionId());
                    if(oldSection != null){
                        if(oldSection.getQuestions() != null){
                            for (int i = oldSection.getQuestions().size() - 1; i >= 0; i--) {
                                if(oldSection.getQuestions().get(i).getId().equals(oldQuestion.getId())){
                                    oldSection.getQuestions().remove(i);
                                }
                            }
                            sectionDao.save(oldSection);
                        }
                    }
                }
            }
        }
        questionDao.save(oldQuestion);
        return QuestionConverter.convert(oldQuestion);
    }
    public QuestionDTO updateStatus(String questionId, String userId, int status){
        Question q = questionDao.findOne(questionId);
        if(q != null){
            q.setStatus(status);
            q.setModifiedBy(userId);
            if(status == Status.ACTIVE.getValue()){
                Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_ADD, q.getId());
                q.setRevision(revision.getVersion());
            }else if(status == Status.DEACTIVE.getValue()){
                Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_DELETE, q.getId());
                q.setRevision(revision.getVersion());
                if(StringUtils.isNotBlank(q.getSectionId())){
                    Section section = sectionDao.findOne(q.getSectionId());
                    if(section != null && section.getQuestions() != null){
                        for (int i = section.getQuestions().size() -1; i >= 0 ; i--) {
                            Question question = section.getQuestions().get(i);
                            if(questionId.equals(question.getId())){
                                section.getQuestions().remove(i);
                            }
                        }
                    }
                    section.setRevision(revision.getVersion());
                    sectionDao.save(section);
                }
            }
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
    @Autowired
    private SectionDao sectionDao;
}
