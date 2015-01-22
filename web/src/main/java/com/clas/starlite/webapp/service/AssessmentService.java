package com.clas.starlite.webapp.service;

import com.clas.starlite.common.Constants;
import com.clas.starlite.dao.*;
import com.clas.starlite.domain.*;
import com.clas.starlite.util.CommonUtils;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.AssessmentInstanceDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by sonnt4 on 21/1/2015.
 */
@Component
public class AssessmentService {
    public List<AssessmentInstanceDTO> getReport(String userId, Long revisionByUser) throws Exception{
        List<AssessmentInstanceDTO> output = CommonUtils.newArrayList();
        List<Assessment> assessments = assessmentDao.getByRevision(userId, revisionByUser);
        for (Assessment assessment : assessments) {
            AssessmentInstanceDTO dto = new AssessmentInstanceDTO();
            dto.setId(assessment.getId());
            dto.setUserId(assessment.getUserId());
            dto.setCustomerName(assessment.getCustomerName());
            dto.setCustomerEmail(assessment.getCustomerEmail());
            if(CollectionUtils.isNotEmpty(assessment.getSolutionHistories())){
                dto.setSolution(new ArrayList<AssessmentInstanceDTO.Solution>());
                for (SolutionHistory solutionHistory : assessment.getSolutionHistories()) {
                    AssessmentInstanceDTO.Solution solution = new AssessmentInstanceDTO.Solution();
                    BeanUtilsBean.getInstance().copyProperties(solution, solutionHistory);
                    dto.getSolution().add(solution);
                }
            }
            ScenarioHistory rootScenarioHistory = assessment.getRootScenarioHistory();
            AssessmentInstanceDTO.Scenario scenario = new AssessmentInstanceDTO.Scenario();
            BeanUtilsBean.getInstance().copyProperties(scenario, rootScenarioHistory);
            dto.setScenario(scenario);
            output.add(dto);
        }

        return output;
    }
    private Map<String, Object> validateScenarioAndBuildAssessment(AssessmentInstanceDTO.Scenario dtoScenario, Map<String, Scenario> scMap, Map<String, Section> sectionMap, Map<String, Question> questionMap, Map<String, Assessment.Score> scoreMap, boolean isRootScenario){
        Map<String, Object> output = new HashMap<String, Object>();
        if(dtoScenario == null){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND);
        }
        if(!scoreMap.containsKey(dtoScenario.getId())){
            Assessment.Score score = new Assessment.Score(dtoScenario.getId(), Constants.REVISION_TYPE_SCENARIO, dtoScenario.getPercent());
            scoreMap.put(dtoScenario.getId(), score);
        }
        Set<String> scIDs = new HashSet<String>();
        Set<String> sIDs = new HashSet<String>();
        scIDs.add(dtoScenario.getId());
        if(CollectionUtils.isNotEmpty(dtoScenario.getScenario())){
            for (AssessmentInstanceDTO.Scenario childDtoScenario : dtoScenario.getScenario()) {
                scIDs.add(childDtoScenario.getId());
                if(!scoreMap.containsKey(childDtoScenario.getId())){
                    Assessment.Score score = new Assessment.Score(childDtoScenario.getId(), Constants.REVISION_TYPE_SCENARIO, childDtoScenario.getPercent());
                    scoreMap.put(childDtoScenario.getId(), score);
                }
            }
        }
        List<Scenario> scenarios = scenarioDao.find(scIDs);
        Scenario rootScenario = null;
        ScenarioHistory rootScenarioHistory = null;
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
                if(!scoreMap.containsKey(childDtoSection.getId())){
                    Assessment.Score score = new Assessment.Score(childDtoSection.getId(), Constants.REVISION_TYPE_SECTION, childDtoSection.getPercent());
                    scoreMap.put(childDtoSection.getId(), score);
                }
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
                    if(!scoreMap.containsKey(dtoQuestion.getQuestionId())){
                        Assessment.Score score = new Assessment.Score(dtoQuestion.getQuestionId(), Constants.REVISION_TYPE_QUESTION, dtoQuestion.getPercent());
                        scoreMap.put(dtoQuestion.getQuestionId(), score);
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
            rootScenarioHistory = scenarioHistoryDao.snapshotScenario(rootScenario);
            if(CollectionUtils.isNotEmpty(sectionHistories)){
                rootScenarioHistory.setSectionHistories(sectionHistories);
                scenarioHistoryDao.save(rootScenarioHistory);
            }
        }
        if(CollectionUtils.isNotEmpty(dtoScenario.getScenario())){
            for (AssessmentInstanceDTO.Scenario childDtoScenario : dtoScenario.getScenario()) {
                Map<String, Object> otherOutput = validateScenarioAndBuildAssessment(childDtoScenario, scMap, sectionMap, questionMap, scoreMap, false);
                ErrorCodeMap errorCode = (ErrorCodeMap) otherOutput.get(Constants.ERROR_CODE);
                if(errorCode != null){
                    return otherOutput;
                }
            }
        }
        if(isRootScenario){
            Assessment ass = new Assessment();
            ass.setRootScenarioHistory(rootScenarioHistory);
            for (Assessment.Score score : scoreMap.values()) {
                ass.getScores().add(score);
            }
            output.put(Constants.DATA, ass);
        }
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
        Map<String, Scenario> scMap = CommonUtils.newHashMap();
        Map<String, Section> sectionMap = CommonUtils.newHashMap();
        Map<String, Question> questionMap = CommonUtils.newHashMap();
        Map<String, Assessment.Score> scoreMap = CommonUtils.newHashMap();
        AssessmentInstanceDTO.Scenario dtoScenario = dto.getScenario();
        Map<String, Object> validateResult = validateScenarioAndBuildAssessment(dtoScenario, scMap, sectionMap, questionMap, scoreMap, true);
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
        ass.setCustomerEmail(dto.getCustomerEmail());
        ass.setCustomerName(dto.getCustomerName());
        user = userDao.incSurveyCount(user);
        ass.setCountByUser(user.getSurveyCount());
        ass.setUserId(user.getId());
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
