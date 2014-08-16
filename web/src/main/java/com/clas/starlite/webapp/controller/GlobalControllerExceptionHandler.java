package com.clas.starlite.webapp.controller;

import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;
import com.clas.starlite.webapp.util.RestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Son on 8/16/14.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler{
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    RestResultDTO handleException(Exception ex) {
        RestResultDTO restResultDTO = RestUtils.createInvalidOutput(ErrorCodeMap.FAILURE_EXCEPTION);
        restResultDTO.setData(ExceptionUtils.getStackTrace(ex));
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------");
        ex.printStackTrace();
        System.out.println("-------------------------------------------------------------------------------");
        return restResultDTO;
    }
}
