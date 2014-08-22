package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.SolutionDao;
import com.clas.starlite.dao.SolutionRuleDao;
import com.clas.starlite.domain.*;
import com.clas.starlite.webapp.common.ErrorCodeMap;
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

        for (RuleCondition condition : rule.getConditions()) {
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
        return null;
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
        if(s == null || StringUtils.isBlank(s.getDesc())
                || (!s.isGroup() && StringUtils.isBlank(s.getAttr()))
                || (!s.isGroup() && StringUtils.isBlank(s.getParentId()))){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else if(StringUtils.isNotBlank(s.getParentId())){
            Solution solution = solutionDao.findOne(s.getParentId());
            if(solution == null){
                return ErrorCodeMap.FAILURE_SOLUTION_NOT_FOUND;
            }
        }
        return null;
    }
    public List<SolutionDTO> getList(Long revision){
        return SolutionConverter.convert(solutionDao.getTrees(revision));
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

    @Autowired
    private SolutionDao solutionDao;
    @Autowired
    private RevisionDao revisionDao;
    @Autowired
    private SolutionRuleDao solutionRuleDao;
    @Autowired
    private QuestionDao questionDao;
}
