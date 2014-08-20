package com.clas.starlite.webapp.converter;

import com.clas.starlite.domain.RuleCondition;
import com.clas.starlite.domain.Solution;
import com.clas.starlite.domain.SolutionRule;
import com.clas.starlite.webapp.dto.RuleConditionDTO;
import com.clas.starlite.webapp.dto.SolutionDTO;
import com.clas.starlite.webapp.dto.SolutionRuleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Son on 8/17/14.
 */
public class SolutionConverter {
    public static SolutionDTO convert(Solution solution){
        if(solution == null) return null;
        SolutionDTO dto = new SolutionDTO(solution.getId(), solution.getDesc(), solution.getAttr(), solution.getParentId(), solution.isGroup(), solution.getCreated(), solution.getModified(), solution.getStatus(), solution.getRevision(), solution.getRootParentId());
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
        if(solutions == null) return null;
        List<SolutionDTO> dtos = new ArrayList<SolutionDTO>();
        for (Solution solution : solutions) {
            dtos.add(convert(solution));
        }
        return dtos;
    }

    public static RuleConditionDTO convertCondition(RuleCondition ruleCondition){
        RuleConditionDTO dto = new RuleConditionDTO(ruleCondition.getId(), ruleCondition.getSolutionRuleId(), ruleCondition.getQuestionId(), ruleCondition.getScoreList(), ruleCondition.getOperator(), ruleCondition.getCreated(), ruleCondition.getModified());
        return dto;
    }
    public static List<RuleConditionDTO> convertConditions(List<RuleCondition> conditions){
        if(conditions == null) return null;

        List<RuleConditionDTO> dtos = new ArrayList<RuleConditionDTO>();
        for (RuleCondition condition : conditions) {
            dtos.add(convertCondition(condition));
        }
        return dtos;
    }
    public static SolutionRuleDTO convertRule(SolutionRule rule){
        if(rule == null) return null;
        SolutionRuleDTO dto = new SolutionRuleDTO(rule.getId(), rule.getType(), rule.getSolutionId(), rule.getCreated(), rule.getCreatedBy(), rule.getModifiedBy(), rule.getModified(), rule.getStatus(), rule.getRevision(), convertConditions(rule.getConditions()));
        return dto;
    }
}
