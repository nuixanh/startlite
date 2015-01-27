package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.dao.SectionDao;
import com.clas.starlite.domain.*;
import com.clas.starlite.util.CommonUtils;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.QuestionConverter;
import com.clas.starlite.webapp.converter.SectionConverter;
import com.clas.starlite.webapp.dto.QuestionDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
            List<Question> questions = section.getQuestions();
            if(questions != null && questions.size() > 0){
                for (Question question : questions) {
                    if(question.getDesc().equals(q.getDesc()) && !question.getId().equals(q.getId())){
                        return ErrorCodeMap.FAILURE_DUPLICATED_QUESTION;
                    }
                }
            }
        }
        return null;
    }
    public void addMoreInfoToQuestion(Question q, String userId){
        q.setId(UUID.randomUUID().toString());
        q.setCreated(System.currentTimeMillis());
        q.setModified(System.currentTimeMillis());
        //TODO: will be changed to Status.PENDING
        q.setStatus(Status.ACTIVE.getValue());
        q.setCreatedBy(userId);
        q.setModifiedBy(userId);
        for (Answer answer : q.getAnswers()) {
            answer.setId(UUID.randomUUID().toString());
            answer.setModified(System.currentTimeMillis());
            answer.setStatus(Status.ACTIVE.getValue());
        }
    }
    public QuestionDTO create(Question q, String userId){
        addMoreInfoToQuestion(q, userId);

        //TODO: will be removed
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_ADD, q.getId());
        q.setRevision(revision.getVersion());
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

    public Map<String, Object> update(Question question, String userId){
        Map<String, Object> output = new HashMap<String, Object>();
        boolean updateRule = false;
        if(StringUtils.isBlank(question.getId())){
            return output;
        }
        Question oldQuestion = questionDao.findOne(question.getId());
        if(oldQuestion == null){
            return output;
        }
        oldQuestion.setDesc(question.getDesc());
        oldQuestion.setType(question.getType());
        oldQuestion.setModified(System.currentTimeMillis());
        oldQuestion.setModifiedBy(userId);
        List<Answer> oldAnswers = oldQuestion.getAnswers();
        Map<String, Answer> oldAnswerMap = new HashMap<String, Answer>();
        for (Answer oldAnswer : oldAnswers) {
            oldAnswerMap.put(oldAnswer.getId(), oldAnswer);
        }
        Set<String> ansIDSet = new HashSet<String>();
        for (Answer answer : question.getAnswers()) {
            String ansId = answer.getId();
            if(StringUtils.isBlank(ansId)){
                ansId = UUID.randomUUID().toString();
                answer.setId(ansId);
                answer.setStatus(Status.ACTIVE.getValue());
                answer.setModified(System.currentTimeMillis());
                oldAnswers.add(answer);
            }else if(!oldAnswerMap.containsKey(ansId)){
                answer.setStatus(Status.ACTIVE.getValue());
                answer.setModified(System.currentTimeMillis());
                oldAnswers.add(answer);
            }else{
                Answer oldAnswer = oldAnswerMap.get(ansId);
                oldAnswer.setDesc(answer.getDesc());
                oldAnswer.setScore(answer.getScore());
                oldAnswer.setStatus(Status.ACTIVE.getValue());
                oldAnswer.setModified(System.currentTimeMillis());
            }
            ansIDSet.add(ansId);
        }
        Set<String> deletedAnswerIds = new HashSet<String>();
        for (Answer oldAnswer : oldAnswers) {
            if(!ansIDSet.contains(oldAnswer.getId()) && oldAnswer.getStatus() == Status.ACTIVE.getValue()){
                oldAnswer.setStatus(Status.DEACTIVE.getValue());
                deletedAnswerIds.add(oldAnswer.getId());
            }
        }
        if(deletedAnswerIds.size() > 0){
            updateRule = solutionService.updateSolutionRuleFromDeletedAnswers(question.getId(), deletedAnswerIds);
        }

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
        output.put(Constants.FLAG, new Boolean(updateRule));
        output.put(Constants.DTO, QuestionConverter.convert(oldQuestion));
        return output;
    }

    public Map<String, Object> updateStatus(String questionId, String userId, int status){
        Question q = questionDao.findOne(questionId);
        Map<String, Object> output = new HashMap<String, Object>();
        boolean updateRule = false;
        if(q != null){
            q.setStatus(status);
            q.setModifiedBy(userId);
            if(status == Status.ACTIVE.getValue()){
                Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_ADD, q.getId());
                q.setRevision(revision.getVersion());
            }else if(status == Status.DEACTIVE.getValue()){
                Revision revision;
                if(StringUtils.isNotBlank(q.getSectionId())){
                    Section section = sectionDao.findOne(q.getSectionId());
                    if(section != null) {
                        if(CollectionUtils.isNotEmpty(section.getQuestions())){
                            for (int i = section.getQuestions().size() -1; i >= 0 ; i--) {
                                Question question = section.getQuestions().get(i);
                                if(questionId.equals(question.getId())){
                                    section.getQuestions().remove(i);
                                }
                            }
                        }

                        if(CollectionUtils.isNotEmpty(section.getScenarios())){
                            List<Scenario> scList = CommonUtils.newArrayList();
                            for(Scenario scenario: section.getScenarios()){
                                Map<String, Set<String>> sectionMap = scenario.getSectionMap();
                                if(sectionMap != null && sectionMap.containsKey(section.getId()) && sectionMap.get(section.getId()).contains(questionId)){
                                    if(sectionMap.get(section.getId()).size() == 1){
                                        scList.add(scenario);
                                    }else{

                                    }
                                }
                            }
                            if(scList.size() > 0){
                                output.put(Constants.ERROR_CODE, ErrorCodeMap.FAILURE_DETACH_SECTION_BEFORE_DELETE_QUESTION);
                                output.put(Constants.DATA, scList);
                                output.put(Constants.DTO, section);
                                return output;
                            }
                        }
                    }
                    revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_DELETE, q.getId());
                    q.setRevision(revision.getVersion());
                    section.setRevision(revision.getVersion());
                    sectionDao.save(section);
                }else{
                    revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_DELETE, q.getId());
                    q.setRevision(revision.getVersion());
                }
                Set<String> qIds = new HashSet<String>();
                qIds.add(questionId);
                updateRule = solutionService.updateSolutionRuleFromDeletedQuestions(qIds);
            }
            questionDao.save(q);
            output.put(Constants.DTO, QuestionConverter.convert(q));
            output.put(Constants.FLAG, new Boolean(updateRule));
        }else{
            output.put(Constants.ERROR_CODE, ErrorCodeMap.FAILURE_OBJECT_NOT_FOUND);
        }
        return output;
    }

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private RevisionDao revisionDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private SolutionService solutionService;
}
