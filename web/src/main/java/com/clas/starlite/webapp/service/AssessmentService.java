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
    private AssessmentInstanceDTO.Answer buildAnswerDTO(Answer answerHistory) throws Exception{
        AssessmentInstanceDTO.Answer answer = new AssessmentInstanceDTO.Answer();
        BeanUtilsBean.getInstance().copyProperties(answer, answerHistory);
        answer.setContent(answerHistory.getDesc());
        return answer;
    }
    private AssessmentInstanceDTO.Question buildQuestionDTO(QuestionHistory questionHistory, Assessment.Score score) throws Exception{
        AssessmentInstanceDTO.Question question = new AssessmentInstanceDTO.Question();
        BeanUtilsBean.getInstance().copyProperties(question, questionHistory);
        question.setQuestionId(questionHistory.getHistoryId());
        if(score != null && Constants.REVISION_TYPE_QUESTION.equals(score.getType()) && score.getEntityId().equals(questionHistory.getId())){
            question.setPercent(score.getScorePercent());
        }
        Map<String, AssessmentInstanceDTO.Answer> answerMap = CommonUtils.newHashMap();
        if(CollectionUtils.isNotEmpty(questionHistory.getAnswers())){
            question.setAllAnswers(new ArrayList<AssessmentInstanceDTO.Answer>());
            for (Answer answer : questionHistory.getAnswers()) {
                AssessmentInstanceDTO.Answer answerDTO = buildAnswerDTO(answer);
                question.getAllAnswers().add(answerDTO);
                answerMap.put(answer.getId(), answerDTO);
            }
        }
        if(score != null && CollectionUtils.isNotEmpty(score.getAnswerIDs())){
            question.setChosenAnswer(new ArrayList<AssessmentInstanceDTO.Answer>());
            for(String asID: score.getAnswerIDs()){
                if(answerMap.containsKey(asID)){
                    question.getChosenAnswer().add(answerMap.get(asID));
                }
            }
        }
        return question;
    }
    private AssessmentInstanceDTO.Section buildSectionDTO(SectionHistory sectionHistory, Assessment.Score score) throws Exception{
        AssessmentInstanceDTO.Section section = new AssessmentInstanceDTO.Section();
        BeanUtilsBean.getInstance().copyProperties(section, sectionHistory);
        if(score != null && Constants.REVISION_TYPE_SECTION.equals(score.getType()) && score.getEntityId().equals(section.getId())){
            section.setPercent(score.getScorePercent());
        }
        int idx = 0;
        List<Assessment.Score> childScores = score == null? new ArrayList<Assessment.Score>(): score.getChildren();
        if(CollectionUtils.isNotEmpty(sectionHistory.getQuestionHistories())){
            section.setQuestion(new ArrayList<AssessmentInstanceDTO.Question>());
            for (QuestionHistory qHistory : sectionHistory.getQuestionHistories()) {
                Assessment.Score childScore = null;
                if(childScores.size() > idx){
                    childScore = childScores.get(idx);
                }
                section.getQuestion().add(buildQuestionDTO(qHistory, childScore));
                idx++;
            }
        }
        return section;
    }
    private void splitScore(List<Assessment.Score> scores, List<Assessment.Score> scenarioScores, List<Assessment.Score> sectionScores){
        for (Assessment.Score score : scores) {
            if(Constants.REVISION_TYPE_SCENARIO.equals(score.getType())){
                scenarioScores.add(score);
            }else if(Constants.REVISION_TYPE_SECTION.equals(score.getType())){
                sectionScores.add(score);
            }
        }
    }
    private AssessmentInstanceDTO.Scenario buildScenarioDTO(ScenarioHistory scenarioHistory, Map<String, SectionHistory> sectionHistoryMap, Assessment.Score score) throws Exception{
        AssessmentInstanceDTO.Scenario scenario = new AssessmentInstanceDTO.Scenario();
        BeanUtilsBean.getInstance().copyProperties(scenario, scenarioHistory);
        if(score != null && Constants.REVISION_TYPE_SCENARIO.equals(score.getType()) && score.getEntityId().equals(scenario.getId())){
            scenario.setPercent(score.getScorePercent());
        }
        List<Assessment.Score> childScores = score == null? new ArrayList<Assessment.Score>(): score.getChildren();
        List<Assessment.Score> scenarioScores = CommonUtils.newArrayList();
        List<Assessment.Score> sectionScores = CommonUtils.newArrayList();
        splitScore(childScores, scenarioScores, sectionScores);
        int idx = 0;
        if(CollectionUtils.isNotEmpty(scenarioHistory.getScenarioHistories())){
            scenario.setScenario(new ArrayList<AssessmentInstanceDTO.Scenario>());
            for (ScenarioHistory childHistory : scenarioHistory.getScenarioHistories()) {
                Assessment.Score childScore = null;
                if(scenarioScores.size() > idx){
                    childScore = scenarioScores.get(idx);
                }
                scenario.getScenario().add(buildScenarioDTO(childHistory, sectionHistoryMap, childScore));
                idx++;
            }
        }
        idx = 0;
        if(CollectionUtils.isNotEmpty(scenarioHistory.getSectionHistories())){
            scenario.setSection(new ArrayList<AssessmentInstanceDTO.Section>());
            for (String sID : scenarioHistory.getSectionHistories()) {
                Assessment.Score childScore = null;
                if(sectionScores.size() > idx){
                    childScore = sectionScores.get(idx);
                }
                if(sectionHistoryMap.containsKey(sID)){
                    scenario.getSection().add(buildSectionDTO(sectionHistoryMap.get(sID), childScore));
                }
                idx++;
            }
        }
        return scenario;
    }
    private void getSectionIDSet(ScenarioHistory scenarioHistory, Set<String> sectionIDSet){
        if(CollectionUtils.isNotEmpty(scenarioHistory.getSectionHistories())){
            for (String sID : scenarioHistory.getSectionHistories()) {
                sectionIDSet.add(sID);
            }
        }
        if(CollectionUtils.isNotEmpty(scenarioHistory.getScenarioHistories())){
            for (ScenarioHistory childHistory : scenarioHistory.getScenarioHistories()) {
                getSectionIDSet(childHistory, sectionIDSet);
            }
        }
    }
    private AssessmentInstanceDTO.RuleCondition buildRuleConditionDTO(RuleCondition condition) throws Exception{
        AssessmentInstanceDTO.RuleCondition ruleCondition = new AssessmentInstanceDTO.RuleCondition();
        BeanUtilsBean.getInstance().copyProperties(ruleCondition, condition);
        return ruleCondition;
    }
    private AssessmentInstanceDTO.SolutionRule buildSolutionRuleDTO(SolutionRuleHistory solutionRuleHistory) throws Exception{
        AssessmentInstanceDTO.SolutionRule solutionRule = new AssessmentInstanceDTO.SolutionRule();
        BeanUtilsBean.getInstance().copyProperties(solutionRule, solutionRuleHistory);
        if(CollectionUtils.isNotEmpty(solutionRuleHistory.getConditions())){
            solutionRule.setRuleConditions(new ArrayList<List<AssessmentInstanceDTO.RuleCondition>>());
            for (List<RuleCondition> ruleConditions : solutionRuleHistory.getConditions()) {
                List<AssessmentInstanceDTO.RuleCondition> ruleDTOs = new ArrayList<AssessmentInstanceDTO.RuleCondition>();
                for(RuleCondition ruleCondition: ruleConditions){
                    ruleDTOs.add(buildRuleConditionDTO(ruleCondition));
                }
                solutionRule.getRuleConditions().add(ruleDTOs);
            }
        }

        return solutionRule;
    }
    private AssessmentInstanceDTO.Solution buildSolutionDTO(SolutionHistory solutionHistory) throws Exception{
        AssessmentInstanceDTO.Solution solution = new AssessmentInstanceDTO.Solution();
        BeanUtilsBean.getInstance().copyProperties(solution, solutionHistory);
        if(CollectionUtils.isNotEmpty(solutionHistory.getSolutionHistories())){
            solution.setSolutions(new ArrayList<AssessmentInstanceDTO.Solution>());
            for (SolutionHistory childHistory : solutionHistory.getSolutionHistories()) {
                solution.getSolutions().add(buildSolutionDTO(childHistory));
            }
        }
        if(CollectionUtils.isNotEmpty(solutionHistory.getRuleHistories())){
            solution.setRules(new ArrayList<AssessmentInstanceDTO.SolutionRule>());
            for (SolutionRuleHistory ruleHistory : solutionHistory.getRuleHistories()) {
                solution.getRules().add(buildSolutionRuleDTO(ruleHistory));
            }
        }
        return solution;
    }
    public List<AssessmentInstanceDTO> getReport(String userId, Long revisionByUser) throws Exception{
        List<AssessmentInstanceDTO> output = CommonUtils.newArrayList();
        List<Assessment> assessments = assessmentDao.getByRevision(userId, revisionByUser);
        for (Assessment assessment : assessments) {
            AssessmentInstanceDTO dto = new AssessmentInstanceDTO();
            dto.setId(assessment.getId());
            dto.setUserId(assessment.getUserId());
            dto.setCustomerName(assessment.getCustomerName());
            dto.setCustomerEmail(assessment.getCustomerEmail());
            dto.setCountByUser(assessment.getCountByUser());
            dto.setTimeStamp(assessment.getScoreDate());
            if(CollectionUtils.isNotEmpty(assessment.getSolutionHistories())){
                dto.setSolution(new ArrayList<AssessmentInstanceDTO.Solution>());
                for (SolutionHistory solutionHistory : assessment.getSolutionHistories()) {
                    dto.getSolution().add(buildSolutionDTO(solutionHistory));
                }
            }
            Set<String> sectionIDSet = CommonUtils.newHashSet();
            getSectionIDSet(assessment.getRootScenarioHistory(), sectionIDSet);
            Map<String, SectionHistory> sectionHistoryMap = CommonUtils.newHashMap();
            if(CollectionUtils.isNotEmpty(sectionIDSet)){
                List<SectionHistory> sectionHistories = sectionHistoryDao.find(sectionIDSet);
                for (SectionHistory sectionHistory : sectionHistories) {
                    sectionHistoryMap.put(sectionHistory.getHistoryId(), sectionHistory);
                }
            }
            System.out.println("-------------------------------------------------");
            System.out.println("-----------" + assessment.getId());
            System.out.println("-------------------------------------------------");
            dto.setScenario(buildScenarioDTO(assessment.getRootScenarioHistory(), sectionHistoryMap, assessment.getScore()));
            output.add(dto);
        }

        return output;
    }
    private Map<String, Object> validateScenarioAndBuildAssessment(AssessmentInstanceDTO.Scenario dtoScenario, Map<String, Scenario> scMap, Map<String, Section> sectionMap, Map<String, Question> questionMap, Assessment.Score rootScore, boolean isRootScenario){
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
                Assessment.Score sectionScore = new Assessment.Score(childDtoSection.getId(), Constants.REVISION_TYPE_SECTION, childDtoSection.getPercent());

                List<AssessmentInstanceDTO.Question> dtoQuestions = childDtoSection.getQuestion();
                for(AssessmentInstanceDTO.Question dtoQuestion: dtoQuestions){
                    Question question = questionMap.get(dtoQuestion.getQuestionId());
                    if(question == null){
                        return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_QUESTION_NOT_FOUND);
                    }
                    if(!section.getId().equals(question.getSectionId())){
                        return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_INVALID_QUESTION);
                    }
                    Assessment.Score questionScore = new Assessment.Score(dtoQuestion.getQuestionId(), Constants.REVISION_TYPE_QUESTION, dtoQuestion.getPercent());
                    Map<String, Answer> answerMap = new HashMap<String, Answer>();
                    for (Answer answer : question.getAnswers()) {
                        answerMap.put(answer.getId(), answer);
                    }
                    if(CollectionUtils.isNotEmpty(dtoQuestion.getChosenAnswer())){
                        questionScore.setAnswerIDs(new ArrayList<String>());
                        for(AssessmentInstanceDTO.Answer dtoAnswers: dtoQuestion.getChosenAnswer()){
                            if(!answerMap.containsKey(dtoAnswers.getId())){
                                return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_INVALID_QUESTION);
                            }
                            questionScore.getAnswerIDs().add(dtoAnswers.getId());
                        }
                    }
                    sectionScore.getChildren().add(questionScore);
                }
                rootScore.getChildren().add(sectionScore);
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
                Assessment.Score scenarioScore = new Assessment.Score(childDtoScenario.getId(), Constants.REVISION_TYPE_SCENARIO, childDtoScenario.getPercent());
                rootScore.getChildren().add(scenarioScore);
                Map<String, Object> otherOutput = validateScenarioAndBuildAssessment(childDtoScenario, scMap, sectionMap, questionMap, scenarioScore, false);
                ErrorCodeMap errorCode = (ErrorCodeMap) otherOutput.get(Constants.ERROR_CODE);
                if(errorCode != null){
                    return otherOutput;
                }
            }
        }
        if(isRootScenario){
            Assessment ass = new Assessment();
            ass.setRootScenarioHistory(rootScenarioHistory);
            ass.setScore(rootScore);
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
//        Map<String, Assessment.Score> scoreMap = CommonUtils.newHashMap();
        AssessmentInstanceDTO.Scenario rootDtoScenario = dto.getScenario();
        if(rootDtoScenario == null){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_SCENARIO_NOT_FOUND);
        }
        Assessment.Score rootScore = new Assessment.Score(rootDtoScenario.getId(), Constants.REVISION_TYPE_SCENARIO, rootDtoScenario.getPercent());
        Map<String, Object> validateResult = validateScenarioAndBuildAssessment(rootDtoScenario, scMap, sectionMap, questionMap, rootScore, true);
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
        ass.setDto(CommonUtils.printPrettyObj(dto));
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
    @Autowired
    private SectionHistoryDao sectionHistoryDao;
}
