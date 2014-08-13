package com.clas.startlite.dao;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

public class BaseDao<T, ID extends Serializable> {
    private static Logger log = LoggerFactory.getLogger(BaseDao.class);
    MongoTemplate template;
    private Class<T> parameterizedType;

    public void save(T entity) {
        template.save(entity);
    }

    public T findOne(ID id) {
        Query query = new Query(Criteria.where("id").is(id));
        return template.findOne(query, parameterizedType);
    }

    public void delete(T entity) {
        template.remove(entity);
    }

    public void deleteById(ID id) {
        T entity = findOne(id);
        if (null != entity)
            delete(entity);
    }

    public List<T> findAll() {
        return template.findAll(parameterizedType);
    }

    public long findAllCount() {
        return template.count(new Query(), parameterizedType);
    }

    public void deleteAll() {
        template.remove(new Query(), parameterizedType);
        ;
    }


    public List<DBObject> getIndexInfo() {
        return template.execute(parameterizedType,
                new CollectionCallback<List<DBObject>>() {
                    public List<DBObject> doInCollection(DBCollection collection)
                            throws MongoException, DataAccessException {
                        return collection.getIndexInfo();
                    }
                });
    }

    public MongoTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MongoTemplate template) {
        this.template = template;
    }

    public Class<T> getParameterizedType() {
        return parameterizedType;
    }

    public void setParameterizedType(Class<T> parameterizedType) {
        this.parameterizedType = parameterizedType;
    }
}