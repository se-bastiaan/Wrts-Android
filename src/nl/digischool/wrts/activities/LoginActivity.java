package nl.digischool.wrts.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import nl.digischool.wrts.R;
import nl.digischool.wrts.api.ApiCallback;
import nl.digischool.wrts.classes.Utilities;
import org.xml.sax.InputSource;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 3-7-13
 * Time: 13:43
 */
public class LoginActivity extends BaseActivity implements ApiCallback {

    private ProgressDialog mDialog;
    private String mEmail, mPassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlogin);
        mApi.setCallback(this);
    }

    public void registerClick(View v) {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra("url", "http://www.wrts.nl/user/create");
        i.putExtra("title", getResources().getString(R.string.register));
        startActivity(i);
    }

    public void loginClick(View v) {
        mDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        mDialog.setTitle(mRes.getString(R.string.checking_data));
        mDialog.setIndeterminate(true);
        mDialog.setMessage(mRes.getString(R.string.a_moment_patience));
        mDialog.show();

        EditText emailEdit = (EditText) findViewById(R.id.emailEdit);
        EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        mEmail = emailEdit.getText().toString();
        mPassword = passwordEdit.getText().toString();

        if(mEmail.isEmpty() && mPassword.isEmpty()) {
            mDialog.dismiss();
            Toast.makeText(this, mRes.getString(R.string.bad_auth), Toast.LENGTH_SHORT).show();
        } else {
            mApi.authenticateUser(mEmail, mPassword);
        }

    }

    @Override
    public void apiResponseCallback(String method, String result) {
        if(method.equals("users/me")) {
            if(!result.contains("error code=\"401\"")) {
                mDialog.dismiss();
                mApi.saveUserData(mEmail, mPassword);
                Intent i = new Intent(this, DownloadActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
