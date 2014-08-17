package com.clas.starlite.webapp.util;

import com.clas.starlite.common.Constants;
import com.clas.starlite.common.UserRole;
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
            authList.add(new SimpleGrantedAuthority(UserRole.ROLE_REGULAR.name()));
        } else if (role == 1) {
            authList.add(new SimpleGrantedAuthority(UserRole.ROLE_CONTRIBUTOR.name()));
        } else if (role == 2) {
            authList.add(new SimpleGrantedAuthority(UserRole.ROLE_CONTRIBUTOR.name()));
            authList.add(new SimpleGrantedAuthority(UserRole.ROLE_SCENARIO_CREATOR.name()));
        }
        return authList;
    }
}
