package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
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
        if(StringUtils.isBlank(sc.getParentId())){
            List<Scenario> scenarios = scenarioDao.getActiveByName(sc.getName().trim());
            for (Scenario scenario : scenarios) {
                if(!scenario.getId().equals(sc.getId())){
                    return ErrorCodeMap.FAILURE_DUPLICATED_NAME;
                }
            }
        }

        return null;
    }
    public ErrorCodeMap validateForUpdate(Scenario sc){
        if(sc == null || StringUtils.isBlank(sc.getName()) || StringUtils.isBlank(sc.getId())){
            return ErrorCodeMap.FAILURE_INVALID_PARAMS;
        }
        if(StringUtils.isBlank(sc.getParentId())){
            List<Scenario> scenarios = scenarioDao.getActiveByName(sc.getName().trim());
            for (Scenario scenario : scenarios) {
                if(!scenario.getId().equals(sc.getId())){
                    return ErrorCodeMap.FAILURE_DUPLICATED_NAME;
                }
            }
        }
        return null;
    }
    public List<ScenarioDTO> getList(Long revision){
        List<Scenario> scenarios;
        try {
            scenarios = scenarioDao.getTrees(revision);
        } catch (Exception e) {
            e.printStackTrace();
            scenarios = new ArrayList<Scenario>();
        }
        return ScenarioConverter.convert(scenarios);
    }
    public ScenarioDTO create(Scenario scenario, String userId){
        scenario.setId(UUID.randomUUID().toString());
        scenario.setCreated(System.currentTimeMillis());
        scenario.setModified(System.currentTimeMillis());
        scenario.setCreatedBy(userId);
        scenario.setModifiedBy(userId);
        scenario.setName(scenario.getName().trim());
        scenario.setStatus(Status.ACTIVE.getValue());
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_ADD, scenario.getId());
        scenario.setRevision(revision.getVersion());
        scenario.setMyRevision(revision.getVersion());
        if(StringUtils.isNotBlank(scenario.getParentId())){
            Scenario parent = scenarioDao.findOne(scenario.getParentId());
            if(parent != null){
                if(parent.getScenarios() == null){
                    parent.setScenarios(new ArrayList<Scenario>());
                }
                parent.getScenarios().add(scenario);
                scenario.setRootParentId(parent.getRootParentId());

                String rootParentId = parent.getRootParentId();
                if(!rootParentId.equals(parent.getId())){
                    Scenario rootScenario = scenarioDao.findOne(rootParentId);
                    rootScenario.setRevision(revision.getVersion());
                    scenarioDao.save(rootScenario);
                }else{
                    parent.setRevision(revision.getVersion());
                }
                scenarioDao.save(parent);
            }
        }
        scenarioDao.save(scenario);
        return ScenarioConverter.convert(scenario);
    }
    public ScenarioDTO update(Scenario sc, String userId){
        Scenario scenario = scenarioDao.findOne(sc.getId());
        if(scenario != null){
            scenario.setName(sc.getName().trim());
            scenario.setModifiedBy(userId);
            scenario.setModified(System.currentTimeMillis());
            Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_EDIT, sc.getId());
            scenario.setRevision(revision.getVersion());
            scenario.setMyRevision(revision.getVersion());
            String rootParentId = scenario.getRootParentId();
            if(!rootParentId.equals(scenario.getId())){
                Scenario rootScenario = scenarioDao.findOne(rootParentId);
                rootScenario.setRevision(revision.getVersion());
                scenarioDao.save(rootScenario);
            }
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
            scenario.setMyRevision(revision.getVersion());
            scenario.setModifiedBy(userId);
            scenario.setModified(System.currentTimeMillis());
            String rootParentId = scenario.getRootParentId();
            if(!rootParentId.equals(scenario.getId())){
                Scenario rootScenario = scenarioDao.findOne(rootParentId);
                rootScenario.setRevision(revision.getVersion());
                scenarioDao.save(rootScenario);
            }
            if(StringUtils.isNotBlank(scenario.getParentId())){
                Scenario parent = scenarioDao.findOne(scenario.getParentId());
                if(parent != null && parent.getScenarios() != null && parent.getScenarios().size() > 0){
                    for (int i = parent.getScenarios().size() - 1; i >= 0 ; i--) {
                        if(parent.getScenarios().get(i).getId().equals(scenarioId)){
                            parent.getScenarios().remove(i);
                        }
                    }

                    scenarioDao.save(parent);
                }
            }
            scenarioDao.save(scenario);
            return ScenarioConverter.convert(scenario);
        }else{
            return null;
        }
    }
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private RevisionDao revisionDao;
}
