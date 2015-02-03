package com.clas.starlite.webapp.util;

import com.clas.starlite.common.Constants;
import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Son on 8/14/14.
 */
public class RestUtils {
    static public RestResultDTO createInvalidOutput(Exception ex) {
        RestResultDTO output = new RestResultDTO();
        output.setErrorCode(ErrorCodeMap.FAILURE_EXCEPTION.getValue());
        output.setErrorDesc(ex.getMessage());
        return output;
    }
    static public RestResultDTO createInvalidOutput(ErrorCodeMap errorCode) {
        RestResultDTO output = new RestResultDTO();
        output.setErrorCode(errorCode.getValue());
        output.setErrorDesc(errorCode.name());
        return output;
    }
    static public Map<String, Object> createInvalidOutput(Map<String, Object> output, ErrorCodeMap errorCode) {
        output.put(Constants.ERROR_CODE, errorCode);
        return output;
    }
    static public Map<String, Object> createExceptionOutput(Map<String, Object> output, Exception ex) {
        output.put(Constants.ERROR_CODE, ErrorCodeMap.FAILURE_EXCEPTION);
        output.put(Constants.ERROR_DESC, ex.getMessage());
        return output;
    }
    static public Map<String, Object> createInvalidOutputMap(ErrorCodeMap errorCode) {
        Map<String, Object> output = new HashMap<String, Object>();
        output.put(Constants.ERROR_CODE, errorCode);
        return output;
    }
}
