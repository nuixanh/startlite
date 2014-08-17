package com.clas.starlite.webapp.converter;

import com.clas.starlite.domain.Solution;
import com.clas.starlite.webapp.dto.SolutionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
public class SolutionConverter {
    public static SolutionDTO convert(Solution solution){
        SolutionDTO dto = new SolutionDTO(solution.getId(), solution.getDesc(), solution.getAttr(), solution.getParentId(), solution.isGroup(), solution.getCreated(), solution.getModified(), solution.getStatus(), solution.getRevision());
        if(solution.getSolutions() != null){
            List<SolutionDTO> solutionDTOs = new ArrayList<SolutionDTO>();
            for (Solution s : solution.getSolutions()) {
                solutionDTOs.add(convert(s));
            }
            dto.setSolutions(solutionDTOs);
        }
        return dto;
    }
    public static List<SolutionDTO> convert(List<Solution> solutions){
        List<SolutionDTO> dtos = new ArrayList<SolutionDTO>();
        for (Solution solution : solutions) {
            dtos.add(convert(solution));
        }
        return dtos;
    }
}
