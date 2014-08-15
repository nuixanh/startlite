package com.clas.starlite.webapp.service;

import com.clas.starlite.dao.QuestionDao;
import com.clas.starlite.dao.ScenarioDao;
import com.clas.starlite.domain.Scenario;
import com.clas.starlite.webapp.converter.ScenarioConverter;
import com.clas.starlite.webapp.dto.ScenarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonnt4 on 8/15/2014.
 */
@Component
public class ScenarioService {
    public List<ScenarioDTO> getList(String scenario, Long timestamp){
        List<ScenarioDTO> output;
        try {
            List<Scenario> scenarios = scenarioDao.findSelfAndChildren(scenario, true, timestamp);
            output = ScenarioConverter.convert(scenarios);
        } catch (Exception e) {
            e.printStackTrace();
            output = new ArrayList<ScenarioDTO>();
        }
        return output;
    }
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private QuestionDao questionDao;
}
