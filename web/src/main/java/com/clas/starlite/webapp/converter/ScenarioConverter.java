package com.clas.starlite.webapp.converter;

import com.clas.starlite.domain.Scenario;
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
            ScenarioDTO dto = new ScenarioDTO(scenario.getId(), scenario.getName(), scenario.getModified(), scenario.isSection(), scenario.getParentId());
            output.add(dto);
        }
        return output;
    }
}
