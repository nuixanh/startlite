package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.domain.Revision;
import com.clas.starlite.domain.Scenario;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.converter.ScenarioConverter;
import com.clas.starlite.webapp.dto.ScenarioDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sonnt4 on 8/15/2014.
 */
@Component
public class ScenarioService {
    public ErrorCodeMap validate(Scenario sc){
        if(sc == null || StringUtils.isBlank(sc.getName())){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }else if(StringUtils.isNotBlank(sc.getParentId())){
            Scenario scenario = scenarioDao.findOne(sc.getParentId());
            if(scenario == null){
                return ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND;
            }
        }
        return null;
    }
    public List<ScenarioDTO> getList(String scenarioId, Long revision){
        List<ScenarioDTO> output;
        try {
            List<Scenario> scenarios = scenarioDao.getTree(scenarioId, revision);
            output = ScenarioConverter.convert(scenarios);
        } catch (Exception e) {
            e.printStackTrace();
            output = new ArrayList<ScenarioDTO>();
        }
        return output;
    }
    public ScenarioDTO create(Scenario scenario, String userId){
        scenario.setId(UUID.randomUUID().toString());
        scenario.setCreated(System.currentTimeMillis());
        scenario.setModified(System.currentTimeMillis());
        scenario.setCreatedBy(userId);
        scenario.setModifiedBy(userId);
        scenario.setStatus(Status.ACTIVE.getValue());
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_ADD, scenario.getId());
        scenario.setRevision(revision.getVersion());
        if(StringUtils.isNotBlank(scenario.getParentId())){
            Scenario parent = scenarioDao.findOne(scenario.getParentId());
            if(parent != null){
                if(parent.getScenarios() == null){
                    parent.setScenarios(new ArrayList<Scenario>());
                }
                parent.getScenarios().add(scenario);
                scenarioDao.save(parent);
            }
        }
        scenarioDao.save(scenario);
        return ScenarioConverter.convert(scenario);
    }
    public ScenarioDTO update(Scenario sc){
        Scenario scenario = scenarioDao.findOne(sc.getId());
        if(scenario != null){
            scenario.setName(sc.getName());
            scenario.setModifiedBy(sc.getModifiedBy());
            scenario.setModified(System.currentTimeMillis());
            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_EDIT, sc.getId());
            scenario.setRevision(revision.getVersion());
            scenarioDao.save(scenario);
            return ScenarioConverter.convert(scenario);
        }else{
            return null;
        }
    }
    public ScenarioDTO delete(String scenarioId, String userId){
        Scenario scenario = scenarioDao.findOne(scenarioId);
        if(scenario != null){
            scenario.setStatus(Status.DEACTIVE.getValue());
            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_DELETE, scenario.getId());
            scenario.setRevision(revision.getVersion());
            scenario.setModifiedBy(userId);
            scenario.setModified(System.currentTimeMillis());
            scenarioDao.save(scenario);
            return ScenarioConverter.convert(scenario);
        }else{
            return null;
        }
    }
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private RevisionDao revisionDao;
}
