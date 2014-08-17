package com.clas.starlite.webapp.converter;

import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.Scenario;
import com.clas.starlite.webapp.dto.QuestionDTO;
import com.clas.starlite.webapp.dto.ScenarioDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonnt4 on 8/15/2014.
 */
public class ScenarioConverter {
    public static List<ScenarioDTO> convert(List<Scenario> scenarios){
        List<ScenarioDTO> output = new ArrayList<ScenarioDTO>();
        for (Scenario scenario : scenarios) {
            ScenarioDTO dto = convert(scenario);
            output.add(dto);
        }
        return output;
    }
    public static ScenarioDTO convert(Scenario scenario){
        ScenarioDTO dto = new ScenarioDTO(scenario.getId(), scenario.getName(), scenario.getModified(), scenario.isSection(), scenario.getParentId(), scenario.getRevision(), scenario.getStatus());
        if(scenario.getScenarios() != null){
            dto.setScenarios(new ArrayList<ScenarioDTO>());
            for (Scenario sc : scenario.getScenarios()) {
                dto.getScenarios().add(convert(sc));
            }
        }
        if(scenario.getQuestions() != null){
            dto.setQuestions(new ArrayList<QuestionDTO>());
            for (Question q : scenario.getQuestions()) {
                dto.getQuestions().add(QuestionConverter.convert(q));
            }
        }
        return dto;
    }
}
