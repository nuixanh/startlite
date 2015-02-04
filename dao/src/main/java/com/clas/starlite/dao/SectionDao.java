package com.clas.starlite.dao;

import com.clas.starlite.common.Status;
import com.clas.starlite.domain.Section;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by Son on 8/19/14.
 */
public class SectionDao extends BaseDao<Section, String>{
    public List<Section> getTrees(Long revision){
        Criteria cr = Criteria.where("parentId").exists(Boolean.FALSE);
        if(revision != null && revision > 0){
            cr = cr.and("revision").gt(revision);
        }
        Query q = Query.query(cr);
        return template.find(q, Section.class);
    }
    public List<Section> getActiveByName(String name){
        Criteria cr = Criteria.where("name").is(name).and("status").is(Status.ACTIVE.getValue());
        Query q = Query.query(cr);
        return template.find(q, Section.class);
    }
    public List<Section> getActiveByName(Collection<String> names){
        Criteria cr = Criteria.where("name").in(names).and("status").is(Status.ACTIVE.getValue());
        Query q = Query.query(cr);
        return template.find(q, Section.class);
    }
    public Section getOneActiveByName(String name){
        Criteria cr = Criteria.where("name").is(name).and("status").is(Status.ACTIVE.getValue());
        Query q = Query.query(cr);
        return template.findOne(q, Section.class);
    }
}
