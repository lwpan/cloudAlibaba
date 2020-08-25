package com.hltx.tool.encryption;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Random;

/**
 * User: ycj
 * Date: 2020/7/30
 * Time: 14:54
 * Description:
 */
public class DES {
    public static void main(String[] args) throws Exception {
        //动态创建16位秘钥,可以写死
        String password = createSecKey(16);
        System.out.println(password);

        String str = "测试内容";
        /*测试加密*/
        String rs = encrypt(str, password);
        System.out.println("加密后: "+rs);

        /*测试解密*/
        String rs_des = decrypt(rs,password);
        System.out.println("解密后: "+rs_des);

    }

    private static String[] ss = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "0" };

    /**
     * 根据长度生成密钥
     * @param length 长度须是8
     * @return
     * @throws Exception
     */
    public static String createSecKey(int length) {
        if (length % 8 != 0) {
            return null;
        }
        String result = "";
        for (int i = 0; i < length; i++) {
            Random r = new Random();
            int n = r.nextInt(ss.length);
            result += ss[n];
        }
        return result;
    }

    /**
     * 加密并base64转码,如果要用get发送这个数据一定要先URLEncoder.encode()转码后发送,这样会把+、=等转换成相应的%2B、%3D等。不转换的话+接收会变成空格
     * @param data 待加密数据
     * @param key  秘钥
     * @return 先des加密后在用base64转码
     * @throws Exception
     */
    public static String encrypt(String data, String key){
        try {
            byte[] bt = encrypt(data.getBytes("utf-8"), key);
            String strs = Base64.encodeBase64String(bt);
            return strs;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 解密(先base64解码)
     * @param data 待解密字符串
     * @param key 秘钥
     * @return 先base64转码,再des解密
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        byte[] buf = Base64.decodeBase64(data);
        byte[] bt = decrypt(buf, key);
        return new String(bt, "UTF-8");
    }

    /**
     * 加密 - byte数组
     * @param datasource
     * @param password
     * @return
     */
    public static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成SecretKey
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密 - byte数组
     * @param src 加密字符串的二进制数组
     * @param password 解密密码
     * @return 原字符串的二进制数组
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
        //DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        //创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        //创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        //将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        //Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        //用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }
}
