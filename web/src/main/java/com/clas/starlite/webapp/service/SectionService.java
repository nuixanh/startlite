package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.dao.SectionDao;
import com.clas.starlite.domain.*;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.SectionConverter;
import com.clas.starlite.webapp.dto.SectionDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Son on 8/19/14.
 */
@Component
public class SectionService {

    public List<SectionDTO> getList(Long revision){
        List<Section> sections;
        try {
            sections = sectionDao.getTrees(revision);
        } catch (Exception e) {
            e.printStackTrace();
            sections = new ArrayList<Section>();
        }
        return SectionConverter.convert(sections);
    }

    public ErrorCodeMap validate(Section section){
        if(section == null || StringUtils.isBlank(section.getName())){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }
        List<Section> sections = sectionDao.getActiveByName(section.getName().trim());
        for (Section s : sections) {
            if(!s.getId().equals(section.getId())){
                return ErrorCodeMap.FAILURE_DUPLICATED_NAME;
            }
        }
        return null;
    }
    public SectionDTO create(Section section, String userId){
        section.setId(UUID.randomUUID().toString());
        section.setCreated(System.currentTimeMillis());
        section.setModified(System.currentTimeMillis());
        section.setCreatedBy(userId);
        section.setModifiedBy(userId);
        section.setName(section.getName().trim());
        section.setStatus(Status.ACTIVE.getValue());
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_ADD_SECTION, section.getId());
        section.setRevision(revision.getVersion());
        section.setMyRevision(revision.getVersion());

        sectionDao.save(section);
        return SectionConverter.convert(section);
    }

    public Map<String, Object> batchUpload(Section section, String userId){
        Map<String, Object> output = new HashMap<String, Object>();
        ErrorCodeMap errorCode = null;
        if(section == null || StringUtils.isBlank(section.getName()) || section.getQuestions() == null
                || section.getQuestions().size() == 0){
            errorCode = ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else{
            Section oldSection = sectionDao.getOneActiveByName(section.getName().trim());
            Map<String, Question> descQuestionMap = new HashMap<String, Question>();
            if(errorCode == null){
                for (Question question : section.getQuestions()) {
                    if(descQuestionMap.containsKey(question.getDesc())){
                        errorCode = ErrorCodeMap.FAILURE_DUPLICATED_QUESTION;
                        break;
                    }
                    descQuestionMap.put(question.getDesc(), question);
                }
            }
            if(errorCode == null){
                Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_BATCH_UPLOAD_SECTION, oldSection.getId());
                if(oldSection != null){
                    Map<String, Question> oldDescQuestionMap = new HashMap<String, Question>();
                    for (Question oldQuestion : oldSection.getQuestions()) {
                        oldDescQuestionMap.put(oldQuestion.getDesc(), oldQuestion);
                    }
                    for (Question question : section.getQuestions()) {
                        Question oldQuestion = oldDescQuestionMap.get(question.getDesc());
                        if(oldQuestion != null){
                            oldQuestion.setType(question.getType());
                            oldQuestion.setRevision(revision.getVersion());
                            oldQuestion.setModifiedBy(userId);
                            oldQuestion.setModified(System.currentTimeMillis());
                            Map<String, Answer> oldDescAnswerMap = new HashMap<String, Answer>();
                            for(Answer answer: oldQuestion.getAnswers()){
                                oldDescAnswerMap.put(answer.getDesc(), answer);
                            }
                            for(Answer answer: question.getAnswers()){
                                Answer oldAnswer = oldDescAnswerMap.get(answer.getDesc());
                                if(oldAnswer != null){
                                    oldAnswer.setScore(answer.getScore());
                                    oldAnswer.setModified(System.currentTimeMillis());
                                }else{
                                    answer.setId(UUID.randomUUID().toString());
                                    answer.setModified(System.currentTimeMillis());
                                    answer.setStatus(Status.ACTIVE.getValue());
                                    oldQuestion.getAnswers().add(answer);
                                }
                            }
                        }else{//new question to an existing section
                            questionService.addMoreInfoToQuestion(question, userId);
                            question.setRevision(revision.getVersion());
                            oldSection.getQuestions().add(question);
                        }
                    }

                    oldSection.setRevision(revision.getVersion());
                    oldSection.setMyRevision(revision.getVersion());
                    oldSection.setModifiedBy(userId);
                    oldSection.setModified(System.currentTimeMillis());
                    sectionDao.save(oldSection);
                    output.put(Constants.DTO, SectionConverter.convert(oldSection));
                }else{
                    for (Question question : section.getQuestions()) {
                        questionService.addMoreInfoToQuestion(question, userId);
                        question.setRevision(revision.getVersion());
                    }
                    section.setRevision(revision.getVersion());
                    section.setMyRevision(revision.getVersion());
                    section.setName(section.getName().trim());
                    section.setModifiedBy(userId);
                    section.setModified(System.currentTimeMillis());
                    sectionDao.save(section);
                    output.put(Constants.DTO, SectionConverter.convert(section));
                }

            }
        }
        if(errorCode != null){
            output.put(Constants.ERROR_CODE, errorCode);
        }
        return output;
    }
    public SectionDTO update(Section section, String userId){
        if(StringUtils.isBlank(section.getId())){
            return null;
        }
        Section oldSection = sectionDao.findOne(section.getId());
        if(oldSection == null){
            return null;
        }
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_EDIT_SECTION, oldSection.getId());
        oldSection.setRevision(revision.getVersion());
        oldSection.setMyRevision(revision.getVersion());
        oldSection.setName(section.getName().trim());
        oldSection.setModifiedBy(userId);
        oldSection.setModified(System.currentTimeMillis());
        sectionDao.save(oldSection);
        return SectionConverter.convert(oldSection);
    }
    public SectionDTO delete(String sectionId, String userId){
        Section section = sectionDao.findOne(sectionId);
        if(section != null){
            section.setStatus(Status.DEACTIVE.getValue());
            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_DELETE_SECTION, section.getId());
            section.setRevision(revision.getVersion());
            section.setMyRevision(revision.getVersion());
            section.setModifiedBy(userId);
            section.setModified(System.currentTimeMillis());
            List<Question> questions = section.getQuestions();
            section.setQuestions(null);
            revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_DELETE_SECTION, section.getId());
            if(section.getScenarios() != null && section.getScenarios().size() > 0){
                for (Scenario sc : section.getScenarios()) {
                    Scenario rootParent = scenarioDao.findOne(sc.getRootParentId());
                    rootParent.setRevision(revision.getVersion());
                    scenarioDao.save(rootParent);
                    Set<String> sectionSet = sc.getSections();
                    if(sectionSet != null && sectionSet.size() > 0){
                        Set<String> newSectionSet = new HashSet<String>();
                        for (String sID : sectionSet) {
                            if(!sID.equals(sectionId)){
                                newSectionSet.add(sID);
                            }
                        }
                        if(newSectionSet.size() > 0){
                            sc.setSections(newSectionSet);
                        }else{
                            sc.setSections(null);
                        }
                        scenarioDao.save(sc);
                    }
                }
            }
            section.setScenarios(null);
            sectionDao.save(section);
            if(questions != null && questions.size() > 0){
                Set<String> qIds = new HashSet<String>();
                for (Question question : questions) {
                    qIds.add(question.getId());
                    question.setStatus(Status.DEACTIVE.getValue());
                    question.setModifiedBy(userId);
                    question.setModified(System.currentTimeMillis());
                    questionDao.save(question);
                }
                solutionService.updateSolutionRuleFromDeletedQuestions(qIds);
            }

            return SectionConverter.convert(section);
        }else{
            return null;
        }
    }
    public ErrorCodeMap detachToScenario(String sectionId, String scenarioId, String userId){
        if(StringUtils.isBlank(sectionId) || StringUtils.isBlank(scenarioId)){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }
        Section section = sectionDao.findOne(sectionId);
        if(section == null){
            return ErrorCodeMap.FAILURE_SECTION_NOT_FOUND;
        }
        Scenario detachedScenario = scenarioDao.findOne(scenarioId);
        if(detachedScenario == null){
            return ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND;
        }
        Set<String> sectionSet = detachedScenario.getSections();
        if(sectionSet != null && sectionSet.size() > 0){
            Set<String> newSectionSet = new HashSet<String>();
            for (String sID : sectionSet) {
                if(!sID.equals(sectionId)){
                    newSectionSet.add(sID);
                }
            }
            if(newSectionSet.size() > 0){
                detachedScenario.setSections(newSectionSet);
            }else{
                detachedScenario.setSections(null);
            }
        }
        if(section.getScenarios() != null && section.getScenarios().size() > 0){
            for (int i = section.getScenarios().size() - 1; i >= 0 ; i--) {
                Scenario scOfSection = section.getScenarios().get(i);
                if(scOfSection.getId().equals(scenarioId)){
                    section.getScenarios().remove(i);
                    break;
                }
            }
        }

        section.setModified(System.currentTimeMillis());
        section.setModifiedBy(userId);
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_DETACH, section.getId());
        section.setRevision(revision.getVersion());
        String rootParentId = detachedScenario.getRootParentId();
        if(rootParentId.equals(detachedScenario.getId())){
            detachedScenario.setRevision(revision.getVersion());
        }else{
            Scenario rootParent = scenarioDao.findOne(rootParentId);
            rootParent.setRevision(revision.getVersion());
            scenarioDao.save(rootParent);
        }
        sectionDao.save(section);
        scenarioDao.save(detachedScenario);
        return null;
    }

    public ErrorCodeMap attachToScenario(String sectionId, String scenarioId, String userId){
        if(StringUtils.isBlank(sectionId) || StringUtils.isBlank(scenarioId)){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }
        Section section = sectionDao.findOne(sectionId);
        if(section == null){
            return ErrorCodeMap.FAILURE_SECTION_NOT_FOUND;
        }
        Scenario attachedScenario = scenarioDao.findOne(scenarioId);
        if(attachedScenario == null){
            return ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND;
        }
        List<Scenario> parents = section.getScenarios();
        if(parents != null && parents.size() > 0){
            for (Scenario parent : parents) {
                if(parent.getRootParentId().equals(attachedScenario.getRootParentId())){
                    return ErrorCodeMap.FAILURE_SECTION_BELONG_SAME_ROOT_SCENARIO;
                }
            }
        }

        if(attachedScenario.getSections() == null){
            attachedScenario.setSections(new HashSet<String>());
        }
        attachedScenario.getSections().add(section.getId());

        if(section.getScenarios() == null){
            section.setScenarios(new ArrayList<Scenario>());
        }
        section.getScenarios().add(attachedScenario);
        section.setModified(System.currentTimeMillis());
        section.setModifiedBy(userId);
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_ATTACH, section.getId());
        section.setRevision(revision.getVersion());
        if(parents != null && parents.size() > 0){
            for (Scenario parent : parents) {
                if(parent.getRootParentId().equals(parent.getId())){
                    parent.setRevision(revision.getVersion());
                    scenarioDao.save(parent);
                }else{
                    Scenario rootParent = scenarioDao.findOne(parent.getRootParentId());
                    rootParent.setRevision(revision.getVersion());
                    scenarioDao.save(rootParent);
                }
            }
        }
        String rootParentId = attachedScenario.getRootParentId();
        if(rootParentId.equals(attachedScenario.getId())){
            attachedScenario.setRevision(revision.getVersion());
        }else{
            Scenario rootParent = scenarioDao.findOne(rootParentId);
            rootParent.setRevision(revision.getVersion());
            scenarioDao.save(rootParent);
        }
        sectionDao.save(section);
        scenarioDao.save(attachedScenario);

        return null;
    }

    @Autowired
    private RevisionDao revisionDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private QuestionService questionService;
}
