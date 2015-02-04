package com.clas.starlite.dao;

import com.clas.starlite.domain.Revision;
import com.clas.starlite.domain.RevisionHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Son on 8/14/14.
 */
public class RevisionDao extends BaseDao<Revision, String>{

    private Revision incVersion(String type, long inc, String action, String entityId, String relatedEntityId){
        Update update = Update.update("whenUpdated", Calendar.getInstance().getTime()).inc("version", inc);
        //return the modified object rather than the original
        Revision revision = template.findAndModify(query(where("type").is(type)),
                update, FindAndModifyOptions.options().returnNew(true).upsert(true), Revision.class);
        RevisionHistory rHistory = new RevisionHistory(UUID.randomUUID().toString(), type, revision.getVersion(),
                action, entityId, Calendar.getInstance().getTime(), relatedEntityId);
        template.save(rHistory);
        return revision;
    }
    private Revision incVersion(String type, long inc, String action, Collection<String> entityIdSet, String relatedEntityId){
        Update update = Update.update("whenUpdated", Calendar.getInstance().getTime()).inc("version", inc);
        //return the modified object rather than the original
        Revision revision = template.findAndModify(query(where("type").is(type)),
                update, FindAndModifyOptions.options().returnNew(true).upsert(true), Revision.class);
        updateRevisionHistory(type, action, revision.getVersion(), entityIdSet, relatedEntityId);
        return revision;
    }
    public Revision incVersion(String type, String action, String entityId){
        return incVersion(type, 1, action, entityId, null);
    }
    public Revision incVersion(String type, String action, String entityId, String relatedEntityId){
        return incVersion(type, 1, action, entityId, relatedEntityId);
    }
    public Revision incVersion(String type, String action, Collection<String> entityIdSet){
        return incVersion(type, 1, action, entityIdSet, null);
    }
    public Revision incVersion(String type, String action, Collection<String> entityIdSet, String relatedEntityId){
        return incVersion(type, 1, action, entityIdSet, relatedEntityId);
    }
    private void updateRevisionHistory(String type, String action, long revision, Collection<String> entityIdSet, String relatedEntityId){
        for (String entityId : entityIdSet) {
            RevisionHistory rHistory = new RevisionHistory(UUID.randomUUID().toString(), type, revision,
                    action, entityId, Calendar.getInstance().getTime(), relatedEntityId);
            template.save(rHistory);
        }
    }

    public void dropRevisionHistory(){
        template.dropCollection(RevisionHistory.class);
    }
}
