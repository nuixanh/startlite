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
        return null;
    }
    public SectionDTO create(Section section, String userId){
        section.setId(UUID.randomUUID().toString());
        section.setCreated(System.currentTimeMillis());
        section.setModified(System.currentTimeMillis());
        section.setCreatedBy(userId);
        section.setModifiedBy(userId);
        section.setStatus(Status.ACTIVE.getValue());
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_QUESTION, Constants.REVISION_ACTION_ADD_SECTION, section.getId());
        section.setRevision(revision.getVersion());
        section.setMyRevision(revision.getVersion());

        sectionDao.save(section);
        return SectionConverter.convert(section);
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
}
