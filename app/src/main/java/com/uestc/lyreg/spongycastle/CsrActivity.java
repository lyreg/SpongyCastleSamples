package com.uestc.lyreg.spongycastle;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uestc.lyreg.spongycastle.security.CertHelper;
import com.uestc.lyreg.spongycastle.utils.HexStringConvert;

import java.security.KeyPair;

public class CsrActivity extends AppCompatActivity {
    private static final String TAG = "CsrActivity";

    private Button mButtonGenCsr;
    private TextView mTextViewCsr;
    private RelativeLayout mRelativeLayout;

    private CertHelper mCertHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csr);

        setupWindowAnimations();
        setupToolbar();
        setupViews();

        mCertHelper = new CertHelper();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setupWindowAnimations() {
        Log.v(TAG, "setupWindowAnimations");

        Explode explode = new Explode();
        explode.setInterpolator(new BounceInterpolator());
        explode.setDuration(500);
        getWindow().setEnterTransition(explode);
        getWindow().setReturnTransition(explode);
    }

    private void setupViews() {
        mButtonGenCsr = (Button) findViewById(R.id.button_gencsr);
        mTextViewCsr  = (TextView) findViewById(R.id.textview_csr);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.csr_relativeLayout);

        mTextViewCsr.setText("");
    }

    public void onButtonGenCsrClicked(View v) {
        try {
            KeyPair kp = mCertHelper.generateKeys(1024);
            String csr = mCertHelper.genCSR(kp, "CN",
                    "SiChuan", "ChengDu", "UESTC", "lyreg", "lyreg@163.com");
            mTextViewCsr.append("*********生成密钥对*********\n");

            String publickey = HexStringConvert.hexToString(kp.getPublic().getEncoded());
            String privatekey = HexStringConvert.hexToString(kp.getPrivate().getEncoded());
            mTextViewCsr.append("公钥 => " + publickey + "\n");
            mTextViewCsr.append("私钥 => " + privatekey + "\n");

            mTextViewCsr.append("**********生成CSR**********\n");

            mTextViewCsr.append(csr);
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mRelativeLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }
}
