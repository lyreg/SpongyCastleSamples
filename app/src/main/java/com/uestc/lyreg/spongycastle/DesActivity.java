package com.uestc.lyreg.spongycastle;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uestc.lyreg.spongycastle.security.DesHelper;
import com.uestc.lyreg.spongycastle.utils.HexStringConvert;

public class DesActivity extends AppCompatActivity {
    private static final String TAG = "DesActivity";

    private Button mButtonGenKey;
    private Button mButtonEnc;
    private Button mButtonDecry;
    private TextView mTextViewDes;
    private EditText mEditTextKey;
    private EditText mEditTextPlain;
    private RelativeLayout mRelativeLayout;

    private DesHelper mDesHelper;

    private byte[] cipherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);
        setupWindowAnimations();

        setupToolbar();
        setupViews();

        mDesHelper = new DesHelper();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setupViews() {
        mButtonGenKey = (Button) findViewById(R.id.button_genkey);
        mButtonEnc = (Button) findViewById(R.id.button_encryption);
        mButtonDecry = (Button) findViewById(R.id.button_decryption);
        mTextViewDes = (TextView) findViewById(R.id.textview_des);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.des_relativeLayout);
        mEditTextKey = (EditText) findViewById(R.id.edit_deskey);
        mEditTextPlain = (EditText) findViewById(R.id.edit_plaintext);

        mTextViewDes.setText("");

    }

    private void setupWindowAnimations() {
        Log.v(TAG, "setupWindowAnimations");

        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setEnterTransition(explode);
        getWindow().setReturnTransition(explode);
    }


    public void onButtonGenKeyClicked(View v) {
        try {
            byte[] key = mDesHelper.generateDesKey(56);

            mEditTextKey.setText(HexStringConvert.hexToString(key));

            mButtonEnc.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onButtonEncClicked(View v) {
        try {
            String plain = mEditTextPlain.getText().toString();
            if(plain != null && !plain.isEmpty()) {
                cipherText = mDesHelper.encryption(plain);
                mTextViewDes.append("*****加密*****" + "\n");
                mTextViewDes.append("明文 => " + plain + "\n");
                mTextViewDes.append("密文 => " + HexStringConvert.hexToString(cipherText) + "\n");
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
            byte[] plaintext = mDesHelper.decryption(cipherText);
            mTextViewDes.append("*****解密*****" + "\n");
            mTextViewDes.append("明文 => " + new String(plaintext) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

}
