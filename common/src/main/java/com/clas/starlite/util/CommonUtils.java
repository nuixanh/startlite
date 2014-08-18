package com.clas.starlite.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Created by sonnt4 on 8/18/2014.
 */
public class CommonUtils {
    private static final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) return false;
        return rfc2822.matcher(email.toLowerCase()).matches();
    }
    public static String genMD5(String input) {
        String output;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());

            byte byteData[] = md.digest();
            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            output = sb.toString();
        } catch (NoSuchAlgorithmException e1) {
            return null;
        }
        return output;
    }

    public static void main(String[] args){
        System.out.println(genMD5("test"));
        System.out.println(genMD5("test" + genMD5("test")));
    }
}
