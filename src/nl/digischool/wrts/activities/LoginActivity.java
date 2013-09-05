package nl.digischool.wrts.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import nl.digischool.wrts.R;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 3-7-13
 * Time: 13:43
 */
public class LoginActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlogin);

    }

    public void registerClick(View v) {

        Intent i = new Intent(this, WebActivity.class);
        i.putExtra("url", "http://www.wrts.nl/user/create");
        i.putExtra("title", getResources().getString(R.string.register));
        startActivity(i);

    }

    public void loginClick(View v) {

        ProgressDialog dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(mRes.getString(R.string.checking_data));
        dialog.setIndeterminate(true);
        dialog.setMessage(mRes.getString(R.string.a_moment_patience));
        dialog.show();

        String email = "", password = "";

        EditText emailEdit = (EditText) findViewById(R.id.emailEdit);
        EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();

        if(mApi.authenticateUser(email, password) && !email.isEmpty() && !password.isEmpty()) {
            dialog.dismiss();
            mApi.saveUserData(email, password);
            Intent i = new Intent(this, DownloadActivity.class);
            startActivity(i);
            finish();
        } else {
            dialog.dismiss();
            Toast.makeText(this, mRes.getString(R.string.bad_auth), Toast.LENGTH_SHORT).show();
        }

    }

}
