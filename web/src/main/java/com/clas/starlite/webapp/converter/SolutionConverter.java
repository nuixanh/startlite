package com.clas.starlite.webapp.converter;

import com.clas.starlite.common.Status;
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
            dto.setSolutions(new ArrayList<SolutionDTO>());
            for (Solution childSol : solution.getSolutions()) {
                if(childSol.getStatus() == Status.ACTIVE.getValue()){
                    dto.getSolutions().add(convert(childSol));
                }
            }
        }
        dto.setSolutions(convert(solution.getSolutions()));
        dto.setRules(convertRules(solution.getRules()));
        return dto;
    }
    public static List<SolutionDTO> convert(List<Solution> solutions){
        if(solutions == null) return null;
        List<SolutionDTO> dtos = new ArrayList<SolutionDTO>();
        for (Solution solution : solutions) {
            dtos.add(convert(solution));
            /*if(solution.getStatus() == Status.ACTIVE.getValue()){
                dtos.add(convert(solution));
            }*/
        }
        return dtos;
    }

    public static RuleConditionDTO convertCondition(RuleCondition ruleCondition){
        RuleConditionDTO dto = new RuleConditionDTO(ruleCondition.getQuestionId(), ruleCondition.getAnswerIds());
        return dto;
    }
    public static List<List<RuleConditionDTO>> convertConditions(List<List<RuleCondition>> conditions){
        if(conditions == null) return null;

        List<List<RuleConditionDTO>> dtos = new ArrayList<List<RuleConditionDTO>>();
        for (List<RuleCondition> conditionList : conditions) {
            if(conditionList != null && conditionList.size() > 0){
                List<RuleConditionDTO> dtoConditionList = new ArrayList<RuleConditionDTO>();
                for (RuleCondition condition : conditionList) {
                    dtoConditionList.add(convertCondition(condition));
                }
                dtos.add(dtoConditionList);
            }
        }
        return dtos;
    }
    public static SolutionRuleDTO convertRule(SolutionRule rule){
        if(rule == null) return null;
        SolutionRuleDTO dto = new SolutionRuleDTO(rule.getId(), rule.getType(), rule.getSolutionId(), rule.getCreated(), rule.getCreatedBy(), rule.getModifiedBy(), rule.getModified(), rule.getStatus(), rule.getRevision(), convertConditions(rule.getConditions()));
        return dto;
    }
    public static List<SolutionRuleDTO> convertRules(List<SolutionRule> rules){
        if(rules == null) return null;
        List<SolutionRuleDTO> dtos = new ArrayList<SolutionRuleDTO>();
        for (SolutionRule rule : rules) {
            if(rule.getStatus() == Status.ACTIVE.getValue()){
                dtos.add(convertRule(rule));
            }
        }
        return dtos;
    }
}
