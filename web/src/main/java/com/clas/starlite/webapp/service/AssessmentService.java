package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.dao.*;
import com.clas.starlite.domain.*;
import com.clas.starlite.util.CommonUtils;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.AssessmentInstanceDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by sonnt4 on 21/1/2015.
 */
@Component
public class AssessmentService {
    private Map<String, Object> validateScenario(AssessmentInstanceDTO.Scenario dtoScenario, Map<String, Scenario> scMap, Map<String, Section> sectionMap, Map<String, Question> questionMap){
        Map<String, Object> output = new HashMap<String, Object>();
        if(dtoScenario == null){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND);
        }
        Set<String> scIDs = new HashSet<String>();
        Set<String> sIDs = new HashSet<String>();
        scIDs.add(dtoScenario.getId());
        if(CollectionUtils.isNotEmpty(dtoScenario.getScenario())){
            for (AssessmentInstanceDTO.Scenario childDtoScenario : dtoScenario.getScenario()) {
                scIDs.add(childDtoScenario.getId());
            }
        }
        List<Scenario> scenarios = scenarioDao.find(scIDs);
        Scenario rootScenario = null;
        if(scenarios.size() != scIDs.size()){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND);
        }
        for (Scenario scenario : scenarios) {
            if(!scMap.containsKey(scenario.getId())){
                scMap.put(scenario.getId(), scenario);
            }
            if(scenario.getId().equals(dtoScenario.getId())){
                rootScenario = scenario;
            }
        }
        if(CollectionUtils.isNotEmpty(dtoScenario.getSection())){
            for (AssessmentInstanceDTO.Section childDtoSection : dtoScenario.getSection()) {
                sIDs.add(childDtoSection.getId());
            }
            List<Section> sections = sectionDao.find(sIDs);
            if(sections.size() != sIDs.size()){
                return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SECTION_NOT_FOUND);
            }
            for (Section section : sections) {
                if(!sectionMap.containsKey(section.getId())){
                    sectionMap.put(section.getId(), section);
                }
                if(CollectionUtils.isNotEmpty(section.getQuestions())){
                    for (Question question : section.getQuestions()) {
                        if(!questionMap.containsKey(question.getId())){
                            questionMap.put(question.getId(), question);
                        }
                    }
                }
            }
            Set<String> sectionHistories = CommonUtils.newHashSet();
            for (AssessmentInstanceDTO.Section childDtoSection : dtoScenario.getSection()) {
                Section section = sectionMap.get(childDtoSection.getId());
                if(section == null){
                    return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SECTION_NOT_FOUND);
                }
                List<AssessmentInstanceDTO.Question> dtoQuestions = childDtoSection.getQuestion();
                for(AssessmentInstanceDTO.Question dtoQuestion: dtoQuestions){
                    Question question = questionMap.get(dtoQuestion.getQuestionId());
                    if(question == null){
                        return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_QUESTION_NOT_FOUND);
                    }
                    if(!section.getId().equals(question.getSectionId())){
                        return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_INVALID_QUESTION);
                    }
                    Map<String, Answer> answerMap = new HashMap<String, Answer>();
                    for (Answer answer : question.getAnswers()) {
                        answerMap.put(answer.getId(), answer);
                    }
                    if(CollectionUtils.isNotEmpty(dtoQuestion.getChosenAnswer())){
                        for(AssessmentInstanceDTO.Answer dtoAnswers: dtoQuestion.getChosenAnswer()){
                            if(!answerMap.containsKey(dtoAnswers.getId())){
                                return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_INVALID_QUESTION);
                            }
                        }
                    }
                }
                SectionHistory sectionHistory = sectionService.snapshotSection(section);
                sectionHistories.add(sectionHistory.getHistoryId());
            }
            ScenarioHistory rootScenarioHistory = scenarioHistoryDao.snapshotScenario(rootScenario);
            if(CollectionUtils.isNotEmpty(sectionHistories)){
                rootScenarioHistory.setSectionHistories(sectionHistories);
                scenarioHistoryDao.save(rootScenarioHistory);
            }
        }
        if(CollectionUtils.isNotEmpty(dtoScenario.getScenario())){
            for (AssessmentInstanceDTO.Scenario childDtoScenario : dtoScenario.getScenario()) {
                Map<String, Object> otherOutput = validateScenario(childDtoScenario, scMap, sectionMap, questionMap);
                ErrorCodeMap errorCode = (ErrorCodeMap) otherOutput.get(Constants.ERROR_CODE);
                if(errorCode != null){
                    return otherOutput;
                }
            }
        }
        Assessment ass = new Assessment();
        output.put(Constants.DATA, ass);
        return output;
    }
    public Map<String, Object> score(AssessmentInstanceDTO dto){
        Map<String, Object> output = new HashMap<String, Object>();
        if(!CommonUtils.isValidEmail(dto.getCustomerEmail())){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_INVALID_EMAIL);
        }
        User user = userDao.findOne(dto.getUserId());
        if(user == null){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_USER_NOT_FOUND);
        }
        List<Solution> solutions = null;
        List<AssessmentInstanceDTO.Solution> dtoSolutions = dto.getSolution();
        if(CollectionUtils.isNotEmpty(dtoSolutions)){
            Set<String> slIDs = new HashSet<String>();
            for (AssessmentInstanceDTO.Solution dtoSolution : dtoSolutions) {
                slIDs.add(dtoSolution.getId());
            }
            solutions = solutionDao.find(slIDs);
            if(solutions.size() != slIDs.size()){
                return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SOLUTION_NOT_FOUND);
            }
        }
        if(CollectionUtils.isEmpty(solutions)){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SOLUTION_NOT_FOUND);
        }
        Map<String, Scenario> scMap = new HashMap<String, Scenario>();
        Map<String, Section> sectionMap = new HashMap<String, Section>();
        Map<String, Question> questionMap = new HashMap<String, Question>();
        AssessmentInstanceDTO.Scenario dtoScenario = dto.getScenario();
        Map<String, Object> validateResult = validateScenario(dtoScenario, scMap, sectionMap, questionMap);
        ErrorCodeMap errorCode = (ErrorCodeMap) validateResult.get(Constants.ERROR_CODE);
        if(errorCode != null){
            return validateResult;
        }

        List<SolutionHistory> solutionHistories = CommonUtils.newArrayList();
        for (Solution solution : solutions) {
            SolutionHistory solutionHistory = solutionHistoryDao.snapshotSolution(solution);
            solutionHistories.add(solutionHistory);
        }
        Assessment ass = (Assessment) validateResult.get(Constants.DATA);
        ass.setSolutionHistories(solutionHistories);
        ass.setScoreDate(dto.getTimeStamp());

        assessmentDao.save(ass);
        output.put(Constants.DATA, ass);
        return output;
    }
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionHistoryDao questionHistoryDao;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private SolutionDao solutionDao;
    @Autowired
    private SolutionHistoryDao solutionHistoryDao;
    @Autowired
    private AssessmentDao assessmentDao;
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private ScenarioHistoryDao scenarioHistoryDao;
    @Autowired
    private SectionDao sectionDao;
}
