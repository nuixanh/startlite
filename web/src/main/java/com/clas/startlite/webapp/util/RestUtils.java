package com.clas.startlite.webapp.util;

import com.clas.startlite.webapp.common.ErrorCodeMap;
import com.clas.startlite.webapp.dto.RestResultDTO;

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
