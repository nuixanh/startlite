package com.clas.startlite.webapp;

import com.clas.domain.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Son on 8/6/14.
 */

public class CustomUserDetailsService implements UserDetailsService {
    private MongoTemplate mongoTemplate;

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        AdminUser adminUser = getUserDetail(username);
        System.out.println(username);
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(adminUser.getUsername(), adminUser.getPassword(),true,true,true,true,getAuthorities(adminUser.getRole()));
        return userDetail;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        if (role.intValue() == 1) {
            authList.add(new SimpleGrantedAuthority("ROLE_USER"));
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (role.intValue() == 2) {
            authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authList;
    }

    public AdminUser getUserDetail(String username){
        MongoOperations mongoOperation = (MongoOperations)mongoTemplate;
        AdminUser adminUser = mongoOperation.findOne(
                new Query(Criteria.where("username").is(username)),
                AdminUser.class);
        System.out.println(adminUser.toString());
        return adminUser;
    }
}
