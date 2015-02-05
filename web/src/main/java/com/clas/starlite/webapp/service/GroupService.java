package com.clas.starlite.webapp.service;

import com.clas.starlite.dao.UserGroupDao;
import com.clas.starlite.domain.UserGroup;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Admin on 1/20/2015.
 */
@Component
public class GroupService {

    public Map<String, Object> create(UserGroup group, String userId) {
        Map<String, Object> output = new HashMap<String, Object>();
        if(group == null || StringUtils.isBlank(group.getName())){
            return RestUtils.createInvalidOutput(output, ErrorCodeMap.FAILURE_INVALID_PARAMS);
        }
        group.setId(UUID.randomUUID().toString());
        group.setCreated(System.currentTimeMillis());
        group.setModified(group.getCreated());
        group.setCreatedBy(userId);
        group.setModifiedBy(userId);

        return output;
    }
    @Autowired
    private UserGroupDao userGroupDao;
}
