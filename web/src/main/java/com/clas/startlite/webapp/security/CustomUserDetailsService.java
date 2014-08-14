package com.clas.startlite.webapp.security;

import com.clas.startlite.common.Constants;
import com.clas.startlite.dao.UserDao;
import com.clas.startlite.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = getUserDetail(email);
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, getAuthorities(user.getRole()));
        return userDetail;
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

    public User getUserDetail(String email){
        User user = userDao.findOneByEmail(email);
        return user;
    }

    @Autowired
    private UserDao userDao;
}
