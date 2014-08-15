package com.clas.starlite.webapp.util;

import com.clas.starlite.webapp.common.ErrorCodeMap;
import com.clas.starlite.webapp.dto.RestResultDTO;

/**
 * Created by Son on 8/14/14.
 */
public class RestUtils {
    static public RestResultDTO createInvalidOutput(ErrorCodeMap errorCode) {
        RestResultDTO output = new RestResultDTO();
        output.setErrorCode(errorCode.getValue());
        output.setErrorDesc(errorCode.name());
        return output;
    }
}
