package com.clas.starlite.webapp.security;

import com.clas.starlite.common.Constants;
import com.clas.starlite.dao.SessionDao;
import com.clas.starlite.dao.UserDao;
import com.clas.starlite.domain.Session;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.util.AuthorityUtils;
import com.clas.starlite.webapp.util.RestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Son on 8/13/14.
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String userId = httpServletRequest.getHeader(Constants.HTTP_HEADER_USER);
        String token = httpServletRequest.getHeader(Constants.HTTP_HEADER_TOKEN);
        boolean hasError = false;
        String error = null;
        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(token)){
            try {
                Session session = sessionDao.validate(token, userId, System.currentTimeMillis());
                if(session != null){
                    com.clas.starlite.domain.User user = userDao.findOne(userId);
                    if(user != null){
                        UserDetails userDetails = new User(userId, user.getPassword(), true, true, true, true, AuthorityUtils.getAuthorities(user.getRole()));
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
                    }
                }
            } catch (Exception e) {
                error = ExceptionUtils.getStackTrace(e);
                hasError = true;
            }
        }
        if(hasError){
            RestResultDTO restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_EXCEPTION);
            restResultDTO.setData(error);
            Gson gson = new GsonBuilder().serializeNulls().create();
            response.getOutputStream().println(gson.toJson(restResultDTO));
        }else{
            // continue thru the filter chain
            chain.doFilter(request, response);
        }
    }

    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private UserDao userDao;

    AuthenticationManager authenticationManager;

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
