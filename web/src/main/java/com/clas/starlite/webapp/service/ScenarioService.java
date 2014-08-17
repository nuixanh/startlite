package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.Status;
import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.RevisionDao;
import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.domain.Revision;
import com.clas.starlite.domain.Scenario;
import com.clas.starlite.webapp.converter.ScenarioConverter;
import com.clas.starlite.webapp.dto.ScenarioDTO;
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
    public List<ScenarioDTO> getList(String scenarioId, Long timestamp){
        List<ScenarioDTO> output;
        try {
            List<Scenario> scenarios = scenarioDao.findSelfAndChildren(scenarioId, Status.ACTIVE.getValue(), timestamp);
            output = ScenarioConverter.convert(scenarios);
        } catch (Exception e) {
            e.printStackTrace();
            output = new ArrayList<ScenarioDTO>();
        }
        return output;
    }
    public ScenarioDTO create(Scenario scenario){
        scenario.setId(UUID.randomUUID().toString());
        scenario.setCreated(System.currentTimeMillis());
        scenario.setModified(System.currentTimeMillis());
        scenario.setStatus(Status.ACTIVE.getValue());
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SCENARIO, Constants.REVISION_ACTION_ADD, scenario.getId());
        scenario.setRevision(revision.getVersion());
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
