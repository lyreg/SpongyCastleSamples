package com.uestc.lyreg.spongycastle.security;

import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x500.X500NameBuilder;
import org.spongycastle.asn1.x500.style.BCStyle;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.spongycastle.util.encoders.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * Created by Administrator on 2016/5/17.
 *
 * @Author lyreg
 */
public class CertHelper {

    private static final String TAG = "CertHelper";

    private static final String RSA = "RSA";
    private static final String MD5 = "MD5withRSA";
    private static final String PROVIDER = "SC";

    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    public KeyPair generateKeys(int keysize) throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA, PROVIDER);
        keyPairGen.initialize(keysize, new SecureRandom());

        KeyPair pair = keyPairGen.generateKeyPair();
        return pair;
    }

    public String genCSR(KeyPair kp, String countryname, String state,
                         String localityname, String organization, String connonname, String email)
            throws Exception{
        X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE);

        x500NameBld.addRDN(BCStyle.C, countryname);
        x500NameBld.addRDN(BCStyle.ST, state);
        x500NameBld.addRDN(BCStyle.L, localityname);
        x500NameBld.addRDN(BCStyle.O, organization);
        x500NameBld.addRDN(BCStyle.CN, connonname);
        x500NameBld.addRDN(BCStyle.E, email);

        X500Name subject = x500NameBld.build();

        return genCSR(kp, subject);
    }

    public String genCSR(KeyPair kp, X500Name subject)
            throws Exception {
        PKCS10CertificationRequestBuilder requestBuilder =
                new JcaPKCS10CertificationRequestBuilder(subject, kp.getPublic());

        PKCS10CertificationRequest req = requestBuilder.build(
                new JcaContentSignerBuilder(MD5).setProvider(PROVIDER).
                        build(kp.getPrivate()));

        if(!req.isSignatureValid(new JcaContentVerifierProviderBuilder()
                .setProvider(PROVIDER).build(kp.getPublic()))) {
            throw new Exception("genCSR failed => " + MD5 + " : Failed verify check.");
        } else {
            byte[] encoded = req.getEncoded();
            String csr = "-----BEGIN CERTIFICATE REQUEST-----\n";
            csr += new String(Base64.encode(encoded));
            csr += "\n-----END CERTIFICATE REQUEST-----\n";

            return csr;
        }
    }
}
