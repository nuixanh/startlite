package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.SolutionDao;
import com.clas.starlite.domain.Revision;
import com.clas.starlite.domain.Solution;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.SolutionConverter;
import com.clas.starlite.webapp.dto.SolutionDTO;
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
}
