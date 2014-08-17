package com.clas.starlite.webapp.security;

import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.util.RestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Son on 8/17/14.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        RestResultDTO restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_PERMISSION_DENY);
        Gson gson = new GsonBuilder().serializeNulls().create();
        response.getOutputStream().println(gson.toJson(restResultDTO));
    }
}
