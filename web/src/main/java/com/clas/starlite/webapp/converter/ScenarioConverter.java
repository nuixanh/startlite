package com.clas.starlite.webapp.converter;

import com.clas.starlite.common.Status;
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
            output.add(convert(scenario));
            /*if(scenario.getStatus() == Status.ACTIVE.getValue()){
                output.add(convert(scenario));
            }*/
        }
        return output;
    }
    public static ScenarioDTO convert(Scenario scenario){
        ScenarioDTO dto = new ScenarioDTO(scenario.getId(), scenario.getName(), scenario.getModified(), scenario.getParentId(), scenario.getRevision(), scenario.getStatus(), scenario.getRootParentId(), scenario.getSections(), scenario.getSectionMap());
        if(scenario.getScenarios() != null){
            dto.setScenarios(new ArrayList<ScenarioDTO>());
            for (Scenario sc : scenario.getScenarios()) {
                if(sc.getStatus() == Status.ACTIVE.getValue()){
                    dto.getScenarios().add(convert(sc));
                }
            }
        }
        return dto;
    }
}
