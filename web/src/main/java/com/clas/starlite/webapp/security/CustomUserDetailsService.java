package com.clas.starlite.webapp.security;

import com.clas.starlite.common.Constants;
import com.clas.starlite.dao.UserDao;
import com.clas.starlite.domain.User;
import com.clas.starlite.webapp.util.AuthorityUtils;
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
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {
        org.springframework.security.core.userdetails.User userDetail = null;
        User user = userDao.findOne(userId);
        if(user != null){
            userDetail = new org.springframework.security.core.userdetails.User(userId, user.getPassword(), true, true, true, true, AuthorityUtils.getAuthorities(user.getRole()));
        }
        return userDetail;
    }

    @Autowired
    private UserDao userDao;
}
