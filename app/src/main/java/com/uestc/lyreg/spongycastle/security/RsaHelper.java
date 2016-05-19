package com.uestc.lyreg.spongycastle.security;

import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

/**
 * Created by Administrator on 2016/5/17.
 *
 * @Author lyreg
 */
public class RsaHelper {
    private static final String TAG = "RsaHelper";

    private static final String RSA = "RSA";
    private static final String MD5 = "MD5withRSA";
    private static final String PROVIDER = "SC";


    private RSAPublicKey mPublicKey;
    private RSAPrivateKey mPrivateKey;
    private String          plainText = "RsaHelper";

    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }


    public RsaHelper() {}

    public KeyPair generateKeys(int keysize) throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA, PROVIDER);
        keyPairGen.initialize(keysize, new SecureRandom());

        KeyPair pair = keyPairGen.generateKeyPair();
        mPublicKey = (RSAPublicKey) pair.getPublic();
        mPrivateKey = (RSAPrivateKey) pair.getPrivate();
        return pair;
    }

    public byte[] sign(byte[] data)
            throws Exception {
        Signature signature = Signature.getInstance(MD5, PROVIDER);
        signature.initSign(mPrivateKey);
        signature.update(data);

        return signature.sign();
    }

    public boolean verify(byte[] data, byte[] sign)
            throws Exception {
        Signature signature = Signature.getInstance(MD5, PROVIDER);
        signature.initVerify(mPublicKey);
        signature.update(data);

        return signature.verify(sign);
    }

    public byte[] encryptByPublicKey(byte[] data)
            throws Exception {
        Cipher cipher = Cipher.getInstance(RSA, PROVIDER);
        SecureRandom random = new SecureRandom();
        cipher.init(Cipher.ENCRYPT_MODE, mPublicKey, random);

        // 模长
        int key_len = mPublicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        byte[][] datas = splitArray(data, key_len - 11);
        Log.e(TAG, "enc datas len is " + datas.length);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for(byte[] s : datas) {
            out.write(cipher.doFinal(s));
        }
        out.close();

        return out.toByteArray();
    }

    public byte[] decryptByPrivateKey(byte[] data)
            throws Exception {
        Cipher cipher = Cipher.getInstance(RSA, PROVIDER);
        SecureRandom random = new SecureRandom();
        cipher.init(Cipher.DECRYPT_MODE, mPrivateKey, random);
        //模长
        int key_len = mPrivateKey.getModulus().bitLength() / 8;
        //如果密文长度大于模长则要分组解密
        byte[][] arrays = splitArray(data, key_len);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for(byte[] arr : arrays){
            out.write(cipher.doFinal(arr));
        }
        out.close();

        return out.toByteArray();
    }

    /**
     *拆分数组
     */
    public byte[][] splitArray(byte[] data, int len){
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if(y!=0){
            z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
            arr = new byte[len];
            if(i==x+z-1 && y!=0){
                System.arraycopy(data, i*len, arr, 0, y);
            }else{
                System.arraycopy(data, i*len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }
}
