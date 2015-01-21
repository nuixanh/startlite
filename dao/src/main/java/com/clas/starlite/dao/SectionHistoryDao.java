package com.clas.starlite.dao;

import com.clas.starlite.domain.*;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 1/21/2015.
 */
public class SectionHistoryDao extends BaseDao<SectionHistory, String>{
    public SectionHistory getOneByIdAndRevision(String id, long revision){
        Criteria cr = Criteria.where("id").is(id).and("revision").is(revision);
        Query q = Query.query(cr);
        return template.findOne(q, SectionHistory.class);
    }
}
