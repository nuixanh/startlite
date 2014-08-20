package com.clas.starlite.dao;

import com.clas.starlite.domain.Revision;
import com.clas.starlite.domain.RevisionHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Son on 8/14/14.
 */
public class RevisionDao extends BaseDao<Revision, String>{

    private Revision incVersion(String type, long inc, String action, String entityId){
        Update update = Update.update("whenUpdated", Calendar.getInstance().getTime()).inc("version", inc);
        //return the modified object rather than the original
        Revision revision = template.findAndModify(query(where("type").is(type)),
                update, FindAndModifyOptions.options().returnNew(true).upsert(true), Revision.class);
        RevisionHistory rHistory = new RevisionHistory(UUID.randomUUID().toString(), type, revision.getVersion(),
                action, entityId, Calendar.getInstance().getTime());
        template.save(rHistory);
        return revision;
    }
    public Revision incVersion(String type, String action, String entityId){
        return incVersion(type, 1, action, entityId);
    }

    public List<RevisionHistory> getHistory(String type, Long revision){
        Criteria cr = Criteria.where("type").is(type).and("version").gt(revision);
        return template.find(Query.query(cr), RevisionHistory.class);
    }
    public List<RevisionHistory> getHistory(String type, String action, Long revision){
        Criteria cr = Criteria.where("type").is(type).and("action").is(action).and("version").gt(revision);
        return template.find(Query.query(cr), RevisionHistory.class);
    }
    public Long getCurrentVersion(String type, String action){
        Query q = Query.query(Criteria.where("type").is(type).and("action").is(action));
        q.with(new Sort(Sort.Direction.DESC, "version"));
        q.limit(1);
        RevisionHistory rh = template.findOne(q, RevisionHistory.class);
        if(rh != null){
            return rh.getVersion();
        }else{
            return 0L;
        }
    }
}
