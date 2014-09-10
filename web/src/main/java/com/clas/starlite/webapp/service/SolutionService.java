package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.SolutionDao;
import com.clas.starlite.dao.SolutionRuleDao;
import com.clas.starlite.domain.*;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.SectionConverter;
import com.clas.starlite.webapp.converter.SolutionConverter;
import com.clas.starlite.webapp.dto.SolutionDTO;
import com.clas.starlite.webapp.dto.SolutionRuleDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Son on 8/17/14.
 */
@Component
public class SolutionService {
    public ErrorCodeMap validateRule(SolutionRule rule){
        if(rule == null || StringUtils.isBlank(rule.getSolutionId()) || rule.getConditions() == null
                || rule.getConditions().size() == 0){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }
        if(StringUtils.isNotBlank(rule.getSolutionId())){
            Solution s = solutionDao.findOne(rule.getSolutionId());
            if(s == null){
                return ErrorCodeMap.FAILURE_SOLUTION_NOT_FOUND;
            }
        }

        for (List<RuleCondition> conditionList : rule.getConditions()) {
            for (RuleCondition condition : conditionList) {
                if(condition.getAnswerIds() == null || condition.getAnswerIds().size() == 0
                        || StringUtils.isBlank(condition.getQuestionId())){
                    return ErrorCodeMap.FAILURE_INVALID_CONDITIONS;
                }else{
                    Question q = questionDao.findOne(condition.getQuestionId());
                    if(q == null){
                        return ErrorCodeMap.FAILURE_INVALID_CONDITIONS;
                    }
                    List<Answer> answers = q.getAnswers();
                    Set<String> answerIds = new HashSet<String>();
                    for (Answer answer : answers) {
                        answerIds.add(answer.getId());
                    }
                    for(List<String> aIds: condition.getAnswerIds()){
                        for (String aId : aIds) {
                            if(!answerIds.contains(aId)){
                                return ErrorCodeMap.FAILURE_INVALID_CONDITIONS;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    private void removeRuleFromSolution(String ruleId, String solutionId, Revision revision){
        Solution solution = solutionDao.findOne(solutionId);
        if(solution != null){
            String rootParentId = solution.getRootParentId();
            if(!rootParentId.equals(solution.getId())){
                Solution rootSolution = solutionDao.findOne(rootParentId);
                rootSolution.setRevision(revision.getVersion());
                solutionDao.save(rootSolution);
            }else{
                solution.setRevision(revision.getVersion());
            }
            for (int i = solution.getRules().size() - 1; i >= 0 ; i--) {
                if(solution.getRules().get(i).getId().equals(ruleId)){
                    solution.getRules().remove(i);
                }
            }
            solutionDao.save(solution);
        }
    }
    public SolutionRuleDTO deleteRule(String ruleId, String userId){
        SolutionRule rule = solutionRuleDao.findOne(ruleId);
        if(rule != null){
            rule.setStatus(Status.DEACTIVE.getValue());
            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION, Constants.REVISION_ACTION_DELETE_RULE, rule.getId());
            rule.setRevision(revision.getVersion());
            rule.setModifiedBy(userId);
            rule.setModified(System.currentTimeMillis());

            if(StringUtils.isNotBlank(rule.getSolutionId())){
                removeRuleFromSolution(rule.getId(), rule.getSolutionId(), revision);
            }
            solutionRuleDao.save(rule);
            return SolutionConverter.convertRule(rule);
        }else{
            return null;
        }
    }
    public SolutionRuleDTO updateRule(SolutionRule r, String userId){
        if(StringUtils.isBlank(r.getId())){
            return null;
        }
        SolutionRule oldRule = solutionRuleDao.findOne(r.getId());
        if(oldRule == null){
            return null;
        }
        oldRule.setType(r.getType());
        oldRule.setConditions(r.getConditions());
        oldRule.setModified(System.currentTimeMillis());
        oldRule.setModifiedBy(userId);
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION, Constants.REVISION_ACTION_EDIT_RULE, r.getId());
        oldRule.setRevision(revision.getVersion());
        Solution solution = solutionDao.findOne(oldRule.getSolutionId());
        String rootParentId = solution.getRootParentId();
        if(!rootParentId.equals(solution.getId())){
            Solution rootSolution = solutionDao.findOne(rootParentId);
            rootSolution.setRevision(revision.getVersion());
            solutionDao.save(rootSolution);
        }else{
            solution.setRevision(revision.getVersion());
        }
        solutionRuleDao.save(oldRule);
        return SolutionConverter.convertRule(oldRule);
    }
    public SolutionRuleDTO createRule(SolutionRule r, String userId){
        r.setId(UUID.randomUUID().toString());
        r.setCreated(System.currentTimeMillis());
        r.setModified(System.currentTimeMillis());
        r.setStatus(Status.ACTIVE.getValue());
        r.setCreatedBy(userId);
        r.setModifiedBy(userId);
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION, Constants.REVISION_ACTION_ADD_RULE, r.getId());
        r.setRevision(revision.getVersion());

        Solution solution = solutionDao.findOne(r.getSolutionId());
        if(solution.getRules() == null){
            solution.setRules(new ArrayList<SolutionRule>());
        }
        solution.getRules().add(r);
        String rootParentId = solution.getRootParentId();
        if(!rootParentId.equals(solution.getId())){
            Solution rootSolution = solutionDao.findOne(rootParentId);
            rootSolution.setRevision(revision.getVersion());
            solutionDao.save(rootSolution);
        }else{
            solution.setRevision(revision.getVersion());
        }
        solutionDao.save(solution);
        solutionRuleDao.save(r);

        return SolutionConverter.convertRule(r);
    }
    public ErrorCodeMap validate(Solution s){
        if(s == null || StringUtils.isBlank(s.getName())
                || (!s.isGroup() && StringUtils.isBlank(s.getDesc()))
                || (!s.isGroup() && StringUtils.isBlank(s.getParentId()))){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else if(StringUtils.isNotBlank(s.getParentId())){
            Solution solution = solutionDao.findOne(s.getParentId());
            if(solution == null){
                return ErrorCodeMap.FAILURE_SOLUTION_NOT_FOUND;
            }
        }
        List<Solution> solutions = solutionDao.getActiveByName(s.getName(), s.isGroup());
        for (Solution solution : solutions) {
            if(!solution.getId().equals(s.getId())){
                return ErrorCodeMap.FAILURE_DUPLICATED_NAME;
            }
        }
        return null;
    }
    public List<SolutionDTO> getList(Long revision){
        return SolutionConverter.convert(solutionDao.getTrees(revision));
    }
    public SolutionDTO delete(String sId, String userId){
        Solution s = solutionDao.findOne(sId);
        if(s != null){
            s.setStatus(Status.DEACTIVE.getValue());
            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION, Constants.REVISION_ACTION_DELETE, s.getId());
            s.setRevision(revision.getVersion());
            s.setMyRevision(revision.getVersion());
            s.setModifiedBy(userId);
            s.setModified(System.currentTimeMillis());
            String rootParentId = s.getRootParentId();
            if(!rootParentId.equals(s.getId())){
                Solution rootSolution = solutionDao.findOne(rootParentId);
                rootSolution.setRevision(revision.getVersion());
                solutionDao.save(rootSolution);
            }
            if(StringUtils.isNotBlank(s.getParentId())){
                Solution parent = solutionDao.findOne(s.getParentId());
                if(parent != null && parent.getSolutions() != null && parent.getSolutions().size() > 0){
                    for (int i = parent.getSolutions().size() - 1; i >= 0 ; i--) {
                        if(parent.getSolutions().get(i).getId().equals(sId)){
                            parent.getSolutions().remove(i);
                        }
                    }
                    solutionDao.save(parent);
                }
            }
            s.setRules(null);
            solutionDao.save(s);
            return SolutionConverter.convert(s);
        }else{
            return null;
        }
    }

    /*public Map<String, Object> batchUpload(Solution solution, String userId){
        Map<String, Object> output = new HashMap<String, Object>();
        ErrorCodeMap errorCode = null;
        Long errorLine = new Long(-1);
        if(solution == null || StringUtils.isBlank(solution.getAttr()) || !solution.isGroup()){
            errorCode = ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else{
            Solution oldSolution = solutionDao.getOneActiveByAttr(solution.getAttr().trim(), true);
            Map<String, Question> descQuestionMap = new HashMap<String, Question>();
            if(errorCode == null){
                long idx = 1;
                for (Solution s : oldSolution.getSolutions()) {
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
    }*/
    public SolutionDTO update(Solution s, String userId){
        if(StringUtils.isBlank(s.getId())){
            return null;
        }
        Solution oldSolution = solutionDao.findOne(s.getId());
        if(oldSolution == null){
            return null;
        }
        oldSolution.setModified(System.currentTimeMillis());
        oldSolution.setModifiedBy(userId);
        oldSolution.setDesc(s.getDesc());
        oldSolution.setName(s.getName());
        oldSolution.setInfoUrl(s.getInfoUrl());
        oldSolution.setCaseStudy(s.getCaseStudy());
        oldSolution.setVideo(s.getVideo());
        oldSolution.setTrial(s.getTrial());
        oldSolution.setPrecedenceIds(s.getPrecedenceIds());
        oldSolution.setGroup(s.isGroup());
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION, Constants.REVISION_ACTION_EDIT, oldSolution.getId());
        oldSolution.setRevision(revision.getVersion());
        oldSolution.setMyRevision(revision.getVersion());
        String rootParentId = oldSolution.getRootParentId();
        if(!rootParentId.equals(oldSolution.getId())){
            Solution rootSolution = solutionDao.findOne(rootParentId);
            rootSolution.setRevision(revision.getVersion());
            solutionDao.save(rootSolution);
        }
        solutionDao.save(oldSolution);
        return SolutionConverter.convert(oldSolution);
    }
    public SolutionDTO create(Solution s, String userId){
        s.setId(UUID.randomUUID().toString());
        s.setCreated(System.currentTimeMillis());
        s.setModified(System.currentTimeMillis());
        s.setStatus(Status.ACTIVE.getValue());
        s.setCreatedBy(userId);
        s.setModifiedBy(userId);
        if(StringUtils.isNotBlank(s.getParentId())){
            Solution parent = solutionDao.findOne(s.getParentId());
            if(parent != null){
                if(parent.getSolutions() == null){
                    parent.setSolutions(new ArrayList<Solution>());
                }
                parent.getSolutions().add(s);
                if(StringUtils.isNotBlank(parent.getRootParentId())){
                    s.setRootParentId(parent.getRootParentId());
                }else{
                    s.setRootParentId(s.getParentId());
                }
                solutionDao.save(parent);
            }
        }
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION, Constants.REVISION_ACTION_ADD, s.getId());
        s.setRevision(revision.getVersion());
        s.setMyRevision(revision.getVersion());
        solutionDao.save(s);
        return SolutionConverter.convert(s);
    }

    public void updateSolutionRuleFromDeletedQuestions(Collection<String> qIDs){
        List<SolutionRule> rules = solutionRuleDao.getRulesByQuestionIds(qIDs);
        if(rules != null && rules.size() > 0){
            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION, Constants.REVISION_ACTION_DELETE_QUESTION, qIDs);
            for (SolutionRule rule : rules) {
                rule.setStatus(Status.DEACTIVE.getValue());
                rule.setRevision(revision.getVersion());
                solutionRuleDao.save(rule);
                removeRuleFromSolution(rule.getId(), rule.getSolutionId(), revision);
            }
        }
    }

    @Autowired
    private SolutionDao solutionDao;
    @Autowired
    private RevisionDao revisionDao;
    @Autowired
    private SolutionRuleDao solutionRuleDao;
    @Autowired
    private QuestionDao questionDao;
}
