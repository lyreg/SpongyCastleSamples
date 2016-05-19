package com.uestc.lyreg.spongycastle.security;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by Administrator on 2016/5/16.
 *
 * @Author lyreg
 */
public class DesHelper {
    private static final String TAG = "DesHelper";

    private static final String DES = "DES";
    private static final String PROVIDER = "SC";

    private KeyGenerator desKeyGenerator = null;
    private SecretKey desKey = null;
    private String    plainText = "DesHelper";

    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }


    public DesHelper() {

    }

    public byte[] generateDesKey(int digit)
            throws NoSuchProviderException, NoSuchAlgorithmException {
        desKeyGenerator = KeyGenerator.getInstance(DES, PROVIDER);
        SecureRandom random = new SecureRandom();
        desKeyGenerator.init(56, random);
        desKey = desKeyGenerator.generateKey();
        byte[] rawKey = desKey.getEncoded();
        return rawKey;
    }

    public byte[] encryption(String plain)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(DES, PROVIDER);
        SecureRandom random = new SecureRandom();

        cipher.init(Cipher.ENCRYPT_MODE, desKey, random);

        if(plain != null && plain != "") {
            plainText = plain;
        }

        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return cipherText;
    }

    public byte[] decryption(byte[] src)
            throws Exception {
        Cipher cipher = Cipher.getInstance(DES, PROVIDER);
        SecureRandom random = new SecureRandom();

        cipher.init(Cipher.DECRYPT_MODE, desKey, random);

        if(src == null) {
            throw new Exception("decryp src is null");
        }

        byte[] plain = cipher.doFinal(src);
        return plain;
    }
}
