package com.clas.startlite.webapp.util;

import com.clas.startlite.common.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Son on 8/14/14.
 */
public class AuthorityUtils {
    public static List<GrantedAuthority> getAuthorities(int role) {
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
}
