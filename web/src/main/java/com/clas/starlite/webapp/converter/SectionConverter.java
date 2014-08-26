package com.clas.starlite.webapp.converter;
import com.clas.starlite.common.Status;
import com.clas.starlite.domain.Question;
import com.clas.starlite.domain.Section;
import com.clas.starlite.webapp.dto.QuestionDTO;
import com.clas.starlite.webapp.dto.SectionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Son on 8/19/14.
 */
public class SectionConverter {

    public static SectionDTO convert(Section section){
        SectionDTO dto = new SectionDTO(section.getId(), section.getName(), section.getNameMap(), section.getCreated(), section.getCreatedBy(), section.getModifiedBy(), section.getModified(), section.getStatus(), section.getRevision());
        if(section.getQuestions() != null){
            dto.setQuestions(new ArrayList<QuestionDTO>());
            for(Question question: section.getQuestions()){
                if(question.getStatus() == Status.ACTIVE.getValue()){
                    dto.getQuestions().add(QuestionConverter.convert(question));
                }
            }
        }
        if(section.getScenarios() != null){
            dto.setScenarios(ScenarioConverter.convert(section.getScenarios()));
        }
        return dto;
    }
    public static List<SectionDTO> convert(List<Section> sections){
        List<SectionDTO> output = new ArrayList<SectionDTO>();
        for (Section section : sections) {
            if(section.getStatus() == Status.ACTIVE.getValue()){
                output.add(convert(section));
            }
        }
        return output;
    }
}
