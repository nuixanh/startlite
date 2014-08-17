package com.clas.starlite.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Son on 6/9/14.
 */
public class RijndaelCrypt {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) throws Exception{
        RijndaelCrypt rijndaelCrypt = new RijndaelCrypt("17082014");
        System.out.println(rijndaelCrypt.encrypt("test".getBytes()));
//        System.out.println(rijndaelCrypt.decrypt("zvFn2CHF2LFBgmivVxvmrw=="));
    }

    private static String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static String ALGORITHM = "AES";
    private static String DIGEST = "MD5";

    private static Cipher _cipher;
    private static SecretKey _password;
    private static IvParameterSpec _IVParamSpec;

    //16-byte private key
    private static byte[] IV = "s!t1a#r@l%i7t5e4".getBytes();;

    public static String generatePublicKey(){
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }
    /**
     Constructor
     @password Public key

     */
    public RijndaelCrypt(String publicKey) {

        try {
            //Encode digest
            MessageDigest digest;
            digest = MessageDigest.getInstance(DIGEST);
            _password = new SecretKeySpec(digest.digest(publicKey.getBytes()), ALGORITHM);

            //Initialize objects
            _cipher = Cipher.getInstance(TRANSFORMATION);
            _IVParamSpec = new IvParameterSpec(IV);

        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm " + ALGORITHM, e);
        } catch (NoSuchPaddingException e) {
            log.error("No such padding PKCS7", e);
        } catch (Exception e) {
            log.error("RijndaelCrypt Error", e);
        }
    }

    /**
     Encryptor.

     @text String to be encrypted
     @return Base64 encrypted text

     */
    public String encrypt(byte[] text) {

        byte[] encryptedData;

        try {

            _cipher.init(Cipher.ENCRYPT_MODE, _password, _IVParamSpec);
            encryptedData = _cipher.doFinal(text);

        } catch (InvalidKeyException e) {
            log.error("Invalid key  (invalid encoding, wrong length, uninitialized, etc).", e);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            log.error("Invalid or inappropriate algorithm parameters for " + ALGORITHM, e);
            return null;
        } catch (IllegalBlockSizeException e) {
            log.error("The length of data provided to a block cipher is incorrect", e);
            return null;
        } catch (BadPaddingException e) {
            log.error("The input data but the data is not padded properly.", e);
            return null;
        }

//        return new String(new Base64(0).encodeBase64(encryptedData));
//        return new BASE64Encoder().encode(encryptedData);

        return new String(org.springframework.security.crypto.codec.Base64.encode(encryptedData));

    }

    /**
     Decryptor.

     @text Base64 string to be decrypted
     @return decrypted text

     */
    public String decrypt(String text) {
        if(StringUtils.isBlank(text)){
            return null;
        }
        try {
            _cipher.init(Cipher.DECRYPT_MODE, _password, _IVParamSpec);

            byte[] decodedValue = org.springframework.security.crypto.codec.Base64.decode(text.getBytes());
//            byte[] decodedValue = Base64.decodeBase64(text.getBytes("UTF-8"));
            byte[] decryptedVal = _cipher.doFinal(decodedValue);
            return new String(decryptedVal);

        } catch (InvalidKeyException e) {
            log.error("Invalid key  (invalid encoding, wrong length, uninitialized, etc).", e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error("Invalid or inappropriate algorithm parameters for " + ALGORITHM, e);
        } catch (IllegalBlockSizeException e) {
            log.error("The length of data provided to a block cipher is incorrect", e);
        } catch (BadPaddingException e) {
            log.error("The input data but the data is not padded properly.", e);
        }
        return null;

    }
}
