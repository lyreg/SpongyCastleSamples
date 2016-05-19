package com.uestc.lyreg.spongycastle;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uestc.lyreg.spongycastle.security.RsaHelper;
import com.uestc.lyreg.spongycastle.utils.HexStringConvert;

import java.security.KeyPair;

public class RsaActivity extends AppCompatActivity {

    private static final String TAG = "RsaActivity";

    private Button mButtonGenKeyPair;
    private Button mButtonEnc;
    private Button mButtonDecry;
    private Button mButtonSign;
    private Button mButtonVerify;
    private TextView mTextViewRsa;
    private EditText mEditTextPlain;
    private RelativeLayout mRelativeLayout;

    private RsaHelper mRsaHelper;

    private byte[] cipherText;

    private byte[] signText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa);

        setupWindowAnimations();
        setupToolbar();
        setupViews();

        mRsaHelper = new RsaHelper();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setupWindowAnimations() {
        Log.v(TAG, "setupWindowAnimations");

        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
        getWindow().setReturnTransition(fade);
    }

    private void setupViews() {
        mButtonGenKeyPair = (Button) findViewById(R.id.button_genkeypair);
        mButtonEnc        = (Button) findViewById(R.id.button_encryption);
        mButtonDecry      = (Button) findViewById(R.id.button_decryption);
        mButtonSign       = (Button) findViewById(R.id.button_sign);
        mButtonVerify     = (Button) findViewById(R.id.button_verify);
        mTextViewRsa      = (TextView) findViewById(R.id.textview_rsa);
        mEditTextPlain    = (EditText) findViewById(R.id.edit_plain);
        mRelativeLayout   = (RelativeLayout) findViewById(R.id.rsa_relativeLayout);

        mTextViewRsa.setText("");
    }

    public void onButtonGenKeyPairClicked(View v) {
        try {
            KeyPair pair = mRsaHelper.generateKeys(1024);

            mTextViewRsa.append("*********生成密钥对*********\n");

            String publickey = HexStringConvert.hexToString(pair.getPublic().getEncoded());
            String privatekey = HexStringConvert.hexToString(pair.getPrivate().getEncoded());
            mTextViewRsa.append("公钥 => " + publickey + "\n");
            mTextViewRsa.append("私钥 => " + privatekey + "\n");
            mButtonEnc.setEnabled(true);
            mButtonSign.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onButtonEncClicked(View v) {
        try {
            String plain = mEditTextPlain.getText().toString();
            if(plain != null && !plain.isEmpty()) {
                cipherText = mRsaHelper.encryptByPublicKey(plain.getBytes());
                mTextViewRsa.append("***********加密***********" + "\n");
                mTextViewRsa.append("明文 => " + plain + "\n");
                mTextViewRsa.append("密文 => " + HexStringConvert.hexToString(cipherText) + "\n");
                mButtonDecry.setEnabled(true);
            } else {
                Snackbar.make(mRelativeLayout, "请输入明文!",Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onButtonDecryptClicked(View v) {
        try {
            byte[] plaintext = mRsaHelper.decryptByPrivateKey(cipherText);
            mTextViewRsa.append("***********解密***********" + "\n");
            mTextViewRsa.append("明文 => " + new String(plaintext) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onButtonSignClicked(View v) {
        try {
            String plain = mEditTextPlain.getText().toString();
            if(plain != null && !plain.isEmpty()) {
                signText = mRsaHelper.sign(plain.getBytes());
                mTextViewRsa.append("***********加密***********" + "\n");
                mTextViewRsa.append("明文 => " + plain + "\n");
                mTextViewRsa.append("签名 => " + HexStringConvert.hexToString(signText) + "\n");
                mButtonVerify.setEnabled(true);
            } else {
                Snackbar.make(mRelativeLayout, "请输入明文!",Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onButtonVerifyClicked(View v) {
        try {
            String plain = mEditTextPlain.getText().toString();
            boolean verify = mRsaHelper.verify(plain.getBytes(), signText);
            mTextViewRsa.append("***********解密***********" + "\n");
            mTextViewRsa.append("验签 => " + verify + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }
}
