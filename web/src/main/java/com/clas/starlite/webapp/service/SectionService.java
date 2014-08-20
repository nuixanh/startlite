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

    public List<SectionDTO> getList(String sectionId, Long revision){
        List<SectionDTO> output;
        try {
            Set<String> sIdSet = new HashSet<String>();
            if(revision != null && revision > 0){
                List<RevisionHistory> rHistoryList = revisionDao.getHistory(Constants.REVISION_TYPE_QUESTION, revision);
                if(rHistoryList.size() > 0){
                    Set<String> entityIdSet = new HashSet<String>();
                    for (RevisionHistory rHistory : rHistoryList) {
                        entityIdSet.add(rHistory.getEntityId());
                    }
                    List<Question> qList = questionDao.find(entityIdSet);
                    for (Question q : qList) {
                        sIdSet.add(q.getSectionId());
                    }
                }
                rHistoryList = revisionDao.getHistory(Constants.REVISION_TYPE_SECTION, Constants.REVISION_ACTION_ADD, revision);
                if(rHistoryList.size() > 0){
                    for (RevisionHistory rHistory : rHistoryList) {
                        sIdSet.add(rHistory.getEntityId());
                    }
                }

            }else if(StringUtils.isNotBlank(sectionId)){
                sIdSet.add(sectionId);
            }
            List<Section> sections = sectionDao.find(sIdSet);
            output = SectionConverter.convert(sections);
        } catch (Exception e) {
            e.printStackTrace();
            output = new ArrayList<SectionDTO>();
        }
        return output;
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
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SECTION, Constants.REVISION_ACTION_ADD, section.getId());
        section.setRevision(revision.getVersion());

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
        Scenario scenario = scenarioDao.findOne(scenarioId);
        if(scenario == null){
            return ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND;
        }
        List<Scenario> parents = section.getScenarios();
        if(parents != null && parents.size() > 0){
            for (Scenario parent : parents) {
                if(parent.getRootParentId().equals(scenario.getRootParentId())){
                    return ErrorCodeMap.FAILURE_SECTION_BELONG_SAME_ROOT_SCENARIO;
                }
            }
        }

        if(scenario.getSections() == null){
            scenario.setSections(new HashSet<String>());
        }
        scenario.getSections().add(section.getId());

        if(section.getScenarios() == null){
            section.setScenarios(new ArrayList<Scenario>());
        }
        section.getScenarios().add(scenario);
        section.setModified(System.currentTimeMillis());
        section.setModifiedBy(userId);
        Revision revision = revisionDao.incVersion(Constants.REVISION_TYPE_SECTION, Constants.REVISION_ACTION_ATTACH, section.getId());
        section.setRevision(revision.getVersion());

        sectionDao.save(section);
        scenarioDao.save(scenario);

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
