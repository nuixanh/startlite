package clas.startlite.webapp.security;

import com.clas.startlite.common.Constants;
import com.clas.startlite.domain.User;
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
        User user = getUserDetail(username);
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, getAuthorities(user.getRole()));
        return userDetail;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<GrantedAuthority> getAuthorities(int role) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        if (role == 0) {
            authList.add(new SimpleGrantedAuthority(Constants.ROLE_REGULAR_USER));
        } else if (role == 1) {
            authList.add(new SimpleGrantedAuthority(Constants.ROLE_QUESTION_CONTRIBUTOR));
        } else if (role == 2) {
            authList.add(new SimpleGrantedAuthority(Constants.ROLE_QUESTION_CONTRIBUTOR));
            authList.add(new SimpleGrantedAuthority(Constants.ROLE_SCENARIO_CREATOR));
        }
        return authList;
    }

    public User getUserDetail(String username){
        MongoOperations mongoOperation = mongoTemplate;
        User user = mongoOperation.findOne(
                new Query(Criteria.where("username").is(username)),
                User.class);
        return user;
    }
}
