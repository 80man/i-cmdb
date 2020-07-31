package com.xtxb.cmdb.util;

import com.sun.crypto.provider.SunJCE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Random;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月30日-下午2:04
 * <p>
 * <p>
 * DES加解密工具类
 */
@Component
public class DesEncrypt {
    @Autowired
    private LoggerUtil log;
    /**
     * 用于CMDB系统内部加解密
     */
    public static String KEY_ENCRYPT_SYSTEM="iCMcb!*20200801";

    private Cipher decryptCipher = null;

    private Cipher encryptCipher = null;

    public DesEncrypt(){
        Security.addProvider(new SunJCE());
        try {
            Key key = getKey(KEY_ENCRYPT_SYSTEM.getBytes());
            this.encryptCipher = Cipher.getInstance("DES");
            this.encryptCipher.init(1, key);
            this.decryptCipher = Cipher.getInstance("DES");
            this.decryptCipher.init(2, key);
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 解密
     * @param strIn
     * @return
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 加密
     * @param strIn
     * @return
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    private byte[] decrypt(byte[] arrB) throws Exception {
        return this.decryptCipher.doFinal(arrB);
    }

    private byte[] encrypt(byte[] arrB) throws Exception {
        return this.encryptCipher.doFinal(arrB);
    }

    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++)
            arrB[i] = arrBTmp[i];
        Key key = new SecretKeySpec(arrB, "DES");
        return key;
    }

    private String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0)
                intTmp += 256;
            if (intTmp < 16)
                sb.append("0");
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    private byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i += 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte)Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    private String getChar(int index){
        switch (index) {
            case 0:
                return "!";
            case 1:
                return "@";
            case 2:
                return "#";
            case 3:
                return "$";
            case 4:
                return "%";
            case 5:
                return "^";
            case 6:
                return "&";
            case 7:
                return "*";
            case 8:
                return "(";
            default:
                return ")";
        }
    }


}
