package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.*;
import com.clas.starlite.domain.*;
import com.clas.starlite.util.CommonUtils;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.SectionConverter;
import com.clas.starlite.webapp.dto.AssessmentInstanceDTO;
import com.clas.starlite.webapp.dto.SectionDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Son on 8/19/14.
 */
@Component
public class SectionService {
    public SectionHistory snapshotSection(Section section){
        SectionHistory sHistory;
        try {
            sHistory = sectionHistoryDao.getOneByIdAndRevision(section.getId(), section.getRevision());
            if(sHistory == null){
                sHistory = new SectionHistory();
                BeanUtilsBean.getInstance().copyProperties(sHistory, section);
                List<Question> questions = sHistory.getQuestions();
                if(questions != null && questions.size() > 0){
                    sHistory.setQuestionHistories(new ArrayList<QuestionHistory>());
                    for (Question question : questions) {
                        QuestionHistory questionHistory = questionHistoryDao.snapshotQuestion(question);
                        sHistory.getQuestionHistories().add(questionHistory);
                    }
                }
                sHistory.setQuestions(null);
                List<Scenario> scenarios = sHistory.getScenarios();
                if(scenarios != null && scenarios.size() > 0){
                    sHistory.setScenarioHistories(new ArrayList<ScenarioHistory>());
                    for (Scenario childSc : scenarios) {
                        ScenarioHistory childHistory = scenarioHistoryDao.snapshotScenario(childSc);
                        sHistory.getScenarioHistories().add(childHistory);
                    }
                }
                sHistory.setScenarios(null);
                sectionHistoryDao.save(sHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sHistory = null;
        }
        return sHistory;
    }
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
        Long errorLine = new Long(-1);
        if(section == null || StringUtils.isBlank(section.getName()) || section.getQuestions() == null
                || section.getQuestions().size() == 0){
            errorCode = ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else{
            Section oldSection = sectionDao.getOneActiveByName(section.getName().trim());
            Map<String, Question> descQuestionMap = new HashMap<String, Question>();
            if(errorCode == null){
                long idx = 1;
                for (Question question : section.getQuestions()) {
                    if(descQuestionMap.containsKey(question.getDesc())){
                        errorCode = ErrorCodeMap.FAILURE_DUPLICATED_QUESTION_CSV;
                        errorLine = idx;
                        idx++;
                        break;
                    }
                    descQuestionMap.put(question.getDesc(), question);
                }
            }
            if(errorCode == null && oldSection != null){
                Map<String, Question> oldDescQuestionMap = new HashMap<String, Question>();
                for (Question oldQuestion : oldSection.getQuestions()) {
                    oldDescQuestionMap.put(oldQuestion.getDesc(), oldQuestion);
                }
                long idx = 1;
                for (Question question : section.getQuestions()) {
                    if(oldDescQuestionMap.containsKey(question.getDesc())){
                        errorCode = ErrorCodeMap.FAILURE_DUPLICATED_QUESTION;
                        errorLine = idx;
                        idx++;
                        break;
                    }
                }
            }
            String sectionId = oldSection != null? oldSection.getId(): UUID.randomUUID().toString();
            if(errorCode == null){
                Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_BATCH_UPLOAD_SECTION, sectionId);
                if(oldSection != null){
                    for (Question question : section.getQuestions()) {
                        //new question to an existing section
                        questionService.addMoreInfoToQuestion(question, userId);
                        question.setRevision(revision.getVersion());
                        question.setSectionId(oldSection.getId());
                        if(oldSection.getQuestions() == null){
                            oldSection.setQuestions(new ArrayList<Question>());
                        }
                        oldSection.getQuestions().add(question);
                        questionDao.save(question);
                    }
                    oldSection.setRevision(revision.getVersion());
                    oldSection.setMyRevision(revision.getVersion());
                    oldSection.setModifiedBy(userId);
                    oldSection.setModified(System.currentTimeMillis());
                    sectionDao.save(oldSection);
                    output.put(Constants.DTO, SectionConverter.convert(oldSection));
                }else{//new Section
                    section.setId(sectionId);
                    section.setStatus(Status.ACTIVE.getValue());
                    section.setRevision(revision.getVersion());
                    section.setMyRevision(revision.getVersion());
                    section.setName(section.getName().trim());
                    section.setModifiedBy(userId);
                    section.setCreatedBy(userId);
                    section.setModified(System.currentTimeMillis());
                    for (Question question : section.getQuestions()) {
                        questionService.addMoreInfoToQuestion(question, userId);
                        question.setRevision(revision.getVersion());
                        question.setSectionId(section.getId());
                        questionDao.save(question);
                    }
                    sectionDao.save(section);
                    output.put(Constants.DTO, SectionConverter.convert(section));
                }
            }
        }
        if(errorCode != null){
            output.put(Constants.ERROR_CODE, errorCode);
        }
        output.put(Constants.ERROR_LINE, errorLine);
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
    public Map<String, Object> delete(String sectionId, String userId){
        Map<String, Object> output = new HashMap<String, Object>();
        boolean updateRule = false;
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
                    Map<String, Set<String>> sectionMap = sc.getSectionMap();
                    boolean modified = false;
                    if(sectionMap != null && sectionMap.containsKey(sectionId)){
                        sectionMap.remove(sectionId);
                        modified = true;
                    }
                    if(CollectionUtils.isNotEmpty(sectionSet)){
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
                        modified = true;
                    }
                    if(modified){
                        scenarioDao.save(sc);
                    }
                }
            }
            section.setScenarios(null);
            sectionDao.save(section);
            if(CollectionUtils.isNotEmpty(questions)){
                Set<String> qIds = new HashSet<String>();
                for (Question question : questions) {
                    qIds.add(question.getId());
                    question.setStatus(Status.DEACTIVE.getValue());
                    question.setModifiedBy(userId);
                    question.setModified(System.currentTimeMillis());
                    questionDao.save(question);
                }
                updateRule = solutionService.updateSolutionRuleFromDeletedQuestions(qIds);
            }
            output.put(Constants.DTO, SectionConverter.convert(section));
            output.put(Constants.FLAG, updateRule);
        }
        return output;
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
        Map<String, Set<String>> sectionMap = detachedScenario.getSectionMap();
        if(sectionMap != null && sectionMap.containsKey(sectionId)){
            sectionMap.remove(sectionId);
        }
        if(CollectionUtils.isNotEmpty(sectionSet)){
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
        if(CollectionUtils.isNotEmpty(section.getScenarios())){
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
    public Map<String, Object> copy(String sectionId, String sectionName, String userId) {
        Map<String, Object> output = new HashMap<String, Object>();
        if(StringUtils.isBlank(sectionId)){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_INVALID_PARAMS);
        }
        Section oldSection = sectionDao.findOne(sectionId);
        if(oldSection == null){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SECTION_NOT_FOUND);
        }
        String validName = null;
        if(StringUtils.isNotBlank(sectionName)){
            List<Section> sections = sectionDao.getActiveByName(sectionName.trim());
            for (Section s : sections) {
                if(!s.getId().equals(oldSection.getId())){
                    return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_DUPLICATED_NAME);
                }
            }
            validName = sectionName.trim();
        }else{
            String oldName = oldSection.getName();
            List<String> nameList = CommonUtils.newArrayList();
            Map<String, Section> nameMap = CommonUtils.newHashMap();
            for (int i = 1; i < 11; i++) {
                String newName = oldName + " - copy " + i;
                nameList.add(newName);
                nameMap.put(newName, null);
            }
            List<Section> sections = sectionDao.getActiveByName(nameList);
            for (Section s : sections) {
                nameMap.put(s.getName(), s);
            }
            for (String name : nameList) {
                Section s = nameMap.get(name);
                if(s == null){
                    validName = name;
                    break;
                }
            }
            if(StringUtils.isBlank(validName)){
                validName = oldName + " (" + UUID.randomUUID().toString() + ")";
            }
        }
        try{
            Section newSection = new Section();
            newSection.setName(validName);
            newSection.setId(UUID.randomUUID().toString());
            newSection.setCreated(System.currentTimeMillis());
            newSection.setModified(System.currentTimeMillis());
            newSection.setCreatedBy(userId);
            newSection.setModifiedBy(userId);
            newSection.setStatus(Status.ACTIVE.getValue());
            Set<String> entityIdSet = CommonUtils.newHashSet();
            entityIdSet.add(newSection.getId());
            if(CollectionUtils.isNotEmpty(oldSection.getQuestions())){
                newSection.setQuestions(new ArrayList<Question>());
                for (Question oldQuestion : oldSection.getQuestions()) {
                    Question newQuestion = questionService.copy(oldQuestion, newSection, userId);
                    entityIdSet.add(newQuestion.getId());
                    newSection.getQuestions().add(newQuestion);
                }
            }

            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_COPY_SECTION, entityIdSet, oldSection.getId());
            newSection.setRevision(revision.getVersion());
            newSection.setMyRevision(revision.getVersion());
            for (Question newQuestion : newSection.getQuestions()) {
                newQuestion.setRevision(revision.getVersion());
                questionDao.save(newQuestion);
            }
            sectionDao.save(newSection);

            output.put(Constants.DTO, SectionConverter.convert(newSection));
            output.put(Constants.REVISION, loginService.getCurrentRevision());

        }catch (Exception e){
            e.printStackTrace();
            return RestUtils.createExceptionOutput(output, e);
        }

        return output;
    }

    public ErrorCodeMap attachToScenario(String sectionId, String scenarioId, String userId, String questionIDs){
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
        Set<String> sectionIDSet = attachedScenario.getSections();
        Map<String, Set<String>> sectionMap = attachedScenario.getSectionMap();
        boolean isNewAttach = true;
        if(sectionIDSet != null & sectionIDSet.contains(sectionId)){
            isNewAttach = false;
        }
        if(StringUtils.isNotBlank(questionIDs)){
            String[] qIDarr = questionIDs.split(",", -1);
            if(CollectionUtils.isEmpty(section.getQuestions()) || section.getQuestions().size() < qIDarr.length){
                return ErrorCodeMap.FAILURE_INVALID_QUESTION;
            }
            Set<String> qIDSet = CommonUtils.newHashSet();
            for(Question question: section.getQuestions()){
                qIDSet.add(question.getId());
            }
            for (int i = 0; i < qIDarr.length; i++) {
                if(!qIDSet.contains(qIDarr[i])){
                    return ErrorCodeMap.FAILURE_INVALID_QUESTION;
                }
            }

            if(sectionMap == null){
                sectionMap = CommonUtils.newHashMap();
                attachedScenario.setSectionMap(sectionMap);
            }
            qIDSet = sectionMap.get(sectionId);
            if(qIDSet == null){
                qIDSet = CommonUtils.newHashSet();
                sectionMap.put(sectionId, qIDSet);
            }
            for (int i = 0; i < qIDarr.length; i++) {
                qIDSet.add(qIDarr[i]);
            }
        }else if(!isNewAttach && sectionMap != null && sectionMap.containsKey(sectionId)){//edit attachment
            sectionMap.remove(sectionId);
        }

        List<Scenario> parents = section.getScenarios();
        if(parents != null && parents.size() > 0 && isNewAttach){
            for (Scenario parent : parents) {
                if(parent.getStatus() == Status.ACTIVE.getValue() && parent.getRootParentId().equals(attachedScenario.getRootParentId())){
                    return ErrorCodeMap.FAILURE_SECTION_BELONG_SAME_ROOT_SCENARIO;
                }
            }
        }

        if(sectionIDSet == null){
            sectionIDSet = CommonUtils.newHashSet();
        }
        sectionIDSet.add(section.getId());

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
    private SectionHistoryDao sectionHistoryDao;
    @Autowired
    private QuestionHistoryDao questionHistoryDao;
    @Autowired
    private ScenarioHistoryDao scenarioHistoryDao;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private LoginService loginService;
}
