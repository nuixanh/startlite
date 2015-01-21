package com.clas.starlite.webapp.service;

import com.clas.starlite.dao.*;
import com.clas.starlite.domain.Assessment;
import com.clas.starlite.domain.Solution;
import com.clas.starlite.domain.SolutionHistory;
import com.clas.starlite.domain.User;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.AssessmentInstanceDTO;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by sonnt4 on 21/1/2015.
 */
@Component
public class AssessmentService {
    public Map<String, Object> score(AssessmentInstanceDTO dto){
        Map<String, Object> output = new HashMap<String, Object>();
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
        List<SolutionHistory> solutionHistories = Collections.emptyList();
        for (Solution solution : solutions) {
            SolutionHistory solutionHistory = solutionHistoryDao.snapshotSolution(solution);
            solutionHistories.add(solutionHistory);
        }
        Assessment ass = new Assessment();
        ass.setSolutionHistories(solutionHistories);
        ass.setScoreDate(dto.getTimeStamp());
        assessmentDao.save(ass);
        return output;
    }
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private SolutionDao solutionDao;
    @Autowired
    private SolutionHistoryDao solutionHistoryDao;
    @Autowired
    private AssessmentDao assessmentDao;
}
