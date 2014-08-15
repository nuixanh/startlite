package com.clas.starlite.webapp.security;

import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.util.RestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Son on 8/13/14.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        RestResultDTO restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_SESSION_INVALID);
        Gson gson = new GsonBuilder().serializeNulls().create();
        response.getOutputStream().println(gson.toJson(restResultDTO));
    }
}
