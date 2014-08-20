package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.SolutionDao;
import com.clas.starlite.dao.SolutionRuleDao;
import com.clas.starlite.domain.Revision;
import com.clas.starlite.domain.Solution;
import com.clas.starlite.domain.SolutionRule;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.SolutionConverter;
import com.clas.starlite.webapp.dto.SolutionDTO;
import com.clas.starlite.webapp.dto.SolutionRuleDTO;
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
public class SolutionService {
    public ErrorCodeMap validateRule(SolutionRule rule){
        if(rule == null || StringUtils.isBlank(rule.getSolutionId()) || rule.getConditions() == null
                || rule.getConditions().size() == 0){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else if(StringUtils.isNotBlank(rule.getSolutionId())){
            Solution s = solutionDao.findOne(rule.getSolutionId());
            if(s == null){
                return ErrorCodeMap.FAILURE_SOLUTION_NOT_FOUND;
            }
        }
        return null;
    }
    public SolutionRuleDTO createRule(SolutionRule r, String userId){
        r.setId(UUID.randomUUID().toString());
        r.setCreated(System.currentTimeMillis());
        r.setModified(System.currentTimeMillis());
        r.setStatus(Status.ACTIVE.getValue());
        r.setCreatedBy(userId);
        r.setModifiedBy(userId);
        Solution solution = solutionDao.findOne(r.getSolutionId());
        if(solution.getRules() == null){
            solution.setRules(new ArrayList<SolutionRule>());
        }
        solution.getRules().add(r);
        solutionDao.save(solution);
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SOLUTION_RULE, Constants.REVISION_ACTION_ADD, r.getId());
        r.setRevision(revision.getVersion());
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
    public List<SolutionDTO> getList(String solutionId, Long revision){
        return SolutionConverter.convert(solutionDao.getTree(solutionId, revision));
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
        solutionDao.save(s);
        return SolutionConverter.convert(s);
    }

    @Autowired
    private SolutionDao solutionDao;
    @Autowired
    private RevisionDao revisionDao;
    @Autowired
    private SolutionRuleDao solutionRuleDao;
}
